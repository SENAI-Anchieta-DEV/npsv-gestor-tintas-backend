package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ItemVendaResponseDTO(
        @Schema(description = "Identificador único do item da venda", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Identificador único do produto que está sendo vendido", example = "123e4567-e89b-12d3-a456-426614174000")
        String produtoId,

        @Schema(description = "Nome do produto que está sendo vendido", example = "Tinta Acrílica Premium")
        String nomeProduto,

        @Schema(description = "Quantidade do produto que está sendo vendido", example = "10")
        BigDecimal quantidade,

        @Schema(description = "Preço praticado do produto no momento da venda", example = "99.99")
        BigDecimal precoPraticado,

        @Schema(description = "Valor total do item (quantidade x preço praticado)", example = "999.90")
        BigDecimal subtotal
) {
    public static ItemVendaResponseDTO fromEntity(ItemVenda item) {
        return new ItemVendaResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getQuantidade(),
                item.getPrecoPraticado(),
                item.getPrecoPraticado().multiply(item.getQuantidade())
        );
    }
}