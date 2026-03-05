package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        String senha,

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
