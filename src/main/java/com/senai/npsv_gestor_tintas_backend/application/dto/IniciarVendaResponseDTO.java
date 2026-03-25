package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IniciarVendaResponseDTO(
        String id,
        LocalDateTime dataHora,
        BigDecimal valorTotal,
        String status,
        String vendedorNome
) {
    public static IniciarVendaResponseDTO fromEntity(Venda venda) {
        return new IniciarVendaResponseDTO(
                venda.getId(),
                venda.getDataHora(),
                venda.getValorTotal(),
                venda.getStatus().name(),
                venda.getVendedor() != null ? venda.getVendedor().getNome() : "Desconhecido"
        );
    }
}