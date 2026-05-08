package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.UsuarioService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeDuplicadaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve registrar usuário com sucesso")
    void registrarUsuario_DeveSalvar_QuandoEmailNaoExistir() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Novo Usuário",
                "novo@gestortintas.com",
                "senha123",
                Role.VENDEDOR
        );

        when(repository.findByEmailAndAtivoTrue("novo@gestortintas.com")).thenReturn(Optional.empty());
        when(encoder.encode("senha123")).thenReturn("senha-criptografada");
        when(repository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId("usr-999");
            return u;
        });

        UsuarioResponseDTO response = usuarioService.registrarUsuario(dto);

        assertNotNull(response);
        assertEquals("novo@gestortintas.com", response.email());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar usuário com e-mail duplicado")
    void registrarUsuario_DeveLancarExcecao_QuandoEmailDuplicado() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Administrador",
                "admin@gestortintas.com",
                "senha123",
                Role.ADMIN
        );

        Usuario admin = UsuarioCreator.criarUsuarioAdminSalvo();

        when(repository.findByEmailAndAtivoTrue("admin@gestortintas.com")).thenReturn(Optional.of(admin));

        assertThrows(EntidadeDuplicadaException.class, () -> usuarioService.registrarUsuario(dto));
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar usuários ativos")
    void listarUsuariosAtivos_DeveRetornarLista() {
        Usuario usuario = UsuarioCreator.criarUsuarioAdminSalvo();

        when(repository.findAllByAtivoTrue()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuariosAtivos();

        assertEquals(1, resultado.size());
        assertEquals("admin@gestortintas.com", resultado.get(0).email());
    }

    @Test
    @DisplayName("Deve atualizar senha do usuário")
    void atualizarSenhaUsuario_DeveSalvarNovaSenha() {
        Usuario usuario = UsuarioCreator.criarUsuarioAdminSalvo();
        usuario.setSenha("senha-antiga");

        when(repository.findByEmailAndAtivoTrue("admin@gestortintas.com")).thenReturn(Optional.of(usuario));
        when(encoder.encode("novaSenha")).thenReturn("nova-senha-criptografada");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponseDTO response = usuarioService.atualizarSenhaUsuario("admin@gestortintas.com", "novaSenha");

        assertNotNull(response);
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("Deve deletar usuário logicamente")
    void deletarUsuario_DeveMarcarComoInativo() {
        Usuario usuario = UsuarioCreator.criarUsuarioAdminSalvo();

        when(repository.findByEmailAndAtivoTrue("admin@gestortintas.com")).thenReturn(Optional.of(usuario));

        usuarioService.deletarUsuario("admin@gestortintas.com");

        assertFalse(usuario.isAtivo());
        verify(repository).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void listarUsuarioPorEmail_DeveLancarExcecao_QuandoNaoEncontrar() {
        when(repository.findByEmailAndAtivoTrue("inexistente@gestortintas.com")).thenReturn(Optional.empty());

        assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> usuarioService.listarUsuarioPorEmail("inexistente@gestortintas.com")
        );
    }
}