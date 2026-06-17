package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PesagemEventoResponseDTO(
        @Schema(description = "Identificador único do evento de pesagem", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Peso lido pela balança durante o evento de pesagem", example = "150.75")
        BigDecimal pesoLido,

        @Schema(description = "Data e hora atual do evento de pesagem", example = "2026-05-29T14:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Indica se a pesagem foi aprovada ou não", example = "true")
        boolean foiAprovado,

        @Schema(description = "Unidade de medida utilizada para o peso lido", example = "ML")
        UnidadeMedida unidadeMedida,

        @Schema(description = "Indica se a pesagem está dentro dos limites estabelecidos", example = "true")
        Boolean estavel,

        @Schema(description = "Indica o estado atual da pesagem", example = "APROVADO")
        ResultadoRN01 resultadoRN01,

        @Schema(description = "Identificador da produção correspondente ao evento de pesagem", example = "123e4567-e89b-12d3-a456-426614174000")
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