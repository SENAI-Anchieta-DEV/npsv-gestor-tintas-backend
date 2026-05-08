package com.senai.npsv_gestor_tintas_backend.integration.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ConcluirVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.IniciarVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import com.senai.npsv_gestor_tintas_backend.infrastructure.security.JwtService;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import com.senai.npsv_gestor_tintas_backend.util.VendaCreator;
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

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired private VendaRepository vendaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CategoriaProdutoRepository categoriaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private JwtService jwtService;

    private String vendedorToken;
    private Usuario vendedorSalvo;
    private Produto produtoSalvo;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;

        vendedorSalvo = usuarioRepository.save(UsuarioCreator.criarUsuarioAdminNovo());

        vendedorToken = jwtService.generateToken(vendedorSalvo.getEmail(), vendedorSalvo.getRole().name());

        CategoriaProduto categoria = categoriaRepository.save(ProdutoCreator.criarCategoriaNova());

        Produto produtoNovo = ProdutoCreator.criarProdutoNovo();
        produtoNovo.setCategoria(categoria);
        produtoNovo.setQuantidadeEstoque(new BigDecimal("50.0"));

        produtoSalvo = produtoRepository.save(produtoNovo);
    }

    @AfterEach
    void tearDown() {
        vendaRepository.deleteAll();
        produtoRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve iniciar uma venda e retornar status 201 Created")
    void iniciarVenda_DeveRetornarStatus201() {
        IniciarVendaRequestDTO requestDTO = VendaCreator.criarIniciarVendaRequestDTO(vendedorSalvo.getId());

        given()
                .header("Authorization", "Bearer " + vendedorToken)
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/api/vendas/iniciar")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue())
                .body("status", equalTo("ABERTA"))
                .body("nomeVendedor", equalTo(vendedorSalvo.getNome()));
    }

    @Test
    @DisplayName("Deve concluir venda, retornar status 200 OK e descontar estoque real")
    void concluirVenda_DeveRetornar200EDescontarEstoque_QuandoEstoqueSuficiente() {

        Venda vendaSalva = vendaRepository.save(VendaCreator.criarVendaAbertaNova());
        vendaSalva.setVendedor(vendedorSalvo);
        vendaRepository.save(vendaSalva);

        ItemVendaRequestDTO item = VendaCreator.criarItemVendaRequestDTO(produtoSalvo.getId(), "10.0");
        ConcluirVendaRequestDTO requestDTO = VendaCreator.criarConcluirVendaRequestDTO(FormaPagamento.PIX, List.of(item));

        given()
                .header("Authorization", "Bearer " + vendedorToken)
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .patch("/api/vendas/" + vendaSalva.getId() + "/concluir")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("CONCLUIDA"));

        Produto produtoAtualizado = produtoRepository.findById(produtoSalvo.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("40.0").compareTo(produtoAtualizado.getQuantidadeEstoque()));
    }

    @Test
    @DisplayName("Deve retornar 422 Unprocessable Entity e fazer rollback de estoque ao falhar")
    void concluirVenda_DeveRetornar422ERollback_QuandoEstoqueInsuficiente() {
        Venda vendaSalva = vendaRepository.save(VendaCreator.criarVendaAbertaNova());
        vendaSalva.setVendedor(vendedorSalvo);
        vendaRepository.save(vendaSalva);

        ItemVendaRequestDTO item = VendaCreator.criarItemVendaRequestDTO(produtoSalvo.getId(), "60.0");
        ConcluirVendaRequestDTO requestDTO = VendaCreator.criarConcluirVendaRequestDTO(FormaPagamento.CARTAO_CREDITO, List.of(item));

        given()
                .header("Authorization", "Bearer " + vendedorToken)
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .patch("/api/vendas/" + vendaSalva.getId() + "/concluir")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("codigoRegraNegocio", equalTo("RN03 – Bloqueio de Venda"));

        Produto produtoAposTentativa = produtoRepository.findById(produtoSalvo.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("50.0").compareTo(produtoAposTentativa.getQuantidadeEstoque()));
    }
}