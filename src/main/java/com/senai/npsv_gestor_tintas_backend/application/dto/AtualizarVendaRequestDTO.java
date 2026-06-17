package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AtualizarVendaRequestDTO(

        @Schema(description = "ID do cliente associado à venda", example = "123e4567-e89b-12d3-a456-426614174000")
        String clienteId,

        @Schema(description = "Forma de pagamento utilizada na venda", example = "CARTAO_CREDITO")
        FormaPagamento formaPagamento,

        @Schema(description = "Lista de itens vendidos, cada um contendo o ID do produto e a quantidade", example = "[{\"produtoId\": \"123e4567-e89b-12d3-a456-426614174001\", \"quantidade\": 2}]")
        List<@NotNull @Valid ItemVendaRequestDTO> itens
) {}
