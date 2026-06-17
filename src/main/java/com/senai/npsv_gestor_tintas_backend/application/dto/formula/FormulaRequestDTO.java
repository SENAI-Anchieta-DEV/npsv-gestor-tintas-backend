package com.senai.npsv_gestor_tintas_backend.application.dto.formula;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public record FormulaRequestDTO(
        @Schema(description = "Código interno único da fórmula", example = "FML-001")
        @NotBlank(message = "O código interno da fórmula é obrigatório.")
        String codigoInterno,

        @Schema(description = "Nome da cor resultante da fórmula", example = "Azul Celeste")
        @NotBlank(message = "O nome da cor é obrigatório.")
        String nomeCor,

        @Schema(description = "Lista de itens que compõem a fórmula")
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