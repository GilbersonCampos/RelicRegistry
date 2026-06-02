package com.gilbersoncampos.relicregistry.screen.config

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gilbersoncampos.relicregistry.data.wrappers.openPDF

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel(),navHostController: NavHostController,onVerifyUpdate:()->Unit) {
    val listOptions by viewModel.settingOptions.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.addOption(SettingModel("Verificar Atualizações",ActionToClick.Default{onVerifyUpdate()}))
        viewModel.addOption(SettingModel("Imprimir Modelo",ActionToClick.Default{ openPDF(viewModel, context) }))
    }
    SettingsUi(
        listOptions, viewModel::onClickOption,
        navHostController = navHostController,
    )
}

@Composable
fun SettingsUi(listOptions: List<SettingModel>, onClickOption:(ActionToClick,NavHostController?)->Unit,navHostController: NavHostController?=null) {
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(listOptions) {
                SettingOptionComponent(it){
                    onClickOption(it.action,navHostController)
                }
            }
        }
    }
}

@Composable
fun SettingOptionComponent(option: SettingModel,onClick:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "icon")
        Text(option.name)
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsUiPreview() {
    SettingsUi(listOf(),{_,_->})
}

@Composable
@Preview(showBackground = true)
fun SettingOptionComponentPreview() {
    SettingOptionComponent(SettingModel("",ActionToClick.Default({}))){}
}