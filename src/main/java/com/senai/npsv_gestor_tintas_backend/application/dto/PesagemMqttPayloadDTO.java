package com.senai.npsv_gestor_tintas_backend.application.dto;


import java.math.BigDecimal;

/**
 * DTO que representa exatamente o payload JSON publicado pelo ESP32
 * no tópico v1/dispositivos/{id}/pesagem.
 *
 * Contrato NPSV-116:
 * {
 *   "producaoId":    "uuid-da-producao",
 *   "pesoLido":      125.50,
 *   "unidadeMedida": "G",
 *   "timestamp":     "2026-03-09T10:30:00Z",
 *   "estavel":       true
 * }
 */
public record PesagemMqttPayloadDTO(
        String producaoId,
        BigDecimal pesoLido,
        String unidadeMedida,
        String timestamp,
        boolean estavel
) {
}
