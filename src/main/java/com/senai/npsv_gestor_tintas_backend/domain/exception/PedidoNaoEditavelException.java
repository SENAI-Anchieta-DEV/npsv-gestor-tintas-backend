package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class PedidoNaoEditavelException extends RuntimeException {
    public PedidoNaoEditavelException(String message) {
        super(message);
    }
}
