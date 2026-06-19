package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.formula.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.FormulaCreator;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FormulaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaProdutoRepository categoriaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private FormulaRepository formulaRepository;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private Produto produto;

    @BeforeEach
    void setup() {
        RestAssured.port = port;

        jdbcTemplate.execute("DELETE FROM item_formula");
        formulaRepository.deleteAll();
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario admin = usuarioRepository.findByEmail("admin@gestortintas.com")
                .orElseGet(() -> usuarioRepository.save(UsuarioCreator.criarUsuarioAdminNovo()));
        admin.setSenha(passwordEncoder.encode("senha123"));
        usuarioRepository.saveAndFlush(admin);

        adminToken = jwtService.generateToken("admin@gestortintas.com", "ADMIN");

        CategoriaProduto categoria = categoriaRepository.saveAndFlush(
                ProdutoCreator.criarCategoriaNova()
        );

        produto = ProdutoCreator.criarProdutoNovo();
        produto.setCategoria(categoria);
        produto = produtoRepository.saveAndFlush(produto);
    }

    @Test
    @DisplayName("Deve criar fórmula e retornar 201")
    void registrarFormula_DeveRetornar201() {
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTO(produto.getId());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/formulas")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("codigoInterno", equalTo("FORM-001"));
    }

    @Test
    @DisplayName("Deve retornar 409 quando código interno já existir")
    void registrarFormula_DeveRetornar409_QuandoCodigoDuplicado() {
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTOCodigoDuplicado(produto.getId());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/formulas")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/formulas")
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    @DisplayName("Deve retornar 400 quando fórmula for inválida")
    void registrarFormula_DeveRetornar400_QuandoSemItens() {
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTOSemItens();

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/api/formulas")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}