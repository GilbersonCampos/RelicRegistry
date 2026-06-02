package com.gilbersoncampos.relicregistry.screen.editRecord

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.gilbersoncampos.relicregistry.BuildConfig

import com.gilbersoncampos.relicregistry.R
import com.gilbersoncampos.relicregistry.data.enums.AccessoryType
import com.gilbersoncampos.relicregistry.data.enums.BodyPosition
import com.gilbersoncampos.relicregistry.data.model.CatalogRecordModel
import com.gilbersoncampos.relicregistry.data.enums.Condition
import com.gilbersoncampos.relicregistry.data.enums.DecorationLocation
import com.gilbersoncampos.relicregistry.data.enums.DecorationType
import com.gilbersoncampos.relicregistry.data.enums.Firing
import com.gilbersoncampos.relicregistry.data.enums.GeneralBodyShape
import com.gilbersoncampos.relicregistry.data.enums.Genitalia
import com.gilbersoncampos.relicregistry.data.enums.InteriorCondition
import com.gilbersoncampos.relicregistry.data.enums.LowerLimbs
import com.gilbersoncampos.relicregistry.data.enums.ManufacturingMarks
import com.gilbersoncampos.relicregistry.data.enums.ManufacturingTechnique
import com.gilbersoncampos.relicregistry.data.enums.PaintColor
import com.gilbersoncampos.relicregistry.data.enums.PlasticDecoration
import com.gilbersoncampos.relicregistry.data.enums.StatueType
import com.gilbersoncampos.relicregistry.data.enums.SurfaceTreatment
import com.gilbersoncampos.relicregistry.data.enums.Temper
import com.gilbersoncampos.relicregistry.data.enums.UpperLimbs
import com.gilbersoncampos.relicregistry.data.enums.UsageMarks
import com.gilbersoncampos.relicregistry.data.enums.Uses
import com.gilbersoncampos.relicregistry.data.wrappers.PdfViewModelInterface
import com.gilbersoncampos.relicregistry.data.wrappers.openPDF
import com.gilbersoncampos.relicregistry.extensions.hasOnlyNumber
import com.gilbersoncampos.relicregistry.extensions.toBrDateTime
import com.gilbersoncampos.relicregistry.extensions.toOnlyFloat
import com.gilbersoncampos.relicregistry.ui.components.Accordion
import com.gilbersoncampos.relicregistry.ui.components.AlertDialogCustom
import com.gilbersoncampos.relicregistry.ui.components.CreateEditRecordModal
import com.gilbersoncampos.relicregistry.ui.components.CustomDropdown
import com.gilbersoncampos.relicregistry.ui.components.ImageCarrousel
import com.gilbersoncampos.relicregistry.ui.components.ListCheckbox
import com.gilbersoncampos.relicregistry.ui.components.Session
import com.gilbersoncampos.relicregistry.ui.components.SubSession
import com.gilbersoncampos.relicregistry.ui.components.TextCheckbox
import com.gilbersoncampos.relicregistry.ui.components.TextRadioButton
import com.gilbersoncampos.relicregistry.ui.theme.RelicRegistryTheme
import java.time.format.DateTimeFormatter
import kotlin.math.round

