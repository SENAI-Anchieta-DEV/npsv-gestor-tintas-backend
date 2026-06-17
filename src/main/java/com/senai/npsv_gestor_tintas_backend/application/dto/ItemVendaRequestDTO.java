package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemVendaRequestDTO(
        @Schema(description = "Identificador do produto que está sendo vendido", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID do produto é obrigatório.")
        String produtoId,

        @Schema(description = "Quantidade do produto que está sendo vendida", example = "10")
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade
) {
        public ItemVenda toEntity() {
                return ItemVenda.builder()
                        .produto(Produto.builder().id(this.produtoId).build())
                        .quantidade(this.quantidade)
                        .build();
        }
}