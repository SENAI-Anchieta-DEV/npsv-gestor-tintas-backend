package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O código de barras é obrigatório.")
        String codigoBarras,

        @NotBlank(message = "A descrição do produto é obrigatória.")
        String descricao,

        @NotNull(message = "A quantidade inicial em estoque é obrigatória.")
        @DecimalMin(value = "0.0", message = "A quantidade de estoque não pode ser negativa.")
        BigDecimal quantidadeEstoque,

        @NotNull(message = "O preço de custo é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço de custo deve ser maior que zero.")
        BigDecimal precoCusto,

        @NotNull(message = "O preço de venda é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço de venda deve ser maior que zero.")
        BigDecimal precoVenda,

        @NotNull(message = "A unidade de medida é obrigatória.")
        UnidadeMedida unidadeMedida,

        @NotBlank(message = "O ID da categoria é obrigatório.")
        String categoriaId,

        @NotNull(message = "O estoque mínimo é obrigatório.")
        @DecimalMin(value = "0.0", message = "O estoque mínimo não pode ser negativo.")
        BigDecimal estoqueMinimo
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
                .estoqueMinimo(this.estoqueMinimo)
                .estoqueEmAlerta(this.quantidadeEstoque.compareTo(this.estoqueMinimo) <= 0)
                .build();
    }
}
