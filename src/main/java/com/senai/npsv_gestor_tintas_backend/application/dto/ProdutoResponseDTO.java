package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import java.math.BigDecimal;

public record ProdutoResponseDTO(
        String id,
        String codigoBarras,
        String descricao,
        BigDecimal quantidadeEstoque,
        BigDecimal precoCusto,
        BigDecimal precoVenda,
        UnidadeMedida unidadeMedida,
        CategoriaProdutoResponseDTO categoria
) {
    public static ProdutoResponseDTO fromEntity(Produto produto) {
        if (produto == null) return null;
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getCodigoBarras(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque(),
                produto.getPrecoCusto(),
                produto.getPrecoVenda(),
                produto.getUnidadeMedida(),
                CategoriaProdutoResponseDTO.fromEntity(produto.getCategoria())
        );
    }
}