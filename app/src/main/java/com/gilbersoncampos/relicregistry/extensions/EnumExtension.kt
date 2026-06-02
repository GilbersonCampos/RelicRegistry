package com.gilbersoncampos.relicregistry.extensions

import com.gilbersoncampos.relicregistry.data.enums.AccessoryType
import com.gilbersoncampos.relicregistry.data.enums.BodyPosition
import com.gilbersoncampos.relicregistry.data.enums.Condition
import com.gilbersoncampos.relicregistry.data.enums.DecorationLocation
import com.gilbersoncampos.relicregistry.data.enums.DecorationType
import com.gilbersoncampos.relicregistry.data.enums.FilterEnum
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

fun Enum<*>.getNameTranslated(): String {
    return when (this) {
        is PaintColor -> when (this) {
            PaintColor.BLACK -> "Preto"
            PaintColor.WHITE -> "Branco"
            PaintColor.CREAM_BEIGE -> "Creme/Bege"
            PaintColor.OTHER -> "Outra"
            PaintColor.UNIDENTIFIED -> "Não identificado"
            PaintColor.RED -> "Vermelho"
        }

        is StatueType -> when (this) {
            StatueType.ANTHROPOMORPHIC -> "Antropomorfo"
            StatueType.ZOOMORPHIC -> "Zoomorfo"
            StatueType.ANTHROPOZOOMORPHIC -> "Antropozoomorfo"
            StatueType.HEAD -> "Cabeça de estatueta"
            StatueType.BODY -> "Corpo de estatueta"
            StatueType.OTHER -> "Não identificado"
        }

        is Condition -> when (this) {
            Condition.INTACT -> "Inteiro"
            Condition.FRAGMENTED -> "Fragmentado"
            Condition.RECONSTITUTED -> "Reconstituido"
        }

        is GeneralBodyShape -> when (this) {
//            GeneralBodyShape.SOLID -> "Sólida"
//            GeneralBodyShape.HOLLOW -> "Oca"
            GeneralBodyShape.NATURALISTIC -> "Naturalista"
            GeneralBodyShape.STYLIZED -> "Estilizada"
            GeneralBodyShape.CYLINDRICAL -> "Cilíndrica"
            GeneralBodyShape.GLOBULAR -> "Globular"
            GeneralBodyShape.TRAPEZOIDAL -> "Trapezoidal"
            GeneralBodyShape.PHALLIC -> "Fálica"
            GeneralBodyShape.UNIDENTIFIED -> "Não Identificado"
        }
        is InteriorCondition -> when (this) {
            InteriorCondition.SOLID->"Maciça"
            InteriorCondition.HOLLOW->"Oca"

        }

        is UpperLimbs -> when (this) {
            UpperLimbs.HEAD -> "Cabeça"
            UpperLimbs.EYES -> "Olhos"
            UpperLimbs.NOSE -> "Nariz"
            UpperLimbs.EARS -> "Orelhas"
            UpperLimbs.MOUTH -> "Boca"
            UpperLimbs.NECK -> "Pescoço"
            UpperLimbs.SHOULDERS -> "Ombros"
            UpperLimbs.CHEST -> "Tórax"
            UpperLimbs.BREASTS -> "Seios"
            UpperLimbs.ARMS -> "Braços"
            UpperLimbs.HANDS -> "Mãos"
            UpperLimbs.FINGERS ->  "Dedos"
            UpperLimbs.NAVEL -> "Umbigo"
        }

        is LowerLimbs -> when (this) {
            LowerLimbs.BUTTOCKS -> "Nadegas"
            LowerLimbs.ANUS ->  "Ânus"
            LowerLimbs.LEGS -> "Pernas"
            LowerLimbs.FEET -> "Pés"
            LowerLimbs.TOES -> "Dedos"
        }

        is Genitalia -> when (this) {
            Genitalia.MALE -> "Pênis"
            Genitalia.FEMALE -> "Vulva"
        }

        is Firing -> when (this) {
            Firing.OXIDIZING -> "Oxidante"
            Firing.REDUCING -> "Redutora"
            Firing.REDUCED_CORE ->"Núcleo Redutor"
            Firing.INTERNAL_OXIDIZING_EXTERNAL_REDUCING -> "Oxidação interna/externa"
            Firing.INTERNAL_REDUCING_EXTERNAL_OXIDIZING -> "Redução interna/externa"
            Firing.UNIDENTIFIED -> "Não Identificada"
        }

        is Temper -> when (this) {
            Temper.MINERAL -> "Mineral"
            Temper.QUARTZ -> "Quartzo"
            Temper.MICA ->  "Mica"
            Temper.IRON_OXIDE -> "Óxido de ferro"
            Temper.CHARCOAL ->"Carvão"
            Temper.GROG -> "Caco moído"
            Temper.CARAIPE -> "Caraipé"
            Temper.SHELL ->  "Concha"
            Temper.CAUIXI -> "Cauixi"
        }

        is ManufacturingTechnique -> when (this) {
            ManufacturingTechnique.COIL_BUILDING -> "Acordelado ou roletado"
            ManufacturingTechnique.MODELING -> "Modelado"
            ManufacturingTechnique.SLAB_BUILDING -> "Placas"
            ManufacturingTechnique.PIT_FINGER -> "Moldado"
            ManufacturingTechnique.MOLDING -> "Pint-Finger"
        }

        is ManufacturingMarks -> when (this) {
            ManufacturingMarks.IMPRESSIONS -> "Impressão"
            ManufacturingMarks.FIRING_MARKS -> "Marca de queima"
        }

        is UsageMarks -> when (this) {
            UsageMarks.CARBON_CRUST -> "Crosta carbônica"
            UsageMarks.SOOT -> "Fuligem"
            UsageMarks.RESIN -> "Resina"
            UsageMarks.SCRATCHES -> "Ranhuras"
            UsageMarks.GEMMULES -> "Gêmulas"
            UsageMarks.NONE -> "Não possui"
            UsageMarks.OTHER -> "Outra"
        }

        is SurfaceTreatment -> when (this) {
            SurfaceTreatment.SMOOTHED -> "Alisado"
            SurfaceTreatment.POLISHED -> "Polimento"
            SurfaceTreatment.BURNISHED ->  "Brunidura"
            SurfaceTreatment.RESIN -> "Resina"
            SurfaceTreatment.BRUSHED -> "Escovado"
            SurfaceTreatment.SLIP -> "Engobo"
            SurfaceTreatment.BARBOTINE -> "Barbotina"
            SurfaceTreatment.NONE -> "Não possui"
        }

        is DecorationLocation -> when (this) {
            DecorationLocation.EXTERNAL -> "Externa"
            DecorationLocation.INTERNAL ->  "Interna"
            DecorationLocation.BOTH ->  "Ambos"
        }

        is DecorationType -> when (this) {
            DecorationType.PAINTED -> "Pintura"
            DecorationType.PLASTIC ->  "Plástica"
            // "Grafismo"
        }

        is PlasticDecoration -> when (this) {
            PlasticDecoration.INCISION -> "Incisão"
            PlasticDecoration.EXCISION -> "Excisão"
            PlasticDecoration.EYE_APPLIQUE ->"Aplique de olhos"
            PlasticDecoration.MAMIFORM_APPLIQUE -> "Aplique mamiforme"
            PlasticDecoration.PUNCTATION -> "Ponteado"
            PlasticDecoration.FINGERPRINT -> "Digitado"
            PlasticDecoration.PERFORATED_HOLE ->  "Orifício vazado"
            PlasticDecoration.NON_PERFORATED_HOLE -> "Orifício não vazado"
            PlasticDecoration.HANDLE -> "Alça"
            PlasticDecoration.APPLIQUE -> "Aplique"
        }

        is AccessoryType -> when (this) {
            AccessoryType.STRETCHED_LOBES ->"Lóbulos alargados"
            AccessoryType.THONGS -> "Tangas"
            AccessoryType.CLOTHING ->  "Vestimentas"
            AccessoryType.ORNAMENTS -> "Adornos"
            AccessoryType.DIADEMS -> "Diademas"
            AccessoryType.HAIRSTYLE -> "Penteado"
            AccessoryType.ORIFICES -> "Orifícios"
            AccessoryType.MASK -> "Máscara"
            AccessoryType.EMBLEMS -> "Emblemas"
        }

        is BodyPosition -> when (this) {
            BodyPosition.STANDING_FRONTAL -> "Em pé (Frontal)"
            BodyPosition.SQUATTING -> "Acocorado"
            BodyPosition.STANDING_PROFILE -> "Em pé (Perfil)"
            BodyPosition.DUAL_PERSPECTIVE -> "Perspectiva dual"
            BodyPosition.ARMS_AWAY_FROM_THE_BODY -> "Braços afastados do corpo"
            BodyPosition.ARMS_CLOSE_TO_THE_BODY -> "Braços colados ao corpo"
            BodyPosition.SEATED_FRONTAL -> "Sentado (Frontal)"
            BodyPosition.SEATED_PROFILE ->"Sentado (Perfil)"
        }

        is Uses -> when (this) {
            Uses.RATTLE -> "Chocalho"
            Uses.CONTAINER -> "Recipiente"
            Uses.PIPE -> "Cachimbo"
            Uses.MUSICAL_INSTRUMENT -> "Instrumento musical"
            Uses.WEAPON -> "Arma"
            Uses.OTHER -> "Outra"
            Uses.NOT_IDENTIFIED -> "Não identificada"
            Uses.TOYS -> "Brinquedos"
        }
        is FilterEnum-> this.translatedName

        else -> "Unknown name"
    }
}