@Composable
fun EditRecord(
    idRecord: Long,
    onBack: () -> Unit,
    viewModel: EditRecordViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    LaunchedEffect(idRecord) {
        viewModel.getRecord(idRecord)
    }
    EditRecordUi(
        uiState = uiState,
        updateRecord = viewModel::updateUiState,
        saveRecord = viewModel::saveRecord,
        saveImages = viewModel::saveImages,
        onBack = onBack,
        generatePdf = {
            openPDF(viewModel, context)
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecordUi(
    uiState: EditRecordUiState,
    updateRecord: (CatalogRecordModel) -> Unit,
    saveRecord: () -> Unit,
    saveImages: (List<Uri>) -> Unit, generatePdf: () -> Unit,
    onBack: () -> Unit
) {


    when (uiState) {
        is EditRecordUiState.Error -> Text(text = uiState.message)
        EditRecordUiState.Loading -> Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

        is EditRecordUiState.Success -> Column {
            TopAppBar(
                title = { Text(text = "Ficha: ${uiState.state.record.identification}") },
                actions = {
                    Row {
                        if (!uiState.state.isSynchronized) {
                            IconButton(onClick = { saveRecord() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_save),
                                    contentDescription = "save_record"
                                )
                            }
                        }

                    }
                })
            EditRecordForm(
                uiState = uiState.state,
                updateRecord = updateRecord,
                saveImages = saveImages,
                onBack = onBack,
                generatePdf = generatePdf
            )
        }

    }
}


@Composable
fun EditRecordForm(
    uiState: SuccessUiState,
    updateRecord: (CatalogRecordModel) -> Unit,
    saveImages: (List<Uri>) -> Unit,
    generatePdf: () -> Unit,
    onBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = !uiState.isSynchronized) {
        showDialog = true
    }
    if (showEditDialog) {
        CreateEditRecordModal(onCreate = { numbering, place, shelf, group ->
            updateRecord(uiState.record.copy( identification = numbering, archaeologicalSite = place, shelfLocation = shelf, group = group))
            showEditDialog = false
        }, defaultValues = uiState.record ,isEditing = true) {
            showEditDialog = false
        }
    }
    if (showDialog) {
        AlertDialogCustom(
            title = "Ficha com alterações pendentes",
            text = "Deseja sair sem salvar as alterações?",
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                onBack()
            })
    }

    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    selectedImage?.let { bitmap ->
        FullScreenImageDialog(bitmap = bitmap, onDismiss = { selectedImage = null })
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        item {
            if (uiState.images.isNotEmpty()) {
                ImageCarrousel(
                    uiState.images,
                    modifier = Modifier
                        .padding(16.dp)
                        .height(200.dp)
                        .border(0.dp, Color.Black, shape = RoundedCornerShape(12.dp))
                        .background(Color.Black, shape = RoundedCornerShape(12.dp))

                ) { selectedImage = it }
            } else {
                Column(
                    Modifier
                        .height(200.dp)
                        .padding(16.dp)
                        .background(
                            Color.Gray,
                            RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image_basic),
                        contentDescription = "", modifier = Modifier.size(50.dp), tint = Color.White
                    )
                }
            }


            val pickerMedia =
                rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                    if (uris.isNotEmpty()) {
                        saveImages(uris)
                    } else {
                        //TODO Adicionar texto de informação
                        Log.d("PhotoPicker", "No media selected")
                    }
                }
            Row {

                Button(onClick = { pickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                    Text(text = "Selecionar fotos")
                }
                Button(onClick = generatePdf) {
                    Text(text = "Gerar Pdf")
                }
            }
            Sessions(uiState, updateRecord,{showEditDialog=true})
        }

    }
}

