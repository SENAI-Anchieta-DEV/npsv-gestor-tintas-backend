package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;

public record IniciarVendaRequestDTO(
        @NotBlank(message = "O ID do vendedor é obrigatório.")
        String vendedorId
) {
        public Venda toEntity() {
                return Venda.builder()
                        .vendedor(Usuario.builder().id(this.vendedorId).build())
                        .valorTotal(BigDecimal.ZERO)
                        .status(StatusVenda.ABERTA)
                        .itens(new ArrayList<>())
                        .build();
        }
}