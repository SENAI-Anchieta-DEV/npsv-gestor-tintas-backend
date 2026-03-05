package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import jakarta.validation.constraints.NotBlank;

public record CategoriaProdutoRequestDTO(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        String nome,

        @NotBlank(message = "A descrição da categoria é obrigatória.")
        String descricao
) {
    public CategoriaProduto toEntity() {
        return CategoriaProduto.builder()
                .nome(this.nome)
                .descricao(this.descricao)
                .build();
    }
}