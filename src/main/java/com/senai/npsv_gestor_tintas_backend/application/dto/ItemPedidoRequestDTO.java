package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemPedido;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemPedidoRequestDTO(
        @Schema(description = "Identificador do produto que está sendo pedido", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID do produto é obrigatório.")
        String produtoId,

        @Schema(description = "Quantidade do produto que está sendo pedido", example = "10")
        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser maior que zero.")
        BigDecimal quantidade,

        @Schema(description = "Preço unitário do produto no momento do pedido", example = "99.99")
        @NotNull(message = "O preço unitário é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço unitário deve ser maior que zero.")
        BigDecimal precoUnitario
) {
        public ItemPedido toEntity(Pedido pedido, Produto produto) {
                return ItemPedido.builder()
                        .pedido(pedido)
                        .produto(produto)
                        .quantidade(this.quantidade)
                        .precoUnitario(this.precoUnitario)
                        .build();
        }
}
