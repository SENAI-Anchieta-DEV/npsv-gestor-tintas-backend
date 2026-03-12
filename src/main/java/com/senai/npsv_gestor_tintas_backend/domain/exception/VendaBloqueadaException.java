package com.senai.npsv_gestor_tintas_backend.domain.exception;

public class VendaBloqueadaException extends RegraNegocioException {
    public VendaBloqueadaException(String mensagem) {
        super(mensagem, "RN03");
    }
}