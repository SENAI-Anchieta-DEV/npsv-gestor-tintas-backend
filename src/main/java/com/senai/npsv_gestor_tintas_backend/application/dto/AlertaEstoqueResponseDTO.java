package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record AlertaEstoqueResponseDTO(
        @Schema(description = "Identificador único do produto", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Descrição detalhada do produto", example = "Tinta Acrílica Fosca Branca 18L")
        String descricao,

        @Schema(description = "Quantidade atual do produto em estoque", example = "5.0")
        BigDecimal quantidadeEstoque,

        @Schema(description = "Nível de estoque mínimo para o produto, que aciona alertas quando atingido", example = "10.0")
        BigDecimal estoqueMinimo
) {
    public static AlertaEstoqueResponseDTO fromEntity(Produto produto) {
        if (produto == null) return null;
        return new AlertaEstoqueResponseDTO(
                produto.getId(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque(),
                produto.getEstoqueMinimo()
        );
    }
}
