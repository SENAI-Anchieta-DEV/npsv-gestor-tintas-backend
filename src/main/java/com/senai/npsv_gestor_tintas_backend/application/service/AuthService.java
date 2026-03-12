package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.AuthDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CredenciaisInvalidasException; // Import no SINGULAR
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException; // Import no SINGULAR
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDTO.LoginRequest req) {
        System.out.println("Autenticando usuário: " + req.email());
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() ->  new EntidadeNaoEncontradaException("Usuário não encontrado com este e-mail."));

        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Palavra-passe inválida.");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}