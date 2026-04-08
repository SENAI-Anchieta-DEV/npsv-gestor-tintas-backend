package com.senai.npsv_gestor_tintas_backend.domain.exception;

import lombok.Getter;

@Getter
public class RegraNegocioException extends RuntimeException {

    private final String codigoRegraNegocio;

    public RegraNegocioException(String message, String codigoRegraNegocio) {
        super(message);
        this.codigoRegraNegocio = codigoRegraNegocio;
    }
}