package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        Role role,
        boolean ativo
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
