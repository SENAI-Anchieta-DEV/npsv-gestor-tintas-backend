package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.application.dto.formula.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.formula.ItemFormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;

import java.math.BigDecimal;
import java.util.List;

public class FormulaCreator {

    public static Formula criarFormulaNova() {
        return Formula.builder()
                .codigoInterno("FORM-001")
                .nomeCor("Azul Oceano")
                .itens(List.of())
                .build();
    }

    public static Formula criarFormulaSalva() {
        Formula formula = criarFormulaNova();
        formula.setId("form-123");
        return formula;
    }

    public static FormulaRequestDTO criarFormulaRequestDTO(String produtoId) {
        return new FormulaRequestDTO(
                "FORM-001",
                "Azul Oceano",
                List.of(
                        new ItemFormulaRequestDTO(
                                new BigDecimal("50"),
                                1,
                                produtoId
                        )
                )
        );
    }

    public static FormulaRequestDTO criarFormulaRequestDTOCodigoDuplicado(String produtoId) {
        return new FormulaRequestDTO(
                "FORM-001",
                "Azul Oceano",
                List.of(
                        new ItemFormulaRequestDTO(
                                new BigDecimal("50"),
                                1,
                                produtoId
                        )
                )
        );
    }

    public static FormulaRequestDTO criarFormulaRequestDTOSemItens() {
        return new FormulaRequestDTO(
                "FORM-002",
                "Azul Sem Itens",
                List.of()
        );
    }

    public static FormulaRequestDTO criarFormulaRequestDTOInsumoInexistente() {
        return new FormulaRequestDTO(
                "FORM-001",
                "Azul Oceano",
                List.of(
                        new ItemFormulaRequestDTO(
                                new BigDecimal("50"),
                                1,
                                "prod-inexistente"
                        )
                )
        );
    }
}