@Composable
private fun FullScreenImageDialog(bitmap: Bitmap, onDismiss: () -> Unit) {
    val scale = remember { mutableStateOf(1f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val imageSize = remember { mutableStateOf(IntSize(0, 0)) }
    val boxSize = remember { mutableStateOf(IntSize(0, 0)) }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            securePolicy = SecureFlagPolicy.SecureOff,
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { onDismiss() }
                .pointerInput(Unit) {
                    detectTransformGestures(panZoomLock = true) { _, pan, zoom, _ ->
                        val newScale = scale.value * zoom
                        scale.value = newScale.coerceIn(1f, 5f) // Limite de zoom

                        if (scale.value > 1f) {
                            val maxX =
                                ((imageSize.value.width * scale.value - boxSize.value.width) / 2).coerceAtLeast(
                                    0f
                                )
                            val maxY =
                                ((imageSize.value.height * scale.value - boxSize.value.height) / 2).coerceAtLeast(
                                    0f
                                )

                            val newOffsetX = (offset.value.x + pan.x).coerceIn(-maxX, maxX)
                            val newOffsetY = (offset.value.y + pan.y).coerceIn(-maxY, maxY)

                            offset.value = Offset(newOffsetX, newOffsetY)
                        } else {
                            offset.value = Offset.Zero
                        }
                    }
                }
                .onSizeChanged { size ->
                    boxSize.value = size
                }
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        translationX = offset.value.x,
                        translationY = offset.value.y
                    )
                    .onSizeChanged { size ->
                        imageSize.value = size
                    }
                    .padding(16.dp),
                contentScale = ContentScale.Fit // Ajusta a imagem para caber na tela
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Sessions(
    uiState: SuccessUiState,
    updateRecord: (CatalogRecordModel) -> Unit,
    openEditingModal: () -> Unit
) {
    Session(title = "Dados da Ficha", modifier = Modifier.padding(16.dp), edit = openEditingModal) {

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            InfosRow(title = "Numeração:", value = uiState.record.identification)
            InfosRow(title = "Sítio arqueológico:", value = uiState.record.archaeologicalSite)
            InfosRow(title = "Prateleira:", value = uiState.record.shelfLocation)
            InfosRow(title = "Grupo:", value = uiState.record.group)
            uiState.record.createdAt?.let {
                InfosRow(title = "Criado em:", value = it.toBrDateTime())
            }

        }
    }
    DimensionSession(uiState.record, updateRecord)
    TopologySession(uiState.record, updateRecord)
    MorfologySession(uiState.record, updateRecord)
    TecnologySession(uiState.record, updateRecord)
    DecorationSession(uiState.record, updateRecord)
    UsesSession(uiState.record, updateRecord)
}

@Composable
private fun UsesSession(uiState: CatalogRecordModel, updateRecord: (CatalogRecordModel) -> Unit) {
    Accordion(
        title = "Usos", modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {

            ListCheckbox(list = Uses.entries, uiState.uses) { selected, _ ->
                updateRecord(uiState.copy(uses = selected))

            }
        }
    }
}

@Composable
private fun DecorationSession(
    uiState: CatalogRecordModel,
    updateRecord: (CatalogRecordModel) -> Unit
) {

    val hasDecoration = uiState.hasDecoration
    Accordion(title = "Decoração", modifier = Modifier.fillMaxWidth()) {
        Column {
            TextCheckbox(
                onCheckedChange = { hasSelected ->
                    updateRecord(
                        uiState.copy(
                            hasDecoration = hasSelected,
                            decorationLocation = null,
                            decorationType = emptyList(),
                            internalPaintColor = emptyList(),
                            externalPaintColor = emptyList(),
                            plasticDecoration = emptyList()
                        )
                    )
                },
                label = "Possui decoraçao",
                hasDecoration
            )
            if (hasDecoration) {
                SubSession(title = "Local") {
                    TextRadioButton(
                        DecorationLocation.entries,
                        uiState.decorationLocation
                    ) {
                        updateRecord(uiState.copy(decorationLocation = it))
                    }
                }
                SubSession(title = "Tipo de Decoraçao") {
                    ListCheckbox(
                        list = DecorationType.entries,
                        uiState.decorationType
                    ) { selected, _ ->
                        updateRecord(uiState.copy(decorationType = selected))

                    }
                }
                SubSession(title = "Cor da pintura (F.I.)") {
                    ListCheckbox(
                        list = PaintColor.entries,
                        uiState.internalPaintColor
                    ) { selected, _ ->
                        updateRecord(uiState.copy(internalPaintColor = selected))

                    }
                }
                SubSession(title = "Cor da pintura (F.E.)") {
                    ListCheckbox(
                        list = PaintColor.entries,
                        uiState.externalPaintColor
                    ) { selected, _ ->
                        updateRecord(uiState.copy(externalPaintColor = selected))

                    }
                }
                SubSession(title = "Decoração plástica") {
                    ListCheckbox(
                        list = PlasticDecoration.entries,
                        uiState.plasticDecoration
                    ) { selected, _ ->
                        updateRecord(uiState.copy(plasticDecoration = selected))

                    }
                }
            }
        }

    }
}

@Composable
private fun TecnologySession(
    uiState: CatalogRecordModel,
    updateRecord: (CatalogRecordModel) -> Unit
) {

    Accordion(title = "Tecnologia", modifier = Modifier.fillMaxWidth()) {

        Column {
            SubSession(title = "Queima") {
                ListCheckbox(list = Firing.entries, uiState.firing) { selected, _ ->
                    updateRecord(uiState.copy(firing = selected))

                }
            }
            SubSession(title = "Antiplástico") {
                ListCheckbox(list = Temper.entries, uiState.temper) { selected, _ ->
                    updateRecord(uiState.copy(temper = selected))

                }
            }
            SubSession(title = "Técnica de fabricação") {
                ListCheckbox(
                    list = ManufacturingTechnique.entries,
                    uiState.manufacturingTechnique
                ) { selected, _ ->
                    updateRecord(uiState.copy(manufacturingTechnique = selected))

                }
            }
            SubSession(title = "Marcas de fabricação") {
                ListCheckbox(
                    list = ManufacturingMarks.entries,
                    uiState.manufacturingMarks
                ) { selected, _ ->
                    updateRecord(uiState.copy(manufacturingMarks = selected))

                }
            }
            SubSession(title = "Marcas de uso") {
                ListCheckbox(list = UsageMarks.entries, uiState.usageMarks) { selected, _ ->
                    updateRecord(uiState.copy(usageMarks = selected))

                }
            }
            SubSession(title = "Tratamento de SUP. (I.T.)") {
                ListCheckbox(
                    list = SurfaceTreatment.entries,
                    uiState.surfaceTreatmentInternal
                ) { selected, _ ->
                    updateRecord(uiState.copy(surfaceTreatmentInternal = selected))

                }
            }
            SubSession(title = "Tratamento de SUP. (E.T.)") {
                ListCheckbox(
                    list = SurfaceTreatment.entries,
                    uiState.surfaceTreatmentExternal
                ) { selected, _ ->
                    updateRecord(uiState.copy(surfaceTreatmentExternal = selected))

                }
            }
        }


    }
}

@Composable
private fun MorfologySession(
    uiState: CatalogRecordModel,
    updateRecord: (CatalogRecordModel) -> Unit
) {

    Accordion(title = "Morfologia", modifier = Modifier.fillMaxWidth()) {
        Column {
            SubSession(title = "Membros superiores") {
                ListCheckbox(
                    list = UpperLimbs.entries,
                    uiState.upperLimbs
                ) { selected, _ ->
                    updateRecord(uiState.copy(upperLimbs = selected))
                }
            }
            SubSession(title = "Membros inferiores") {
                ListCheckbox(
                    list = LowerLimbs.entries,
                    uiState.lowerLimbs
                ) { selected, _ ->
                    updateRecord(uiState.copy(lowerLimbs = selected))
                }
            }
            SubSession(title = "Fabricação de genitália") {
                var hasGenitalia by remember {
                    mutableStateOf(false)
                }
                TextCheckbox(
                    onCheckedChange = { checked ->
                        hasGenitalia = checked
                    },
                    label = "Possui genitália",
                    hasGenitalia
                )
                if (hasGenitalia) {

                    TextRadioButton(
                        Genitalia.entries,
                        uiState.genitalia
                    ) {
                        updateRecord(uiState.copy(genitalia = it))
                    }
                }
            }
            SubSession(title = "Posição corporal") {
                ListCheckbox(list = BodyPosition.entries, uiState.bodyPosition) { selected, _ ->
                    updateRecord(uiState.copy(bodyPosition = selected))
                }
            }
            SubSession(title = "Outros atributos formais") {
                ListCheckbox(
                    //TODO adiconar os outros enums
                    list = AccessoryType.entries,
                    uiState.otherFormalAttributes
                ) { selected, _ ->
                    updateRecord(uiState.copy(otherFormalAttributes = selected))

                }
            }
        }
    }
}

@Composable
private fun TopologySession(
    uiState: CatalogRecordModel,
    updateRecord: (CatalogRecordModel) -> Unit
) {

    Accordion(title = "Tipologia", modifier = Modifier.fillMaxWidth()) {
        Column {
            SubSession(title = "Tipo") {
                CustomDropdown(
                    title = "Tipo",
                    list = StatueType.entries,
                    selectedState = uiState.statueType,
                    onSelect = {
                        updateRecord(uiState.copy(statueType = it))
                    })
            }
            SubSession(title = "Condição") {
                CustomDropdown(
                    title = "Condição",
                    list = Condition.entries,
                    selectedState = uiState.condition,
                    onSelect = {
                        updateRecord(uiState.copy(condition = it))
                    })
            }
            SubSession(title = "Forma geral do Corpo") {
                CustomDropdown(
                    title = "Forma geral do Corpo",
                    list = GeneralBodyShape.entries,
                    selectedState = uiState.generalBodyShape,
                    onSelect = {
                        updateRecord(uiState.copy(generalBodyShape = it))
                    })
            }
            SubSession(title = "Condição Interna") {
                CustomDropdown(
                    title = "Condição Interna",
                    list = InteriorCondition.entries,
                    selectedState = uiState.interiorCondition,
                    onSelect = {
                        updateRecord(uiState.copy(interiorCondition = it))
                    })
            }
        }


    }
}

@Composable
private fun DimensionSession(
    uiState: CatalogRecordModel,
    updateRecord: (CatalogRecordModel) -> Unit
) {
    var length by remember {
        mutableStateOf(uiState.length?.toString() ?: "")
    }
    var width by remember {
        mutableStateOf(uiState.width?.toString() ?: "")
    }
    var height by remember {
        mutableStateOf(uiState.height?.toString() ?: "")
    }
    var weight by remember {
        mutableStateOf(uiState.weight?.toString() ?: "")
    }
    Accordion(title = "Dimensões", modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    label = { Text(text = "Comprimento (cm)") },
                    value = length,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            length = newValue

                            val formatted = newValue.toFloatOrNull()?.let {
                                round(it * 100) / 100
                            }
                            length = newValue
                            updateRecord(uiState.copy(length = formatted))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    label = { Text(text = "Largura (cm)") },
                    value = width,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            width = newValue

                            val formatted = newValue.toFloatOrNull()?.let {
                                round(it * 100) / 100
                            }
                            width = newValue

                            updateRecord(uiState.copy(width = formatted))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    label = { Text(text = "Altura (cm)") },
                    value = height,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            height = newValue

                            val formatted = newValue.toFloatOrNull()?.let {
                                round(it * 100) / 100
                            }
                            height = newValue
                            updateRecord(uiState.copy(height = formatted))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    label = { Text(text = "Peso (g)") },
                    value = weight,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() || it == '.' }) {
                            weight = newValue

                            val formatted = newValue.toFloatOrNull()?.let {
                                round(it * 100) / 100
                            }
                            weight = newValue
                            updateRecord(uiState.copy(weight = formatted))
                        }

                    },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

    }
}

@Composable
private fun InfosRow(title: String, value: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}


@Composable
@Preview(showBackground = true)
@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,)
fun EditRecordPreview() {
    RelicRegistryTheme{
    EditRecordUi(
        uiState = EditRecordUiState.Loading,
        updateRecord = { },
        saveRecord = {},
        saveImages = {},
        onBack = {},
        generatePdf = {}
    )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,)
fun EditRecordFormPreview() {
    RelicRegistryTheme {
        EditRecordForm(uiState =
        SuccessUiState(
            record = CatalogRecordModel(
                0,
                listImages = listOf(),
                "8",
                "caboclo",
                "12",
                "2",
                "2",
                observations = ""
            ),
            isSynchronized = true,
            images = listOf()
        ), {}, {}, {}, {}
        )
    }
}