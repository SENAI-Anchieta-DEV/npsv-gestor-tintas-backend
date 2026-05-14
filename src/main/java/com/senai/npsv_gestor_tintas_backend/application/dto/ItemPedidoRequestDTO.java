package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemPedidoRequestDTO(
        @NotBlank(message = "O ID do produto é obrigatório.")
        String produtoId,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        @NotNull(message = "O preço unitário é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço unitário deve ser maior que zero.")
        BigDecimal precoUnitario
) {
}
