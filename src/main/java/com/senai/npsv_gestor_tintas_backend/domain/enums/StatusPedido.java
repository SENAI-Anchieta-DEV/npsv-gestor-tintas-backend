package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum StatusPedido {

    @Schema(description = "Pedido registrado, aguardando processamento")
    PENDENTE,

    @Schema(description = "Pedido processado e sendo enviado")
    ENVIADO,

    @Schema(description = "Pedido entregue e recebido na loja")
    RECEBIDO,

    @Schema(description = "Pedido cancelado")
    CANCELADO
}
