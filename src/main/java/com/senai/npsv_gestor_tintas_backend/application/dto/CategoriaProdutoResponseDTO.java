package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;

public record CategoriaProdutoResponseDTO(
        String id,
        String nome,
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