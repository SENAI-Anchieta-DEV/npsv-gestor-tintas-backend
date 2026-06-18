package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum UnidadeMedida {

    @Schema(description = "Unidade de medida em gramas")
    G,

    @Schema(description = "Unidade de medida em quilogramas")
    KG,

    @Schema(description = "Unidade de medida em mililitros")
    ML,

    @Schema(description = "Unidade de medida em litros")
    L,

    @Schema(description = "Unidade de medida em unidades")
    UN
}
