package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PesagemEventoRequestDTO(
        @NotNull(message = "O peso lido pela balança é obrigatório.")
        BigDecimal pesoLido,

        @NotBlank(message = "O ID da produção correspondente é obrigatório.")
        String producaoId
) {
    public PesagemEvento toEntity() {
        return PesagemEvento.builder()
                .pesoLido(this.pesoLido)
                .producao(Producao.builder().id(this.producaoId).build())
                .build();
    }
}