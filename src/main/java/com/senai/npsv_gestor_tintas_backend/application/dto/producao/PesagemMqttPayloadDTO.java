package com.senai.npsv_gestor_tintas_backend.application.dto.producao;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record PesagemMqttPayloadDTO(
        @Schema(description = "Identificador da produção correspondente ao evento de pesagem", example = "123e4567-e89b-12d3-a456-426614174000")
        String producaoId,

        @Schema(description = "Peso lido pela balança durante o evento de pesagem", example = "150.75")
        BigDecimal pesoLido,

        @Schema(description = "Unidade de medida utilizada para o peso lido", example = "ML")
        String unidadeMedida,

        @Schema(description = "Data e hora do evento de pesagem", example = "2026-05-29T14:30:00Z")
        String timestamp,

        @Schema(description = "Indica se a pesagem está estável ou não", example = "true")
        boolean estavel
) {
}
