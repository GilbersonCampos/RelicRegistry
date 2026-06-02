package com.gilbersoncampos.relicregistry.screen.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.gilbersoncampos.relicregistry.data.model.CatalogRecordModel
import com.gilbersoncampos.relicregistry.data.services.ImageStoreService
import com.gilbersoncampos.relicregistry.data.services.PdfService
import com.gilbersoncampos.relicregistry.data.useCase.DeleteCacheUseCase
import com.gilbersoncampos.relicregistry.data.wrappers.PdfViewModelInterface
import com.gilbersoncampos.relicregistry.screen.editRecord.EditRecordUiState
import com.gilbersoncampos.relicregistry.screen.historic.navigateToHistoric
import com.gilbersoncampos.relicregistry.screen.recordList.RecordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val deleteCacheUseCase: DeleteCacheUseCase,private val pdfService: PdfService) :
    ViewModel(), PdfViewModelInterface {
    private var _settingOptions = MutableStateFlow<List<SettingModel>>(
        mutableListOf(SettingModel("Limpar cache", ActionToClick.OnClearCache),
        SettingModel("Histórico de sync",ActionToClick.OnNavigateHistoric))
    )
    val settingOptions: StateFlow<List<SettingModel>> = _settingOptions.asStateFlow()
//    val settingOptions = listOf(SettingModel("Limpar cache", ActionToClick.OnClearCache),
//        SettingModel("Histórico de sync",ActionToClick.OnNavigateHistoric))
    fun addOption(settingModel: SettingModel) {
        _settingOptions.value = _settingOptions.value.plus(settingModel)
    }
    fun onClickOption(action: ActionToClick,navHostController: NavHostController?) {
        when (action) {
            ActionToClick.OnClearCache -> {
                deleteCacheUseCase()
            }
           is ActionToClick.Default -> {
                action.click()
            }
            ActionToClick.OnNavigateHistoric ->{
                navHostController?.navigateToHistoric()
            }


        }
    }
    override fun generatePdf() {
        viewModelScope.launch(Dispatchers.IO) {
            val catalogEmpty=CatalogRecordModel(0, archaeologicalSite = "", identification = "", group = "", shelfLocation = "", classification = "", observations = "")
            pdfService.generatePdf(
                catalogEmpty,
                listImages = listOf()
            )
        }
    }

    override fun getPdf(): File {
        return pdfService.getPDF()
    }

    companion object {
        //val settingOptions = listOf(SettingModel("Limpar cache"))
    }
}

data class SettingModel(val name: String, val action: ActionToClick)
sealed interface ActionToClick {
    data object OnClearCache : ActionToClick
    data object OnNavigateHistoric : ActionToClick
    data class Default(val click:()->Unit) : ActionToClick
}