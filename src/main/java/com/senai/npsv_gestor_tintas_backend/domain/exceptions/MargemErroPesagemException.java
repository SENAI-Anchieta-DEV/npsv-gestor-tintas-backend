package com.senai.npsv_gestor_tintas_backend.domain.exceptions;

public class MargemErroPesagemException extends RegraNegocioException {
    public MargemErroPesagemException(String mensagem) {
        super(mensagem, "RN01");
    }
}