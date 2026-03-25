package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record VendaRequestDTO(
        @NotNull(message = "O ID do vendedor é obrigatório.")
        String vendedorId


) {
    public record ItemVendaRequest(
            @NotNull(message = "O ID do produto é obrigatório.")
            String produtoId,

            @NotNull(message = "A quantidade é obrigatória.")
            BigDecimal quantidade
    ) {}
}