package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum FormaPagamento {

    @Schema(description = "Pagamento realizado em dinheiro")
    DINHEIRO,

    @Schema(description = "Pagamento realizado via PIX")
    PIX,

    @Schema(description = "Pagamento realizado com cartão de débito")
    CARTAO_DEBITO,

    @Schema(description = "Pagamento realizado com cartão de crédito")
    CARTAO_CREDITO,

    @Schema(description = "Pagamento realizado por meio de boleto bancário")
    BOLETO,

    @Schema(description = "Outro método de pagamento não listado")
    OUTROS
}
