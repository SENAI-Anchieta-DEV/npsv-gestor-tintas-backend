package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record ConcluirVendaRequestDTO(
        @NotBlank(message = "A forma de pagamento é obrigatória.")
        String formaPagamento,

        // Opcional: Pode ser nulo dependendo da forma de pagamento (ex: Cartão não tem "troco")
        BigDecimal valorRecebido
) {}