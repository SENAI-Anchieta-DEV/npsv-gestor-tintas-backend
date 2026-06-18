package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.produto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProdutoControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CategoriaProdutoRepository categoriaRepository;
    @Autowired private JwtService jwtService;
    @Autowired private UsuarioRepository usuarioRepository;

    private String adminToken;
    private CategoriaProduto categoriaBase;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;

        Usuario admin = usuarioRepository.save(UsuarioCreator.criarUsuarioAdminNovo());

        categoriaBase = categoriaRepository.save(ProdutoCreator.criarCategoriaNova());

        adminToken = jwtService.generateToken(admin.getEmail(), "ADMIN");
    }

    @Test
    @DisplayName("Deve criar produto e retornar status 201 Created")
    void criarProduto_DeveRetornarStatus201() {
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO(categoriaBase.getId());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/api/produtos")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("codigoBarras", equalTo("789123456"))
                .body("estoqueEmAlerta", equalTo(false));
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict ao tentar duplicar código de barras")
    void criarProduto_DeveRetornarStatus409_QuandoCodigoDuplicado() {
        // Arrange
        Produto produto1 = ProdutoCreator.criarProdutoNovo();
        produto1.setCategoria(categoriaBase);
        produtoRepository.save(produto1);

        // Act
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO(categoriaBase.getId());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/api/produtos")
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("title", equalTo("Código já existente"));
    }

    @AfterEach
    void tearDown() {
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }
}
