package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class CodigoJaExisteException extends RuntimeException {
    public CodigoJaExisteException(String message) {
        super(message);
    }
}
