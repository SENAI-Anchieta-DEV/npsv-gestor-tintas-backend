package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import java.time.LocalDateTime;

public record ProducaoResponseDTO(
        String id,
        LocalDateTime dataHora,
        StatusProducao status,
        UsuarioResponseDTO colorista,
        FormulaResponseDTO formula
) {
    public static ProducaoResponseDTO fromEntity(Producao producao) {
        if (producao == null) return null;
        return new ProducaoResponseDTO(
                producao.getId(),
                producao.getDataHora(),
                producao.getStatus(),
                UsuarioResponseDTO.fromEntity(producao.getColorista()),
                FormulaResponseDTO.fromEntity(producao.getFormula())
        );
    }
}