package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        String id,
        ProdutoResponseDTO produto,
        BigDecimal quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {
    public static ItemPedidoResponseDTO fromEntity(ItemPedido item) {
        if (item == null) return null;
        return new ItemPedidoResponseDTO(
                item.getId(),
                ProdutoResponseDTO.fromEntity(item.getProduto()),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPrecoUnitario().multiply(item.getQuantidade())
        );
    }
}
