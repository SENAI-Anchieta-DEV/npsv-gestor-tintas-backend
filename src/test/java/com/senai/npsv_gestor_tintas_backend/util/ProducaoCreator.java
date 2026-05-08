package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.domain.entity.*;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProducaoCreator {
    public static Formula criarFormulaNova(Produto insumoSalvoNoBanco) {
        Formula formula = Formula.builder()
                .codigoInterno("F-AZUL-01")
                .nomeCor("Azul Oceano")
                .itens(new ArrayList<>())
                .build();

        ItemFormula item = ItemFormula.builder()
                .formula(formula)
                .insumo(insumoSalvoNoBanco)
                .quantidadeNecessaria(new BigDecimal("9.0"))
                .ordemAdicao(1)
                .build();

        formula.getItens().add(item);
        return formula;
    }

    public static Producao criarProducaoProcessandoNova(Usuario coloristaSalvoNoBanco, Formula formulaSalvaNoBanco) {
        return Producao.builder()
                .dataHora(LocalDateTime.now())
                .status(StatusProducao.PROCESSANDO)
                .colorista(coloristaSalvoNoBanco)
                .formula(formulaSalvaNoBanco)
                .build();
    }

    public static Formula criarFormulaSalva() {
       Formula formula = criarFormulaNova(ProdutoCreator.criarProdutoSalvo());
        formula.setId("form-123");
        formula.getItens().getFirst().setId("item-1");
        return formula;
    }

    public static Producao criarProducaoProcessandoSalva() {
        Producao producao = criarProducaoProcessandoNova(
                UsuarioCreator.criarUsuarioColoristaSalvo(),
                criarFormulaSalva()
        );
        producao.setId("prod-ord-123");
        return producao;
    }
}
