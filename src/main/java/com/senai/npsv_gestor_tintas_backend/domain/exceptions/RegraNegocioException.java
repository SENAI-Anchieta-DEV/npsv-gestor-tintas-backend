package com.senai.npsv_gestor_tintas_backend.domain.exceptions;

import lombok.Getter;

@Getter
public class RegraNegocioException extends RuntimeException {

    private final String codigoErro;

    public RegraNegocioException(String message, String codigoErro) {
        super(message);
        this.codigoErro = codigoErro;
    }
}