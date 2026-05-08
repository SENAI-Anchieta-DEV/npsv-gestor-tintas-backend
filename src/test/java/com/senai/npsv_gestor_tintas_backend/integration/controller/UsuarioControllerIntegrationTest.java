package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.Role;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    private String adminToken;

    @BeforeEach
    void setup() {
        RestAssured.port = port;

        Usuario admin = UsuarioCreator.criarUsuarioAdminNovo();

        usuarioRepository.saveAndFlush(admin);

        adminToken = jwtService.generateToken(admin.getEmail(), admin.getRole().name());;
    }

    @Test
    @DisplayName("Deve criar usuário e retornar 201")
    void registrarUsuario_DeveRetornar201() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Vendedor",
                "vendedor@gestortintas.com",
                "senha123",
                Role.VENDEDOR
        );

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("email", equalTo("vendedor@gestortintas.com"))
                .body("role", equalTo("VENDEDOR"));
    }

    @Test
    @DisplayName("Deve retornar 409 ao tentar cadastrar e-mail duplicado")
    void registrarUsuario_DeveRetornar409_QuandoEmailDuplicado() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(
                "Administrador",
                "admin@gestortintas.com",
                "senha123",
                Role.ADMIN
        );

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/usuarios")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Deve listar usuários ativos")
    void listarUsuariosAtivos_DeveRetornar200() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/api/usuarios")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", not(empty()));
    }

    @AfterEach
    void tearDown() {
        usuarioRepository.deleteAll();
    }
}