package com.gilbersoncampos.relicregistry.data.enums

import kotlin.reflect.KClass

// Enum classes to capture the fixed options
enum class FilterEnum(val translatedName: String) {
    SURFACE_TREATMENT_INTERNAL("Tratamento de Sup. (I.T.)"),
    SURFACE_TREATMENT_EXTERNAL("Tratamento de Sup. (E.T.)"),
    PAINT_COLOR_EXTERNAL("Cor da pintura (F.E)"),
    PAINT_COLOR_INTERNAL("Cor da pintura (F.I)"),
    STATUE_TYPE("Artefato"),
    CONDITION("Condição"),
    GENERAL_BODY_SHAPE("Forma Geral do Corpo"),
    UPPER_LIMBS("Membros Superiores"),
    LOWER_LIMBS("Membros Inferiores"),
    GENITALIA("Fabricação de Genitália"),
    FIRING("Queima"),
    TEMPER("Antiplástico"),
    MANUFACTURING_TECHNIQUE("Técnica de fabricação"),
    MANUFACTURING_MARKS("Marcas de fabricação"),
    USAGE_MARKS("Marcas de Uso"),
    SURFACE_TREATMENT("Tratamento de Superfície"),
    DECORATION_LOCATION("Decoração"),
    DECORATION_TYPE("Tipo de decoração"),
    PAINT_COLOR("Cor da pintura"),
    PLASTIC_DECORATION("Decoração plástica"),
    ACCESSORY_TYPE("Outros atributos formais"),
    BODY_POSITION("Posição corporal"),
    USES("Usos")
}


// Mapa com as traduções das classes enum

enum class SurfaceTreatmentInternal

enum class SurfaceTreatmentExternal

enum class PaintColorExternal

enum class PaintColorInternal

enum class StatueType {
    ANTHROPOMORPHIC, ZOOMORPHIC, ANTHROPOZOOMORPHIC, HEAD, BODY, OTHER;
}

enum class Condition {
    INTACT, FRAGMENTED, RECONSTITUTED;
}

enum class GeneralBodyShape {
     NATURALISTIC, STYLIZED, CYLINDRICAL, GLOBULAR, TRAPEZOIDAL, PHALLIC, UNIDENTIFIED;//SOLID, HOLLOW,
}
enum class InteriorCondition {
    SOLID, HOLLOW,
}

enum class UpperLimbs {
    HEAD, EYES, NOSE, EARS, MOUTH, NECK, SHOULDERS, CHEST, BREASTS, ARMS, HANDS, FINGERS, NAVEL, ;
}

enum class LowerLimbs {
    BUTTOCKS, ANUS, LEGS, FEET, TOES
}

enum class Genitalia {
    MALE, FEMALE
}

enum class Firing {
    OXIDIZING, REDUCING, REDUCED_CORE, INTERNAL_OXIDIZING_EXTERNAL_REDUCING, INTERNAL_REDUCING_EXTERNAL_OXIDIZING, UNIDENTIFIED;
}

enum class Temper {
    MINERAL, QUARTZ, MICA, IRON_OXIDE, CHARCOAL, GROG, CARAIPE, SHELL, CAUIXI;

}

enum class ManufacturingTechnique {
    COIL_BUILDING, MODELING, SLAB_BUILDING, PIT_FINGER, MOLDING;

}

enum class ManufacturingMarks {
    IMPRESSIONS, FIRING_MARKS
}

enum class UsageMarks {
    CARBON_CRUST, SOOT, RESIN, SCRATCHES, GEMMULES, NONE, OTHER;
}

enum class SurfaceTreatment {
    SMOOTHED, POLISHED, BURNISHED, RESIN, BRUSHED, SLIP, BARBOTINE, NONE;
}

enum class DecorationLocation {
    EXTERNAL, INTERNAL, BOTH;
}

enum class DecorationType {
    PAINTED, PLASTIC;
}

enum class PaintColor {
    WHITE, BLACK, CREAM_BEIGE,RED, OTHER, UNIDENTIFIED;
}

enum class PlasticDecoration {
    INCISION, EXCISION, EYE_APPLIQUE, MAMIFORM_APPLIQUE, PUNCTATION, FINGERPRINT, PERFORATED_HOLE, NON_PERFORATED_HOLE, HANDLE, APPLIQUE;
}

enum class AccessoryType {
    STRETCHED_LOBES, THONGS, CLOTHING, ORNAMENTS, DIADEMS, HAIRSTYLE, MASK, EMBLEMS, ORIFICES;

}

enum class BodyPosition {
    STANDING_FRONTAL, STANDING_PROFILE, SEATED_FRONTAL,SEATED_PROFILE,SQUATTING, DUAL_PERSPECTIVE, ARMS_AWAY_FROM_THE_BODY, ARMS_CLOSE_TO_THE_BODY;

}

enum class Uses {
    RATTLE, CONTAINER, PIPE, MUSICAL_INSTRUMENT, WEAPON, TOYS, OTHER, NOT_IDENTIFIED;

}