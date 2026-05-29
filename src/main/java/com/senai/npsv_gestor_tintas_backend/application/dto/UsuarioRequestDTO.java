package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @Schema(description = "Email do usuário para login e contato", example = "colorista@gestortintas.com")
        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Senha de acesso do usuário", example = "senha123")
        @NotBlank(message = "A senha é obrigatória.")
        String senha,

        @Schema(description = "Função do usuário no sistema", example = "COLORISTA")
        @NotNull(message = "A role (ADMIN, COLORISTA ou VENDEDOR) deve ser informada.")
        Role role
) {
    public Usuario toEntity() {
        return Usuario.builder()
                .ativo(true)
                .nome(this.nome)
                .email(this.email)
                .role(this.role)
                .build();
    }
}
