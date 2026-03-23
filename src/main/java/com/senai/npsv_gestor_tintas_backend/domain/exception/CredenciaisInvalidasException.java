package com.senai.npsv_gestor_tintas_backend.domain.exception;


public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}