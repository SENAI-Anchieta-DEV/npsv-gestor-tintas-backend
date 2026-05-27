package com.senai.npsv_gestor_tintas_backend.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {

    public record LoginRequest(
            @Schema(description = "Email do usuário para autenticação", example = "email@email.com")
            @NotBlank(message = "O e-mail não pode estar em branco.")
            @Email(message = "O formato do e-mail é inválido.")
            String email,

            @Schema(description = "Senha do usuário para autenticação", example = "senha123")
            @NotBlank(message = "A senha não pode estar em branco.")
            String senha
    ) {}
    public record TokenResponse(
            @Schema(description = "Token JWT gerado após autenticação bem-sucedida", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            String token
    ) {}
}
