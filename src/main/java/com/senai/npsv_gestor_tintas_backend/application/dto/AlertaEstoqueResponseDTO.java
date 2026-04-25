package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;

import java.math.BigDecimal;

public record AlertaEstoqueResponseDTO(
        String id,
        String descricao,
        BigDecimal quantidadeEstoque,
        BigDecimal estoqueMinimo
) {
    public static AlertaEstoqueResponseDTO fromEntity(Produto produto) {
        return new AlertaEstoqueResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo()
        );
    }
}
