package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
        String id,
        LocalDateTime dataHora,
        BigDecimal valorTotal,
        String nomeVendedor,
        List<ItemVendaResponse> itens
) {
    public record ItemVendaResponse(
            String nomeProduto,
            BigDecimal quantidade,
            BigDecimal precoPraticado,
            BigDecimal subtotal
    ) {}

    public static VendaResponseDTO fromEntity(Venda venda) {
        List<ItemVendaResponse> itensDto = venda.getItens().stream()
                .map(item -> new ItemVendaResponse(
                        item.getProduto().getDescricao(),
                        item.getQuantidade(),
                        item.getPrecoPraticado(),
                        item.getPrecoPraticado().multiply(item.getQuantidade())
                )).toList();

        return new VendaResponseDTO(
                venda.getId(),
                venda.getDataHora(),
                venda.getValorTotal(),
                venda.getVendedor() != null ? venda.getVendedor().getNome() : "Desconhecido",
                itensDto
        );
    }
}