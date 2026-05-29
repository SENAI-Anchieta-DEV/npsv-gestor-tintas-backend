package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ProducaoRequestDTO(
        @Schema(description = "Identificador do Colorista responsável pela produção", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID do Colorista responsável é obrigatório.")
        String coloristaId,

        @Schema(description = "Identificador da Fórmula que está sendo produzida", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID da Fórmula é obrigatório.")
        String formulaId
) {
    public Producao toEntity() {
        return Producao.builder()
                .colorista(Usuario.builder().id(this.coloristaId).build())
                .formula(Formula.builder().id(this.formulaId).build())
                .build();
    }
}