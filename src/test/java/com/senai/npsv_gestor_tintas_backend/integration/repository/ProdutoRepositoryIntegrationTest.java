package com.senai.npsv_gestor_tintas_backend.integration.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProdutoRepositoryIntegrationTest {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaProdutoRepository categoriaRepository;

    @Test
    @DisplayName("CT-04: Integração DB - Deve marcar estoqueEmAlerta=true ao dar baixa abaixo do mínimo")
    void darBaixaEstoque_DeveAtualizarFlagDeAlerta_QuandoAtingirMinimo() {
        // Arrange
        CategoriaProduto categoria = categoriaRepository.save(ProdutoCreator.criarCategoriaValida());
        Produto produto = ProdutoCreator.criarProdutoValido();
        produto.setCategoria(categoria);
        Produto salvo = produtoRepository.save(produto);

        // Act
        int linhasAfetadas = produtoRepository.darBaixaEstoque(salvo.getId(), new BigDecimal("91.0"));

        // Assert
        assertEquals(1, linhasAfetadas, "A query deve afetar exatamente 1 linha");

        produtoRepository.flush();

        Produto produtoAtualizado = produtoRepository.findById(salvo.getId()).orElseThrow();
        assertEquals(0, new BigDecimal("9.0").compareTo(produtoAtualizado.getQuantidadeEstoque()));
        assertTrue(produtoAtualizado.isEstoqueEmAlerta(), "O banco de dados deveria ter mudado a flag para TRUE magicamente!");
    }
}
