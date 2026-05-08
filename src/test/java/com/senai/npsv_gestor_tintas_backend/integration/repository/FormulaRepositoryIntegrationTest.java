package com.senai.npsv_gestor_tintas_backend.integration.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.util.FormulaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FormulaRepositoryIntegrationTest {

    @Autowired
    private FormulaRepository formulaRepository;

    @Test
    @DisplayName("Deve buscar fórmula por código interno")
    void findByCodigoInterno_DeveRetornarFormula() {
        Formula formula = FormulaCreator.criarFormulaNova();

        formulaRepository.save(formula);

        Optional<Formula> resultado = formulaRepository.findByCodigoInterno("FORM-001");

        assertTrue(resultado.isPresent());
        assertEquals("Azul Oceano", resultado.get().getNomeCor());
    }

    @Test
    @DisplayName("Não deve retornar fórmula para código inexistente")
    void findByCodigoInterno_DeveRetornarVazio() {
        Optional<Formula> resultado = formulaRepository.findByCodigoInterno("FORM-999");

        assertTrue(resultado.isEmpty());
    }
}