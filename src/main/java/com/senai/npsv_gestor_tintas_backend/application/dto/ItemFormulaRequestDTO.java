package com.senai.npsv_gestor_tintas_backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemFormulaRequestDTO(
        @Schema(description = "Quantidade necessária do insumo para a fórmula", example = "34.5")
        @NotNull(message = "A quantidade necessária é obrigatória.")
        BigDecimal quantidadeNecessaria,

        @Schema(description = "Ordem de adição do insumo na fórmula, onde 1 é o primeiro a ser adicionado", example = "1")
        @NotNull(message = "A ordem de adição é obrigatória.")
        Integer ordemAdicao,

        @Schema(description = "Identificador do insumo utilizado na fórmula", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID do insumo (Produto) é obrigatório.")
        String insumoId
) {
}