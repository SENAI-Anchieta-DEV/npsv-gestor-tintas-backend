package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;

import java.math.BigDecimal;

/**
 * Response do endpoint GET /producoes/{id}/pesagem/atual.
 * Consumido pela Aba da Máquina (NPSV-254) para exibição em tempo real.
 */
public record PesagemAtualResponseDTO(
        BigDecimal pesoAlvo,
        BigDecimal pesoLido,
        ResultadoRN01 resultadoRN01,
        StatusProducao statusProducao
) {
}
