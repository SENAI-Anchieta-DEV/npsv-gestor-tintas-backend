package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PesagemEventoResponseDTO(
        String id,
        BigDecimal pesoLido,
        LocalDateTime timestamp,
        boolean foiAprovado,
        UnidadeMedida unidadeMedida,
        Boolean estavel,
        ResultadoRN01 resultadoRN01,
        String producaoId
) {
    public static PesagemEventoResponseDTO fromEntity(PesagemEvento evento) {
        if (evento == null) return null;
        return new PesagemEventoResponseDTO(
                evento.getId(),
                evento.getPesoLido(),
                evento.getTimestamp(),
                evento.isFoiAprovado(),
                evento.getUnidadeMedida(),
                evento.getEstavel(),
                evento.getResultadoRN01(),
                evento.getProducao() != null ? evento.getProducao().getId() : null
        );
    }
}