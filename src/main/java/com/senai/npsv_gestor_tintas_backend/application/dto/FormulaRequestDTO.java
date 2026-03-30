package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public record FormulaRequestDTO(
        @NotBlank(message = "O código interno da fórmula é obrigatório.")
        String codigoInterno,

        @NotBlank(message = "O nome da cor é obrigatório.")
        String nomeCor,

        @NotEmpty(message = "A fórmula precisa ter pelo menos um insumo.")
        List<@Valid ItemFormulaRequestDTO> itens
) {
    public Formula toEntity() {
        return Formula.builder()
                .codigoInterno(this.codigoInterno)
                .nomeCor(this.nomeCor)
                .itens(new ArrayList<>())
                .build();
    }
}