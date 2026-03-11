package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import jakarta.validation.constraints.NotBlank;

public record FormulaRequestDTO(
        @NotBlank(message = "O código interno da fórmula é obrigatório.")
        String codigoInterno,

        @NotBlank(message = "O nome da cor é obrigatório.")
        String nomeCor
) {
    public Formula toEntity() {
        return Formula.builder()
                .codigoInterno(this.codigoInterno)
                .nomeCor(this.nomeCor)
                .build();
    }
}