package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
        String id,
        LocalDateTime dataAbertura,
        BigDecimal valorTotal,
        String status,
        String nomeVendedor,
        List<ItemVendaResponseDTO> itens
) {

    public static VendaResponseDTO fromEntity(Venda venda) {
        List<ItemVendaResponseDTO> itensDto = venda.getItens().stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();

        return new VendaResponseDTO(
                venda.getId(),
                venda.getDataAbertura(),
                venda.getValorTotal(),
                venda.getStatus().name(),
                venda.getVendedor() != null ? venda.getVendedor().getNome() : "Desconhecido",
                itensDto
        );
    }
}