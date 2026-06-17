package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ConcluirVendaRequestDTO(
        @Schema(description = "Identificador único do cliente associado à venda", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "A forma de pagamento é obrigatória.")
        FormaPagamento formaPagamento,

        @Schema(description = "Lista de itens que compõem a venda")
        @NotEmpty(message = "A venda deve conter pelo menos um item.")
        List<@NotNull @Valid ItemVendaRequestDTO> itens,

        @Schema(description = "Identificador único do cliente associado à venda", example = "123e4567-e89b-12d3-a456-426614174000")
        String clienteId
) {}