package com.senai.npsv_gestor_tintas_backend.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    public record LoginRequest(
            @NotBlank(message = "O e-mail não pode estar em branco.")
            @Email(message = "O formato do e-mail é inválido.")
            String email,

            @NotBlank(message = "A senha não pode estar em branco.")
            String senha
    ) {}
    public record TokenResponse(
            String token
    ) {}
}
