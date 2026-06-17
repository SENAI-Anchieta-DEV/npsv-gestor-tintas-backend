package com.senai.npsv_gestor_tintas_backend.application.dto.usuario;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioResponseDTO(
        @Schema(description = "Identificador único do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        String nome,

        @Schema(description = "Email do usuário", example = "colorista@gestortintas.com")
        String email,

        @Schema(description = "Função do usuário no sistema", example = "COLORISTA")
        Role role,

        @Schema(description = "Indica se o usuário está ativo ou inativo", example = "true")
        boolean ativo
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.isAtivo()
        );
    }

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.isAtivo()
        );
    }
}
