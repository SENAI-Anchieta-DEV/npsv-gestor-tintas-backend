package com.senai.npsv_gestor_tintas_backend.domain.enums;

/**
 * Resultado da validação da Regra de Negócio RN01.
 * APROVADO            → peso estável e dentro da margem de 5%
 * FORA_DA_MARGEM      → peso estável mas fora da margem de 5%
 * AGUARDANDO_ESTABILIDADE → flag estavel=false enviada pelo ESP32
 */
public enum ResultadoRN01 {
    APROVADO,
    FORA_DA_MARGEM,
    AGUARDANDO_ESTABILIDADE
}
