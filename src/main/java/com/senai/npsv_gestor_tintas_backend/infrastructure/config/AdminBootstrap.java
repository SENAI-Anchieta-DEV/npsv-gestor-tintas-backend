package com.senai.npsv_gestor_tintas_backend.infrastructure.config;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        usuarioRepository.findByEmail(adminEmail).ifPresentOrElse(
                admin -> {
                    if (!admin.isAtivo()) {
                        admin.setAtivo(true);
                        usuarioRepository.save(admin);
                    }
                },
                () -> {
                    Usuario admin = Usuario.builder()
                            .ativo(true)
                            .nome("Administrador Provisório")
                            .email(adminEmail)
                            .senha(passwordEncoder.encode(adminSenha))
                            .role(Role.ADMIN)
                            .build();
                    usuarioRepository.save(admin);
                    System.out.println("⚡ Usuário admin provisório criado: " + adminEmail);
                }
        );
    }
}
