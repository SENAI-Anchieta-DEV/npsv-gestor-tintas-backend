package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum StatusProducao {

    @Schema(description = "Ordem criada, aguardando início da pesagem")
    PENDENTE,

    @Schema(description = "Em processo de mistura e pesagem")
    PROCESSANDO,

    @Schema(description = "Produção finalizada com sucesso")
    CONCLUIDO,

    @Schema(description = "Ordem cancelada antes do início")
    CANCELADO,

    @Schema(description = "Produção interrompida durante a produção, resultando em perda total da tinta")
    PERDA_TOTAL
}
