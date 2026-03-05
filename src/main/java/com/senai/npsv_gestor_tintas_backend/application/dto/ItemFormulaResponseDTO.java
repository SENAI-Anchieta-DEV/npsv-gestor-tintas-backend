package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import java.math.BigDecimal;

public record ItemFormulaResponseDTO(
        String id,
        BigDecimal quantidadeNecessaria,
        Integer ordemAdicao,
        ProdutoResponseDTO insumo,
        String formulaId
) {
    public static ItemFormulaResponseDTO fromEntity(ItemFormula item) {
        if (item == null) return null;
        return new ItemFormulaResponseDTO(
                item.getId(),
                item.getQuantidadeNecessaria(),
                item.getOrdemAdicao(),
                ProdutoResponseDTO.fromEntity(item.getInsumo()),
                item.getFormula() != null ? item.getFormula().getId() : null
        );
    }
}