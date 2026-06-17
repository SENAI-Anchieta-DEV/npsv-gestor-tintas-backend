package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AtualizarVendaRequestDTO(
        String clienteId,
        FormaPagamento formaPagamento,
        List<@NotNull @Valid ItemVendaRequestDTO> itens
) {}
