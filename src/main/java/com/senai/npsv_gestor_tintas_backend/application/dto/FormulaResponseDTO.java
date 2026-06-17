package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record FormulaResponseDTO(
        @Schema(description = "Identificador único da fórmula", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Código interno único da fórmula", example = "FML-001")
        String codigoInterno,

        @Schema(description = "Nome da cor resultante da fórmula", example = "Azul Celeste")
        String nomeCor,

        @Schema(description = "Data e hora de criação da fórmula", example = "2026-01-01T12:00:00")
        LocalDateTime dataCriacao,

        @Schema(description = "Data e hora da última alteração feita na fórmula", example = "2026-01-02T15:30:00")
        LocalDateTime dataAtualizacao,

        @Schema(description = "Lista de itens que compõem a fórmula")
        List<ItemFormulaResponseDTO> itens
) {
    public static FormulaResponseDTO fromEntity(Formula formula) {
        if (formula == null) return null;

        List<ItemFormulaResponseDTO> itensDto = formula.getItens().stream()
                .map(ItemFormulaResponseDTO::fromEntity)
                .toList();

        return new FormulaResponseDTO(
                formula.getId(),
                formula.getCodigoInterno(),
                formula.getNomeCor(),
                formula.getDataCriacao(),
                formula.getDataAtualizacao(),
                itensDto
        );
    }
}