package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ItemVendaRequestDTO(
        @NotBlank(message = "O ID do produto é obrigatório.")
        String produtoId,

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