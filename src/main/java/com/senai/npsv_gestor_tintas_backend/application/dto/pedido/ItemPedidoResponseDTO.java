package com.senai.npsv_gestor_tintas_backend.application.dto.pedido;

import com.senai.npsv_gestor_tintas_backend.application.dto.produto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        @Schema(description = "Identificador único do item do pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Produto que está sendo pedido")
        ProdutoResponseDTO produto,

        @Schema(description = "Quantidade do produto que está sendo pedido", example = "10")
        BigDecimal quantidade,

        @Schema(description = "Preço unitário do produto no momento do pedido", example = "99.99")
        BigDecimal precoUnitario,

        @Schema(description = "Valor total do item (quantidade x preço unitário)", example = "999.90")
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
