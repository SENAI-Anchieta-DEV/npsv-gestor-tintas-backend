package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProducaoResponseDTO(
        @Schema(description = "Identificador único da produção", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Data e hora da produção", example = "2024-06-01T08:30:00")
        LocalDateTime dataHora,

        @Schema(description = "Status atual da produção", example = "EM_ANDAMENTO")
        StatusProducao status,

        @Schema(description = "Identificador do Colorista responsável pela produção", example = "123e4567-e89b-12d3-a456-426614174000")
        UsuarioResponseDTO colorista,

        @Schema(description = "Identificador da Fórmula utilizada na produção", example = "123e4567-e89b-12d3-a456-426614174000")
        FormulaResponseDTO formula
) {
    public static ProducaoResponseDTO fromEntity(Producao producao) {
        if (producao == null) return null;
        return new ProducaoResponseDTO(
                producao.getId(),
                producao.getDataHora(),
                producao.getStatus(),
                UsuarioResponseDTO.fromEntity(producao.getColorista()),
                FormulaResponseDTO.fromEntity(producao.getFormula())
        );
    }
}