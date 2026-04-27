package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProducaoCreator {
    public static Producao criarProducaoProcessando() {
        return Producao.builder()
                .id("prod-ord-123")
                .dataHora(LocalDateTime.now())
                .status(StatusProducao.PROCESSANDO)
                .colorista(UsuarioCreator.criarUsuarioColorista())
                .formula(criarFormulaValida())
                .build();
    }

    public static Formula criarFormulaValida() {
        Formula formula = Formula.builder()
                .id("form-123")
                .codigoInterno("F-AZUL-01")
                .nomeCor("Azul Oceano")
                .itens(new ArrayList<>())
                .build();

        ItemFormula item = ItemFormula.builder()
                .id("item-1")
                .formula(formula)
                .insumo(ProdutoCreator.criarProdutoValido())
                .quantidadeNecessaria(new BigDecimal("9.0"))
                .ordemAdicao(1)
                .build();

        formula.getItens().add(item);
        return formula;
    }
}
