package com.senai.npsv_gestor_tintas_backend.application.dto.formula;

import com.senai.npsv_gestor_tintas_backend.application.dto.produto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ItemFormulaResponseDTO(
        @Schema(description = "Identificador único do item da fórmula", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Quantidade necessária do insumo para a fórmula", example = "34.5")
        BigDecimal quantidadeNecessaria,

        @Schema(description = "Ordem de adição do insumo na fórmula, onde 1 é o primeiro a ser adicionado", example = "1")
        Integer ordemAdicao,

        @Schema(description = "Insumo utilizado na fórmula")
        ProdutoResponseDTO insumo
) {
    public static ItemFormulaResponseDTO fromEntity(ItemFormula item) {
        if (item == null) return null;
        return new ItemFormulaResponseDTO(
                item.getId(),
                item.getQuantidadeNecessaria(),
                item.getOrdemAdicao(),
                ProdutoResponseDTO.fromEntity(item.getInsumo())
        );
    }
}