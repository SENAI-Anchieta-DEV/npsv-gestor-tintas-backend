package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

// Importante: Usar "record" (Java 17+) para funcionar os métodos .produtoId() e .quantidade() sem o "get"
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