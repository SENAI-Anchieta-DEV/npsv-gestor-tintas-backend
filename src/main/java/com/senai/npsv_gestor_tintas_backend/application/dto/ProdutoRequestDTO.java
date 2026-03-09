package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O código de barras é obrigatório.")
        String codigoBarras,

        @NotBlank(message = "A descrição do produto é obrigatória.")
        String descricao,

        @NotNull(message = "A quantidade inicial em estoque é obrigatória.")
        BigDecimal quantidadeEstoque,

        @NotNull(message = "O preço de custo é obrigatório.")
        BigDecimal precoCusto,

        @NotNull(message = "O preço de venda é obrigatório.")
        BigDecimal precoVenda,

        @NotNull(message = "A unidade de medida é obrigatória.")
        UnidadeMedida unidadeMedida,

        @NotBlank(message = "O ID da categoria é obrigatório.")
        String categoriaId
) {
    public Produto toEntity() {
        return Produto.builder()
                .codigoBarras(this.codigoBarras)
                .descricao(this.descricao)
                .quantidadeEstoque(this.quantidadeEstoque)
                .precoCusto(this.precoCusto)
                .precoVenda(this.precoVenda)
                .unidadeMedida(this.unidadeMedida)
                .categoria(CategoriaProduto.builder().id(this.categoriaId).build())
                .build();
    }
}
