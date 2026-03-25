package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.AuthDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CredenciaisInvalidasException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDTO.LoginRequest req) {
        log.info("Tentativa de autenticação recebida.");
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElse(null);

        if (usuario == null || !encoder.matches(req.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas.");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}