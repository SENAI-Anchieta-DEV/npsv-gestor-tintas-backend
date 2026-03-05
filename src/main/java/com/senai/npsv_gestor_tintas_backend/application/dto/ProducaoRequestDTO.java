package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import jakarta.validation.constraints.NotBlank;

public record ProducaoRequestDTO(
        @NotBlank(message = "O ID do Colorista responsável é obrigatório.")
        String coloristaId,

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