package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.AuthDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.AuthService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CredenciaisInvalidasException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.AuthCreator;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarios;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwt;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve realizar login com sucesso quando credenciais forem válidas")
    void login_DeveRetornarToken_QuandoCredenciaisValidas() {
        AuthDTO.LoginRequest req = AuthCreator.criarLoginRequestValido();

        Usuario usuario = UsuarioCreator.criarUsuarioAdminSalvo();
        usuario.setSenha("senha-criptografada");

        when(usuarios.findByEmail("admin@gestortintas.com")).thenReturn(Optional.of(usuario));
        when(encoder.matches("senha123", "senha-criptografada")).thenReturn(true);
        when(jwt.generateToken("admin@gestortintas.com", "ADMIN")).thenReturn("token-valido");

        String token = authService.login(req);

        assertEquals("token-valido", token);
        verify(usuarios).findByEmail("admin@gestortintas.com");
        verify(encoder).matches("senha123", "senha-criptografada");
        verify(jwt).generateToken("admin@gestortintas.com", "ADMIN");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não existir")
    void login_DeveLancarExcecao_QuandoUsuarioNaoExistir() {
        AuthDTO.LoginRequest req = AuthCreator.criarLoginRequestUsuarioInexistente();

        when(usuarios.findByEmail("naoexiste@gestortintas.com")).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class, () -> authService.login(req));

        verify(usuarios).findByEmail("naoexiste@gestortintas.com");
        verify(encoder, never()).matches(anyString(), anyString());
        verify(jwt, never()).generateToken(anyString(), anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a senha for inválida")
    void login_DeveLancarExcecao_QuandoSenhaInvalida() {
        AuthDTO.LoginRequest req = AuthCreator.criarLoginRequestSenhaInvalida();

        Usuario usuario = UsuarioCreator.criarUsuarioAdminSalvo();
        usuario.setSenha("senha-criptografada");

        when(usuarios.findByEmail("admin@gestortintas.com")).thenReturn(Optional.of(usuario));
        when(encoder.matches("senha-errada", "senha-criptografada")).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class, () -> authService.login(req));

        verify(usuarios).findByEmail("admin@gestortintas.com");
        verify(encoder).matches("senha-errada", "senha-criptografada");
        verify(jwt, never()).generateToken(anyString(), anyString());
    }
}