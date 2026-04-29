package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;

public class UsuarioCreator {
    public static Usuario criarUsuarioAdminNovo() {
        return Usuario.builder()
                .nome("Administrador")
                .email("admin@gestortintas.com")
                .senha("senha123")
                .role(Role.ADMIN)
                .ativo(true)
                .build();
    }

    public static Usuario criarUsuarioColoristaNovo() {
        return Usuario.builder()
                .nome("Colorista")
                .email("colorista@gestortintas.com")
                .senha("senha123")
                .role(Role.COLORISTA)
                .ativo(true)
                .build();
    }

    public static Usuario criarUsuarioAdminSalvo() {
        Usuario admin = criarUsuarioAdminNovo();
        admin.setId("usr-admin");
        return admin;
    }

    public static Usuario criarUsuarioColoristaSalvo() {
        Usuario colorista = criarUsuarioColoristaNovo();
        colorista.setId("usr-colorista");
        return colorista;
    }
}
