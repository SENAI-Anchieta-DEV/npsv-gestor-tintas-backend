package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;

public class UsuarioCreator {

    public static Usuario criarUsuarioAdmin() {
        return Usuario.builder()
                .id("usr-admin")
                .nome("Administrador")
                .email("admin@gestortintas.com")
                .senha("senha123")
                .role(Role.ADMIN)
                .ativo(true)
                .build();
    }

    public static Usuario criarUsuarioColorista() {
        return Usuario.builder()
                .id("usr-colorista")
                .nome("Colorista")
                .email("colorista@gestortintas.com")
                .senha("senha123")
                .role(Role.COLORISTA)
                .ativo(true)
                .build();
    }
}
