package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        @Schema(description = "Identificador único do pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Fornecedor associado ao pedido")
        FornecedorResponseDTO fornecedor,

        @Schema(description = "Usuário administrador que criou o pedido")
        UsuarioResponseDTO admin,

        @Schema(description = "Status atual do pedido", example = "PENDENTE")
        StatusPedido status,

        @Schema(description = "Data e hora em que o pedido foi criado", example = "2026-05-30T14:30:00")
        LocalDateTime dataPedido,

        @Schema(description = "Data prevista para entrega do pedido", example = "2026-06-05")
        LocalDate dataPrevisaoEntrega,

        @Schema(description = "Nota de observação relacionada ao pedido")
        String observacao,

        @Schema(description = "Lista de itens que compõem o pedido")
        List<ItemPedidoResponseDTO> itens,

        @Schema(description = "Valor total do pedido", example = "1500.00")
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
