package com.senai.npsv_gestor_tintas_backend.unit.entity;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProdutoTest {
    @Test
    @DisplayName("Deve mudar alerta para TRUE quando estoque for menor ou igual ao mínimo")
    void atualizarStatus_AlertaDeveMudarParaTrue_QuandoEstoqueAbaixoDoMinimo() {
        // Arrange
        Produto produto = ProdutoCreator.criarProdutoSalvo();
        produto.setQuantidadeEstoque(new BigDecimal("10.0"));
        produto.setEstoqueEmAlerta(false);

        // Act
        produto.atualizarStatusAlerta();

        // Assert
        assertTrue(produto.isEstoqueEmAlerta(), "A flag deveria ser alterada para TRUE");
    }

    @Test
    @DisplayName("Deve manter alerta como FALSE quando estoque for maior que o mínimo")
    void atualizarStatusAlerta_DeveManterFalse_QuandoEstoqueAcimaDoMinimo() {
        // Arrange
        Produto produto = ProdutoCreator.criarProdutoSalvo();
        produto.setQuantidadeEstoque(new BigDecimal("50.0"));
        produto.setEstoqueEmAlerta(false);

        // Act
        produto.atualizarStatusAlerta();

        // Assert
        assertFalse(produto.isEstoqueEmAlerta(), "A flag deveria se manter FALSE");
    }
}
