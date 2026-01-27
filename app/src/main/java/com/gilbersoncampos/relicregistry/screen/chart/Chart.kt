package com.gilbersoncampos.relicregistry.screen.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.gilbersoncampos.relicregistry.data.enums.FilterEnum
import com.gilbersoncampos.relicregistry.ui.components.CustomDropdown
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import  com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer

@Composable
fun ChartsScreen(viewModel: ChartViewModel= hiltViewModel()) {
    Column {
        val list by viewModel.barChartData.collectAsState()
        val filterSelected by viewModel.selectedFilter.collectAsState()
        val siteSelected by viewModel.siteSelected.collectAsState()
        val siteList by viewModel.siteList.collectAsState()
        Text("Escolha o sitio")
        CustomDropdown(
            title = "Escolha o sitio",
            list = siteList,
            selectedState = siteSelected,
            onSelect = {
                viewModel.setSiteSelected(it)
            }, hasSearch = true)
        Text("Escolha o parâmetro")
        CustomDropdown(
            title = "Parâmetro",
            list = FilterEnum.entries,
            selectedState = filterSelected,
            onSelect = {
               viewModel.setFilterSelected(it)
            })

        val barChartData = BarChartData(
            bars = list, padBy = 2f
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(vertical = 8.dp)) {

        BarChart(barChartData = barChartData, xAxisDrawer = SimpleXAxisDrawer(axisLineColor = Color.White), yAxisDrawer =SimpleYAxisDrawer(axisLineColor = Color.White, labelTextColor = Color.White) , labelDrawer = SimpleValueDrawer(labelTextColor = Color.White), modifier = Modifier.fillMaxSize())
        }
    }
}