package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import java.time.LocalDateTime;

public record FormulaResponseDTO(
        String id,
        String codigoInterno,
        String nomeCor,
        LocalDateTime dataCriacao
) {
    public static FormulaResponseDTO fromEntity(Formula formula) {
        if (formula == null) return null;
        return new FormulaResponseDTO(
                formula.getId(),
                formula.getCodigoInterno(),
                formula.getNomeCor(),
                formula.getDataCriacao()
        );
    }
}