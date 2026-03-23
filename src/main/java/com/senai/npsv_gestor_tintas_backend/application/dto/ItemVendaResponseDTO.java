package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import java.math.BigDecimal;

public record ItemVendaResponseDTO(
        String id,
        String produtoId,
        String nomeProduto,
        BigDecimal quantidade,
        BigDecimal precoPraticado,
        BigDecimal subtotal,
        String vendaId
) {
    public static ItemVendaResponseDTO fromEntity(ItemVenda item) {
        return new ItemVendaResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getDescricao(),
                item.getQuantidade(),
                item.getPrecoPraticado(),
                item.getPrecoPraticado().multiply(item.getQuantidade()), // Subtotal calculado
                item.getVenda().getId()
        );
    }
}