package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;

import java.math.BigDecimal;

public class ProdutoCreator {
    public static CategoriaProduto criarCategoriaValida() {
        return CategoriaProduto.builder()
                .id("cat-123")
                .nome("Insumos Base")
                .descricao("Matérias-primas para mistura")
                .build();
    }

    public static Produto criarProdutoValido() {
        return Produto.builder()
                .id("prod-123")
                .codigoBarras("789123456")
                .descricao("Tinta Base Branca 18L")
                .quantidadeEstoque(new BigDecimal("100.0"))
                .precoCusto(new BigDecimal("50.0"))
                .precoVenda(new BigDecimal("120.0"))
                .unidadeMedida(UnidadeMedida.L)
                .estoqueMinimo(new BigDecimal("15.0"))
                .estoqueEmAlerta(false)
                .categoria(criarCategoriaValida())
                .build();
    }

    public static ProdutoRequestDTO criarProdutoRequestDTO() {
        return new ProdutoRequestDTO(
                "789123456",
                "Tinta Base Branca 18L",
                new BigDecimal("100.0"),
                new BigDecimal("50.0"),
                new BigDecimal("120.0"),
                UnidadeMedida.L,
                "cat-123",
                new BigDecimal("15.0")
        );
    }
}
