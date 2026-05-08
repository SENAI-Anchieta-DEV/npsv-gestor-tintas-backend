package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.application.dto.AuthDTO;

public class AuthCreator {

    public static AuthDTO.LoginRequest criarLoginRequestValido() {
        return new AuthDTO.LoginRequest(
                "admin@gestortintas.com",
                "senha123"
        );
    }

    public static AuthDTO.LoginRequest criarLoginRequestSenhaInvalida() {
        return new AuthDTO.LoginRequest(
                "admin@gestortintas.com",
                "senha-errada"
        );
    }

    public static AuthDTO.LoginRequest criarLoginRequestUsuarioInexistente() {
        return new AuthDTO.LoginRequest(
                "naoexiste@gestortintas.com",
                "senha123"
        );
    }
}