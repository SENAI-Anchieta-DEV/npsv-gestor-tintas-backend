package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProdutoResponseDTO(

        @Schema(description = "Identificador único do produto", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Código de barras do produto", example = "7891234567890")
        String codigoBarras,

        @Schema(description = "Descrição detalhada do produto", example = "Tinta Acrílica Fosca Branca 18L")
        String descricao,

        @Schema(description = "Quantidade atual do produto em estoque", example = "50.0")
        BigDecimal quantidadeEstoque,

        @Schema(description = "Preço de custo do produto", example = "120.50")
        BigDecimal precoCusto,

        @Schema(description = "Preço de venda do produto", example = "189.90")
        BigDecimal precoVenda,

        @Schema(description = "Unidade de medida do produto", example = "L")
        UnidadeMedida unidadeMedida,

        @Schema(description = "Categoria à qual o produto pertence")
        CategoriaProdutoResponseDTO categoria,

        @Schema(description = "Nível de estoque mínimo para o produto, que aciona alertas quando atingido", example = "10.0")
        BigDecimal estoqueMinimo,

        @Schema(description = "Indica se o produto atingiu o nível crítico de estoque", example = "false")
        boolean estoqueEmAlerta
) {
    public static ProdutoResponseDTO fromEntity(Produto produto) {
        if (produto == null) return null;
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getCodigoBarras(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque(),
                produto.getPrecoCusto(),
                produto.getPrecoVenda(),
                produto.getUnidadeMedida(),
                CategoriaProdutoResponseDTO.fromEntity(produto.getCategoria()),
                produto.getEstoqueMinimo(),
                produto.isEstoqueEmAlerta()
        );
    }
}