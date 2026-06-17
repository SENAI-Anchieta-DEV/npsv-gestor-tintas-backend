package com.senai.npsv_gestor_tintas_backend.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum Role {

    @Schema(description = "Usuário com acesso total ao sistema")
    ADMIN,

    @Schema(description = "Usuário responsável por gerenciar a produção de tintas")
    COLORISTA,

    @Schema(description = "Usuário responsável por registrar vendas e atender clientes")
    VENDEDOR
}
