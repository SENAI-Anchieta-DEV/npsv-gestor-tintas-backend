package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resultado da validação da Regra de Negócio RN01.
 * APROVADO            → peso estável e dentro da margem de 5%
 * FORA_DA_MARGEM      → peso estável mas fora da margem de 5%
 * AGUARDANDO_ESTABILIDADE → flag estavel=false enviada pelo ESP32
 */
public enum ResultadoRN01 {
    @Schema(description = "Peso estável e dentro da margem de 5% do peso alvo")
    APROVADO,

    @Schema(description = "Peso fora da margem de 5% do peso alvo")
    FORA_DA_MARGEM,

    @Schema(description = "Peso ainda não estável, aguardando estabilidade para avaliação")
    AGUARDANDO_ESTABILIDADE
}
