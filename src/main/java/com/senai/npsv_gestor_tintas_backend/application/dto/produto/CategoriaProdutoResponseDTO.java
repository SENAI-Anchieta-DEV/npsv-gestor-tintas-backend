package com.senai.npsv_gestor_tintas_backend.application.dto.produto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoriaProdutoResponseDTO(
        @Schema(description = "Identificador único da categoria do produto", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Nome da categoria do produto", example = "Tintas para Parede")
        String nome,

        @Schema(description = "Descrição detalhada da categoria do produto", example = "Tintas de paredes internas e externas.")
        String descricao
) {
    public static CategoriaProdutoResponseDTO fromEntity(CategoriaProduto categoria) {
        if (categoria == null) return null;
        return new CategoriaProdutoResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}