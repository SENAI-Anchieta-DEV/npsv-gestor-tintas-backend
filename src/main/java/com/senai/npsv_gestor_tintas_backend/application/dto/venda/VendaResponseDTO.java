package com.senai.npsv_gestor_tintas_backend.application.dto.venda;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record VendaResponseDTO(
        @Schema(description = "Identificador único da venda", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Data e hora em que a venda foi iniciada", example = "2024-06-01T14:30:00")
        LocalDateTime dataAbertura,

        @Schema(description = "Valor total da venda", example = "250.00")
        BigDecimal valorTotal,

        @Schema(description = "Status atual da venda", example = "ABERTA")
        StatusVenda status,

        @Schema(description = "Forma de pagamento utilizada na venda", example = "CARTAO_CREDITO")
        FormaPagamento formaPagamento,

        @Schema(description = "Nome do Vendedor responsável pela venda", example = "João Silva")
        String nomeVendedor,

        @Schema(description = "Nome do cliente associado à venda, se houver", example = "Maria Oliveira")
        String nomeCliente,

        @Schema(description = "Lista de itens que compõem a venda")
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
                venda.getStatus(),
                venda.getFormaPagamento(),
                venda.getVendedor() != null ? venda.getVendedor().getNome() : "Desconhecido",
                venda.getCliente() != null ? venda.getCliente().getNome() : null,
                itensDto
        );
    }
}