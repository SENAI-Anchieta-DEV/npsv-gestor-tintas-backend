package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class EstoqueBaixoException extends RegraNegocioException {
    public EstoqueBaixoException(String mensagem) {
        super(mensagem, "RN02");
    }
}