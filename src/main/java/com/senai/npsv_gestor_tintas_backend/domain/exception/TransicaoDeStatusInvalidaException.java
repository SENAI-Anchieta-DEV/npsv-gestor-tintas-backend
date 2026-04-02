package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class TransicaoDeStatusInvalidaException extends RuntimeException {
    public TransicaoDeStatusInvalidaException(String message) {
        super(message);
    }
}
