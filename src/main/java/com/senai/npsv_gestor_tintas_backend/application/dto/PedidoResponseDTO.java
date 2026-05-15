package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        String id,
        FornecedorResponseDTO fornecedor,
        UsuarioResponseDTO admin,
        StatusPedido status,
        LocalDateTime dataPedido,
        LocalDate dataPrevisaoEntrega,
        String observacao,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal valorTotal
) {
    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensDto = pedido.getItens().stream()
                .map(ItemPedidoResponseDTO::fromEntity)
                .toList();

        BigDecimal total = itensDto.stream()
                .map(ItemPedidoResponseDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PedidoResponseDTO(
                pedido.getId(),
                FornecedorResponseDTO.fromEntity(pedido.getFornecedor()),
                UsuarioResponseDTO.fromEntity(pedido.getAdmin()),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getDataPrevisaoEntrega(),
                pedido.getObservacao(),
                itensDto,
                total
        );
    }
}
