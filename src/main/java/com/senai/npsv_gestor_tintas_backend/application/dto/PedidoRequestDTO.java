package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PedidoRequestDTO(
        @NotBlank(message = "O ID do fornecedor é obrigatório.")
        String fornecedorId,

        LocalDate dataPrevisaoEntrega,

        String observacao,

        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        List<@NotNull @Valid ItemPedidoRequestDTO> itens
) {
}
