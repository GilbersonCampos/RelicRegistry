package com.gilbersoncampos.relicregistry.data.repository.Impl

import android.util.Log
import com.gilbersoncampos.relicregistry.Constants.DATE_FORMATER
import com.gilbersoncampos.relicregistry.builder.JsonBuilder
import com.gilbersoncampos.relicregistry.data.local.dao.RecordDao
import com.gilbersoncampos.relicregistry.data.mapper.toEntity
import com.gilbersoncampos.relicregistry.data.mapper.toModel
import com.gilbersoncampos.relicregistry.data.model.CatalogRecordModel
import com.gilbersoncampos.relicregistry.data.model.HistoricSyncModel
import com.gilbersoncampos.relicregistry.data.model.StatusSync
import com.gilbersoncampos.relicregistry.data.remote.RemoteDataSource
import com.gilbersoncampos.relicregistry.data.repository.HistoricSyncRepository
import com.gilbersoncampos.relicregistry.data.repository.RecordRepository
import com.gilbersoncampos.relicregistry.worker.HistoricListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(private val recordDao: RecordDao,private val remoteDataSource: RemoteDataSource,val historicRepository: HistoricSyncRepository) :
    RecordRepository {
    override suspend fun createRecord(record: CatalogRecordModel) {
        recordDao.createRecord(record.toEntity())
        val recordCreated =getLastRecord()
        //TODO("Colocar no método de sync")
//        val idRemote=remoteDataSource.createRecord(recordCreated)
//        updateRecord(recordCreated.copy(idRemote=idRemote))
    }

    override suspend fun getAllRecord(): Flow<List<CatalogRecordModel>> {
        return flow { recordDao.getAllRecord().collect { list -> emit(list.map { it.toModel() }) } }
    }

    override suspend fun getLastRecord(): CatalogRecordModel {
        return recordDao.getLastRecord().toModel()
    }

    override suspend fun getRecordById(id: Long): Flow<CatalogRecordModel> {
        return flow { recordDao.getRecordById(id).collect { record -> emit(record.toModel()) } }
    }

    override suspend fun updateRecord(record: CatalogRecordModel) {
        recordDao.updateRecord(record.copy(updatedAt = LocalDateTime.now()).toEntity())
    }

    override suspend fun removeRecords(recordList: List<CatalogRecordModel>) {
        val idList = recordList.map { it.id }
        recordDao.deleteRecords(idList)
    }

    override suspend fun getAllArchaeologicalSite(): Flow<List<String>> {
        return flow { recordDao.getAllArchaeologicalSite().collect{ emit(it.groupingBy { aas-> aas.trim()}.eachCount().keys.toList())} }
    }
    override suspend fun syncRecords(listener: HistoricListener) {
        val builder= JsonBuilder.Builder().addMessage("Iniciando sincronização...")
        Log.d("SYNC", "Iniciando sincronização...")
        var historic= HistoricSyncModel(id = 0, startIn = LocalDateTime.now(), endIn = null, status = StatusSync.LOADING, data = "")
        historicRepository.createHistoricSync(historic)
        historic=historicRepository.getLastHistoricSync()
        syncLocalToServer(historic,listener,builder)
        builder.addMessage("Registros locais enviados. Buscando registros remotos...")
        Log.d("SYNC", "Registros locais enviados. Buscando registros remotos...")
        historic=historicRepository.getHistoricSync(historic.id)
        syncServerToLocal(historic,listener,builder)
        historic=historicRepository.getHistoricSync(historic.id)
        builder.addMessage("Sincronização concluída.")
        listener.updateHistoric(historic.copy(endIn = LocalDateTime.now(), status = StatusSync.SUCCESS, data = builder.build()))
        Log.d("SYNC", "Sincronização concluída.")
    }

    private suspend fun syncServerToLocal(historic: HistoricSyncModel,listener: HistoricListener,builder: JsonBuilder) {
        val localRecords =
            recordDao.getAllRecord().firstOrNull()?.map { it.toModel() } ?: emptyList()
        try {
            val remoteRecords = remoteDataSource.getAllRecord().firstOrNull() ?: emptyList()

            val localRecordsMapByIdRemote = localRecords
                .filter { it.idRemote != null }
                .associateBy { it.idRemote }

            for (remoteRecord in remoteRecords) {
                val existingLocalRecord = localRecordsMapByIdRemote[remoteRecord.idRemote]

                if (existingLocalRecord == null) {
                    // Registro existe no servidor, mas não localmente -> Adicionar localmente
                    builder.addMessage("Baixando novo registro do servidor: ${remoteRecord.idRemote}")
                    Log.d("SYNC", "Baixando novo registro do servidor: ${remoteRecord.idRemote}")
                    recordDao.createRecord(remoteRecord.toEntity()) // Assume que o id local será gerado automaticamente
                    // ou que toEntity() lida com id local nulo para inserção.
                    // Se o `id` local é o mesmo que o do servidor, você precisa garantir
                    // que o `CatalogRecordModel` vindo do `remoteDataSource`
                    // não tenha um `id` que conflite com a chave primária local,
                    // a menos que você queira que eles sejam os mesmos.
                    // Geralmente, o `id` local é autogerado e diferente do `idRemote`.
                } else {
                    remoteRecord.idRemote?.let {

                        val localRecord= recordDao.getRecordByIdRemote(it).firstOrNull()
                        if(needsUpdate(localRecord?.updatedAt,remoteRecord.updatedAt)){
                            recordDao.updateRecord(remoteRecord.copy(id = localRecord?.id?:0).toEntity())
                        }
                    }
                    // Registro existe em ambos. Verificar se precisa atualizar localmente.
                    // Isso requer um campo de timestamp ou versão para comparação.
                    // Exemplo simplificado: sobrescrever o local com o remoto se eles forem diferentes (exceto pelo id local).
                    // Você precisará de uma lógica mais sofisticada para evitar perda de dados se o local foi editado.
                    // if (needsUpdate(existingLocalRecord, remoteRecord)) {
                    //    Log.d("SYNC", "Atualizando registro local ${existingLocalRecord.id} com dados do servidor ${remoteRecord.idRemote}")
                    //    recordDao.updateRecord(remoteRecord.copy(id = existingLocalRecord.id).toEntity()) // Mantém o ID local
                    // }
                }
            }
        } catch (e: Exception) {
            Log.e("SYNC", "Erro ao buscar ou processar registros remotos: ${e.message}", e)
            builder.addMessage("Erro ao buscar ou processar registros remotos: ${e.message}")
            val newHistoric=historic.copy(status = StatusSync.ERROR,errorMessage = e.message)
            listener.updateHistoric(newHistoric)
        }
    }

    private suspend fun syncLocalToServer(historic: HistoricSyncModel,listener: HistoricListener,builder: JsonBuilder): List<CatalogRecordModel> {
        val localRecords =
            recordDao.getAllRecord().firstOrNull()?.map { it.toModel() } ?: emptyList()

        for (localRecord in localRecords) {
            try {
                if (localRecord.idRemote == null) {
                    builder.addMessage("Criando registro local ${localRecord.id} no servidor...")
                    Log.d("SYNC", "Criando registro local ${localRecord.id} no servidor...")
                    // Supondo que seu RemoteDataSource tenha um createRecordRemote que retorna o ID remoto
                    val remoteId =
                        remoteDataSource.createRecord(localRecord) // Certifique-se que este método existe e funciona
                    if (remoteId != null) {
                        // Atualiza o registro local com o ID remoto
                        recordDao.updateRecord(localRecord.copy(idRemote = remoteId).toEntity())
                        builder.addMessage("Registro local ${localRecord.id} atualizado com idRemote: $remoteId")

                    } else {
                        builder.addMessage("Falha ao obter idRemote para o registro local ${localRecord.id}")
                    }
                } else {
                    Log.d(
                        "SYNC",
                        "Verificando atualização para registro local ${localRecord.id} (remoto ${localRecord.idRemote}) no servidor..."
                    )
                    val remoteRecord = remoteDataSource.getRecordByIdRemote(localRecord.idRemote)
                    if(needsUpdate(remoteRecord?.updatedAt,localRecord.updatedAt)){
                        Log.d(
                            "SYNC",
                            "Atualizando ${localRecord.id} para o remoto (remoto ${localRecord.idRemote}) no servidor..."
                        )
                        builder.addMessage("Atualizando ${localRecord.id} para o remoto (remoto ${localRecord.idRemote}) no servidor...")

                        remoteDataSource.updateRecord(localRecord)
                    }
                    //verificação de updated.
                    // Lógica para atualizar registros existentes no servidor (se modificados localmente)
                    // Para simplificar, vamos assumir que se tem idRemote, já está lá e poderia ser atualizado.
                    // Uma verificação de "modificado localmente" seria ideal aqui.
                    // remoteDataSource.updateRecordRemote(localRecord) // Descomente se tiver essa lógica
                }
            } catch (e: Exception) {
                Log.e(
                    "SYNC",
                    "Erro ao enviar registro local ${localRecord.id} para o servidor: ${e.message}",
                    e
                )
                builder.addMessage("Erro ao enviar registro local ${localRecord.id} para o servidor: ${e.message}")

                val newHistoric=historic.copy(status = StatusSync.ERROR,errorMessage = e.message)
                listener.updateHistoric(newHistoric)
                // Considere uma estratégia de retry ou enfileiramento para falhas
            }
        }
        return localRecords
    }
    private fun needsUpdate(firstDate: LocalDateTime?, secondDate: LocalDateTime?): Boolean {
        if (secondDate == null&&firstDate == null) return false
        if(secondDate == null) return false
        if(firstDate == null) return true
        return secondDate.isAfter(firstDate)
    }

}
