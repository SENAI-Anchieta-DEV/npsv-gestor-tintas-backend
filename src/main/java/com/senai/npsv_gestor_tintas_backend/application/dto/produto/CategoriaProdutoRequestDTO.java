package com.senai.npsv_gestor_tintas_backend.application.dto.produto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CategoriaProdutoRequestDTO(
        @Schema(description = "Nome da categoria do produto", example = "Tintas para Parede")
        @NotBlank(message = "O nome da categoria é obrigatório.")
        String nome,

        @Schema(description = "Descrição detalhada da categoria do produto", example = "Tintas de paredes internas e externas.")
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
