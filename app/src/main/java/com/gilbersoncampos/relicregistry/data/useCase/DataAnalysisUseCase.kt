package com.gilbersoncampos.relicregistry.data.useCase

import androidx.compose.ui.graphics.Color
import com.gilbersoncampos.relicregistry.data.enums.AccessoryType
import com.gilbersoncampos.relicregistry.data.enums.BodyPosition
import com.gilbersoncampos.relicregistry.data.enums.Condition
import com.gilbersoncampos.relicregistry.data.enums.DecorationLocation
import com.gilbersoncampos.relicregistry.data.enums.DecorationType
import com.gilbersoncampos.relicregistry.data.enums.FilterEnum
import com.gilbersoncampos.relicregistry.data.enums.Firing
import com.gilbersoncampos.relicregistry.data.enums.GeneralBodyShape
import com.gilbersoncampos.relicregistry.data.enums.Genitalia
import com.gilbersoncampos.relicregistry.data.enums.LowerLimbs
import com.gilbersoncampos.relicregistry.data.enums.ManufacturingMarks
import com.gilbersoncampos.relicregistry.data.enums.ManufacturingTechnique
import com.gilbersoncampos.relicregistry.data.enums.PaintColorExternal
import com.gilbersoncampos.relicregistry.data.enums.PaintColorInternal
import com.gilbersoncampos.relicregistry.data.enums.PlasticDecoration
import com.gilbersoncampos.relicregistry.data.enums.StatueType
import com.gilbersoncampos.relicregistry.data.enums.SurfaceTreatmentExternal
import com.gilbersoncampos.relicregistry.data.enums.SurfaceTreatmentInternal
import com.gilbersoncampos.relicregistry.data.enums.Temper
import com.gilbersoncampos.relicregistry.data.enums.UpperLimbs
import com.gilbersoncampos.relicregistry.data.enums.UsageMarks
import com.gilbersoncampos.relicregistry.data.enums.Uses
import com.gilbersoncampos.relicregistry.data.repository.RecordRepository
import com.gilbersoncampos.relicregistry.exceptions.AppException
import com.gilbersoncampos.relicregistry.extensions.getNameTranslated
import com.gilbersoncampos.relicregistry.ui.theme.primaryLight
import com.github.tehras.charts.bar.BarChartData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataAnalysisUseCase @Inject constructor(private val repository: RecordRepository) {
    operator fun invoke(site: String?, filter: FilterEnum): Flow<List<BarChartData.Bar>> {
        return flow {
            repository.getAllRecord().map { recordsList ->
                val records =
                    site?.let { site -> recordsList.filter { catalogRecord -> catalogRecord.archaeologicalSite.trim() == site.trim() } }
                        ?: recordsList
                when (filter) {
                    FilterEnum.GENITALIA -> records.groupingBy { it.genitalia }.eachCount()
                    FilterEnum.STATUE_TYPE -> records.groupingBy { it.statueType }.eachCount()
                    FilterEnum.CONDITION -> records.groupingBy { it.condition }.eachCount()
                    FilterEnum.GENERAL_BODY_SHAPE -> records.groupingBy { it.generalBodyShape }
                        .eachCount()

                    FilterEnum.UPPER_LIMBS -> records.flatMap { it.upperLimbs }.groupingBy { it }
                        .eachCount()

                    FilterEnum.LOWER_LIMBS -> records.flatMap { it.lowerLimbs }.groupingBy { it }
                        .eachCount()

                    FilterEnum.FIRING -> records.flatMap { it.firing }.groupingBy { it }.eachCount()
                    FilterEnum.TEMPER -> records.flatMap { it.temper }.groupingBy { it }.eachCount()
                    FilterEnum.MANUFACTURING_TECHNIQUE -> records.flatMap { it.manufacturingTechnique }
                        .groupingBy { it }.eachCount()

                    FilterEnum.MANUFACTURING_MARKS -> records.flatMap { it.manufacturingMarks }
                        .groupingBy { it }.eachCount()

                    FilterEnum.USAGE_MARKS -> records.flatMap { it.usageMarks }.groupingBy { it }
                        .eachCount()

                    FilterEnum.SURFACE_TREATMENT_EXTERNAL -> records.flatMap { it.surfaceTreatmentExternal }
                        .groupingBy { it }.eachCount()

                    FilterEnum.SURFACE_TREATMENT_INTERNAL -> records.flatMap { it.surfaceTreatmentInternal }
                        .groupingBy { it }.eachCount()

                    FilterEnum.DECORATION_LOCATION -> records.groupingBy { it.decorationLocation }
                        .eachCount()

                    FilterEnum.DECORATION_TYPE -> records.flatMap { it.decorationType }
                        .groupingBy { it }.eachCount()

                    FilterEnum.PAINT_COLOR_INTERNAL -> records.flatMap { it.internalPaintColor }
                        .groupingBy { it }.eachCount()

                    FilterEnum.PAINT_COLOR_EXTERNAL -> records.flatMap { it.externalPaintColor }
                        .groupingBy { it }.eachCount()

                    FilterEnum.PLASTIC_DECORATION -> records.flatMap { it.plasticDecoration }
                        .groupingBy { it }.eachCount()

                    FilterEnum.ACCESSORY_TYPE -> records.flatMap { it.otherFormalAttributes }
                        .groupingBy { it }.eachCount()

                    FilterEnum.BODY_POSITION -> records.flatMap { it.bodyPosition }
                        .groupingBy { it }.eachCount()

                    FilterEnum.USES -> records.flatMap { it.uses }.groupingBy { it }.eachCount()
                    else -> throw AppException.FilterNotFoundException
                }


            }.map { list ->
                list.map { item ->
                    BarChartData.Bar(
                        value = item.value.toFloat(),
                        label = item.key?.getNameTranslated() ?:"Não possui",
                        color = primaryLight
                    )
                }
            }.collect { emit(it) }
        }
    }
}