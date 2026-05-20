package com.senai.npsv_gestor_tintas_backend.infrastructure.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Schema(description = "Estrutura padronizada de erros da API (Baseada na RFC 7807)")
public record ApiErrorResponseSchema(
        @Schema(description = "URI que identifica a categoria do erro", example = "https://gestortintas.com/errors/requisicao-invalida")
        String type,

        @Schema(description = "Título curto do erro", example = "Erro de validação")
        String title,

        @Schema(description = "Código de status HTTP", example = "400")
        int status,

        @Schema(description = "Descrição detalhada do problema", example = "Um ou mais campos no corpo da requisição são inválidos.")
        String detail,

        @Schema(description = "URI da requisição que gerou o erro", example = "/api/produtos")
        String instance,

        @Schema(description = "Data e hora exata do erro", example = "2026-05-20T10:30:00Z")
        Instant timestamp,

        @Schema(description = "Código interno da Regra de Negócio que foi violada (se aplicável)", example = "RN02 – Baixa de Estoque", nullable = true)
        String codigoRegraNegocio,

        @Schema(description = "Mapa detalhado de erros de validação por campo (se aplicável)", example = "{\"codigoBarras\": [\"O código de barras é obrigatório.\"]}", nullable = true)
        Map<String, List<String>> errors
) {
}
