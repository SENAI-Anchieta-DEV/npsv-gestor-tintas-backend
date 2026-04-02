package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class EstoqueInsuficienteException extends RegraNegocioException {
    public EstoqueInsuficienteException(String mensagem, String codigoRegraNegocio) {
        super(mensagem, codigoRegraNegocio);
    }
}