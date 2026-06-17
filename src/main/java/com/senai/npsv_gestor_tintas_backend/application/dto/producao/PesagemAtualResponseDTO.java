package com.senai.npsv_gestor_tintas_backend.application.dto.producao;

import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * Response do endpoint GET /producoes/{id}/pesagem/atual.
 * Consumido pela Aba da Máquina (NPSV-254) para exibição em tempo real.
 */
public record PesagemAtualResponseDTO(
        @Schema(description = "Peso alvo definido para a produção", example = "100.0")
        BigDecimal pesoAlvo,

        @Schema(description = "Peso atual registrado na produção", example = "75.5")
        BigDecimal pesoLido,

        @Schema(description = "Indica o estado atual da pesagem", example = "FORA_DA_MARGEM")
        ResultadoRN01 resultadoRN01,

        @Schema(description = "Status atual da produção", example = "EM_ANDAMENTO")
        StatusProducao statusProducao
) {
}
