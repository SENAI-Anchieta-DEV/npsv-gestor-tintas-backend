package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PesagemEventoResponseDTO(
        String id,
        BigDecimal pesoLido,
        LocalDateTime timestamp,
        boolean foiAprovado,
        String producaoId
) {
    public static PesagemEventoResponseDTO fromEntity(PesagemEvento evento) {
        if (evento == null) return null;
        return new PesagemEventoResponseDTO(
                evento.getId(),
                evento.getPesoLido(),
                evento.getTimestamp(),
                evento.isFoiAprovado(),
                evento.getProducao() != null ? evento.getProducao().getId() : null
        );
    }
}