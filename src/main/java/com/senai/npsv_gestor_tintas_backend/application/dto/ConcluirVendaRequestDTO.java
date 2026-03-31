package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ConcluirVendaRequestDTO(
        @NotBlank(message = "A forma de pagamento é obrigatória.")
        FormaPagamento formaPagamento,

        @NotNull(message = "A venda deve conter pelo menos um item.")
        List<@Valid ItemVendaRequestDTO> itens
) {}