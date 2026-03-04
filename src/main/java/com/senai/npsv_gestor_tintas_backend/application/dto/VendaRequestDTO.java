package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

// Importante: Usar "record" (Java 17+) para funcionar os métodos .produtoId() e .quantidade() sem o "get"
public record VendaRequestDTO(
        @NotNull(message = "O ID do vendedor é obrigatório.")
        String vendedorId,

        @NotEmpty(message = "A venda deve conter pelo menos um item.")
        List<ItemVendaRequest> itens // Nome da lista que o Service chama de dto.itens()
) {
    // A classe interna DEVE se chamar "ItemVendaRequest" (igual está no seu Service)
    public record ItemVendaRequest(
            @NotNull(message = "O ID do produto é obrigatório.")
            String produtoId,

            @NotNull(message = "A quantidade é obrigatória.")
            BigDecimal quantidade
    ) {}
}