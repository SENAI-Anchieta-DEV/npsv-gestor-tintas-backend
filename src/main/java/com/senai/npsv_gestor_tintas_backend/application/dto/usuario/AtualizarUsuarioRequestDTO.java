package com.senai.npsv_gestor_tintas_backend.application.dto.usuario;

import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarUsuarioRequestDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotNull(message = "A role (ADMIN, COLORISTA ou VENDEDOR) deve ser informada.")
        Role role
) {}
