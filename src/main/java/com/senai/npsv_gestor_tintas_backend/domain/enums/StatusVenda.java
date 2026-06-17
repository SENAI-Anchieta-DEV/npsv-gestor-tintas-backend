package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum StatusVenda {

    @Schema(description = "Venda registrada, aguardando pagamento ou finalização")
    PENDENTE,

    @Schema(description = "Venda finalizada com sucesso, pagamento confirmado e produtos entregues ao cliente")
    CONCLUIDA,

    @Schema(description = "Venda iniciada, mas ainda em aberto, aguardando ações adicionais para conclusão")
    ABERTA,

    @Schema(description = "Venda cancelada antes da finalização, sem conclusão ou entrega dos produtos")
    CANCELADA
}