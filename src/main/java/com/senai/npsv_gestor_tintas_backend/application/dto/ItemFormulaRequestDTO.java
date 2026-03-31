package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemFormulaRequestDTO(
        @NotNull(message = "A quantidade necessária é obrigatória.")
        BigDecimal quantidadeNecessaria,

        @NotNull(message = "A ordem de adição é obrigatória.")
        Integer ordemAdicao,

        @NotBlank(message = "O ID do insumo (Produto) é obrigatório.")
        String insumoId
) {
}