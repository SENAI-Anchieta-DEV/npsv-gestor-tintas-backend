package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.domain.entity.*;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.repository.*;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.ProducaoCreator;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ProducaoControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired private ProducaoRepository producaoRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CategoriaProdutoRepository categoriaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private FormulaRepository formulaRepository;
    @Autowired private PesagemEventoRepository pesagemEventoRepository;
    @Autowired private JwtService jwtService;

    private String adminToken;
    private Producao producaoSalva;
    private Produto insumoSalvo;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
        adminToken = jwtService.generateToken("admin@gestortintas.com", "ADMIN");

        Producao producaoFake = ProducaoCreator.criarProducaoProcessando();

        usuarioRepository.save(producaoFake.getColorista());

        insumoSalvo = producaoFake.getFormula().getItens().getFirst().getInsumo();
        categoriaRepository.save(insumoSalvo.getCategoria());
        produtoRepository.save(insumoSalvo);

        formulaRepository.save(producaoFake.getFormula());

        producaoSalva = producaoRepository.save(producaoFake);

        PesagemEvento pesagem = PesagemEvento.builder()
                .producao(producaoSalva)
                .pesoLido(new BigDecimal("9.0"))
                .timestamp(LocalDateTime.now())
                .foiAprovado(true)
                .build();
        pesagemEventoRepository.save(pesagem);
    }

    @Test
    @DisplayName("CT-02: Integração - Deve concluir produção e descontar 9L do estoque (100 -> 91)")
    void concluirProducaoDeveRetornar200EDescontarEstoque() {
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/producoes/" + producaoSalva.getId() + "/concluir")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("CONCLUIDO"));

        Produto produtoAtualizado = produtoRepository.findById(insumoSalvo.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("91.0").compareTo(produtoAtualizado.getQuantidadeEstoque()));
    }

    @Test
    @DisplayName("BUG-01: Integração - Rollback Atômico se o estoque falhar, mantendo produção PROCESSANDO")
    void concluirProducao_DeveGarantirRollback_QuandoEstoqueFalha() {
        // Arrange
        insumoSalvo.setQuantidadeEstoque(new BigDecimal("5.0"));
        produtoRepository.saveAndFlush(insumoSalvo);

        // Act
        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .when()
                .patch("/api/producoes/" + producaoSalva.getId() + "/concluir")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("codigoRegraNegocio", equalTo("RN02 – Baixa de Estoque"));

        // Assert
        Producao producaoAposTentativa = producaoRepository.findById(producaoSalva.getId()).orElseThrow();
        assertEquals(StatusProducao.PROCESSANDO, producaoAposTentativa.getStatus(),
                "O BUG-01 Ocorreu: A produção alterou o status sem ter estoque!");

        Produto produtoAposTentativa = produtoRepository.findById(insumoSalvo.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("5.0").compareTo(produtoAposTentativa.getQuantidadeEstoque()));
    }
}
