package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotBlank;

public record IniciarVendaRequestDTO(
        @NotBlank(message = "O ID do vendedor é obrigatório.")
        String vendedorId
) {}