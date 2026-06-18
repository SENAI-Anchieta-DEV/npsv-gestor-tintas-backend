package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum ResultadoRN01 {
    @Schema(description = "Peso estável e dentro da margem de 5% do peso alvo")
    APROVADO,

    @Schema(description = "Peso fora da margem de 5% do peso alvo")
    FORA_DA_MARGEM,

    @Schema(description = "Peso ainda não estável, aguardando estabilidade para avaliação")
    AGUARDANDO_ESTABILIDADE
}
