package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConcluirVendaRequestDTO(
        @NotBlank(message = "A forma de pagamento é obrigatória.")
        String formaPagamento,

        @NotEmpty(message = "A venda deve conter pelo menos um item.")
        List<@Valid VendaItemRequestDTO> itens
) {}