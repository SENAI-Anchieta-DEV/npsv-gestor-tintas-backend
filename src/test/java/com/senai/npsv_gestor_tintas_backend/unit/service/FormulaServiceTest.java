package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.FormulaService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CodigoJaExisteException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.util.FormulaCreator;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormulaServiceTest {

    @Mock
    private FormulaRepository formulaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private FormulaService formulaService;

    @Test
    @DisplayName("Deve registrar fórmula com sucesso")
    void registrarFormula_DeveSalvar_QuandoCodigoNaoExistir() {
        Produto produto = ProdutoCreator.criarProdutoSalvo();
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTO(produto.getId());

        when(formulaRepository.findByCodigoInterno("FORM-001")).thenReturn(Optional.empty());
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(formulaRepository.saveAndFlush(any(Formula.class))).thenAnswer(invocation -> {
            Formula formula = invocation.getArgument(0);
            formula.setId("form-999");
            return formula;
        });

        FormulaResponseDTO response = formulaService.registrarFormula(dto);

        assertNotNull(response);
        assertEquals("FORM-001", response.codigoInterno());

        ArgumentCaptor<Formula> captor = ArgumentCaptor.forClass(Formula.class);
        verify(formulaRepository).saveAndFlush(captor.capture());
        assertEquals(1, captor.getValue().getItens().size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando código interno já existir")
    void registrarFormula_DeveLancarExcecao_QuandoCodigoJaExistir() {
        Formula formulaExistente = FormulaCreator.criarFormulaSalva();
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTO("prod-123");

        when(formulaRepository.findByCodigoInterno("FORM-001")).thenReturn(Optional.of(formulaExistente));

        assertThrows(CodigoJaExisteException.class, () -> formulaService.registrarFormula(dto));
        verify(formulaRepository, never()).saveAndFlush(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando insumo não existir")
    void registrarFormula_DeveLancarExcecao_QuandoInsumoNaoExistir() {
        FormulaRequestDTO dto = FormulaCreator.criarFormulaRequestDTO("prod-inexistente");

        when(formulaRepository.findByCodigoInterno("FORM-001")).thenReturn(Optional.empty());
        when(produtoRepository.findById("prod-inexistente")).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> formulaService.registrarFormula(dto));
    }

    @Test
    @DisplayName("Deve listar fórmulas")
    void listarFormulas_DeveRetornarLista() {
        Formula formula = FormulaCreator.criarFormulaSalva();

        when(formulaRepository.findAll()).thenReturn(List.of(formula));

        List<FormulaResponseDTO> resultado = formulaService.listarFormulas();

        assertEquals(1, resultado.size());
        assertEquals("FORM-001", resultado.get(0).codigoInterno());
    }
}