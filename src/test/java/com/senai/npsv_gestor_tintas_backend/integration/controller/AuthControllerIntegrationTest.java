package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.AuthDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.util.AuthCreator;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        RestAssured.port = port;

        usuarioRepository.deleteAll();

        Usuario usuario = UsuarioCreator.criarUsuarioAdminNovo();
        usuario.setSenha(passwordEncoder.encode("senha123"));

        usuarioRepository.saveAndFlush(usuario);
    }

    @Test
    @DisplayName("Deve realizar login e retornar token")
    void login_DeveRetornarToken() {
        AuthDTO.LoginRequest req = AuthCreator.criarLoginRequestValido();

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 401 para credenciais inválidas")
    void login_DeveRetornar401_QuandoCredenciaisInvalidas() {
        AuthDTO.LoginRequest req = AuthCreator.criarLoginRequestSenhaInvalida();

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("title", equalTo("Credenciais Inválidas"));
    }
}