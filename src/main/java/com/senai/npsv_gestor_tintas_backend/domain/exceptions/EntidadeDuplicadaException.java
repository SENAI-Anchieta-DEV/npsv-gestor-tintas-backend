package com.senai.npsv_gestor_tintas_backend.domain.exceptions;

public class EntidadeDuplicadaException extends RuntimeException {
    public EntidadeDuplicadaException(String mensagem) {
        super(mensagem);
    }
}