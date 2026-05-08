package com.senai.npsv_gestor_tintas_backend.integration.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UsuarioRepositoryIntegrationTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve buscar usuário ativo por e-mail")
    void findByEmailAndAtivoTrue_DeveRetornarUsuario() {
        Usuario usuario = UsuarioCreator.criarUsuarioAdminNovo();

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByEmailAndAtivoTrue(usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals("Administrador", resultado.get().getNome());
    }

    @Test
    @DisplayName("Não deve retornar usuário inativo por e-mail")
    void findByEmailAndAtivoTrue_NaoDeveRetornarUsuarioInativo() {
        Usuario usuario = UsuarioCreator.criarUsuarioAdminNovo();
        usuario.setAtivo(false);

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByEmailAndAtivoTrue(usuario.getEmail());

        assertTrue(resultado.isEmpty());
    }
}