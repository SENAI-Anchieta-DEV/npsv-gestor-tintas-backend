package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import java.time.LocalDateTime;
import java.util.List;

public record FormulaResponseDTO(
        String id,
        String codigoInterno,
        String nomeCor,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        List<ItemFormulaResponseDTO> itens
) {
    public static FormulaResponseDTO fromEntity(Formula formula) {
        if (formula == null) return null;

        List<ItemFormulaResponseDTO> itensDto = formula.getItens().stream()
                .map(ItemFormulaResponseDTO::fromEntity)
                .toList();

        return new FormulaResponseDTO(
                formula.getId(),
                formula.getCodigoInterno(),
                formula.getNomeCor(),
                formula.getDataCriacao(),
                formula.getDataAtualizacao(),
                itensDto
        );
    }
}