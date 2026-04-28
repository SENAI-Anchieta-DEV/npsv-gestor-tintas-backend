package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProdutoControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaRepository;

    @Autowired
    private JwtService jwtService;

    private String adminToken;
    private CategoriaProduto categoriaBase;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;

        categoriaBase = ProdutoCreator.criarCategoriaValida();
        categoriaRepository.save(categoriaBase);

        adminToken = jwtService.generateToken("admin@gestortintas.com", "ADMIN");
    }

    @Test
    @DisplayName("CT-01: Integração - Deve criar produto e retornar status 201 Created")
    void criarProdutoDeveRetornarStatus201() {
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO();

        given()
                .header("Authorization", "Bearer " + adminToken) // Passando o JWT
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
    @DisplayName("BUG-02: Integração - Deve retornar 409 Conflict ao tentar duplicar código de barras")
    void criarProdutoDeveRetornarStatus409QuandoCodigoDuplicado() {
        // Arrange
        Produto produto1 = ProdutoCreator.criarProdutoValido();
        produto1.setCategoria(categoriaBase);
        produtoRepository.save(produto1);

        // Act
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO();

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

}
