package com.senai.npsv_gestor_tintas_backend.application.dto.venda;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.ArrayList;

public record IniciarVendaRequestDTO(
        @Schema(description = "Identificador do cliente para quem a venda está sendo realizada", example = "123e4567-e89b-12d3-a456-426614174000")
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