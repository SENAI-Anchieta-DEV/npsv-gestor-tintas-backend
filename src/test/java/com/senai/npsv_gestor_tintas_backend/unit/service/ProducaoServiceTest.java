package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ProducaoService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EstoqueInsuficienteException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PesagemEventoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.util.ProducaoCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProducaoServiceTest {
    @Mock
    private ProducaoRepository producaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PesagemEventoRepository pesagemEventoRepository;

    @InjectMocks
    private ProducaoService producaoService;

    @Test
    @DisplayName("CT-02: Deve concluir produção e dar baixa no estoque quando houver saldo")
    void concluirProducao_DeveRetornarProducaoConcluida_QuandoEstoqueSuficiente() {
        // Arrange
        Producao producaoMock = ProducaoCreator.criarProducaoProcessando();
        String producaoId = producaoMock.getId();
        String produtoId = producaoMock.getFormula().getItens().getFirst().getInsumo().getId();

        when(producaoRepository.findById(producaoId)).thenReturn(Optional.of(producaoMock));
        when(pesagemEventoRepository.existsByProducaoId(producaoId)).thenReturn(true);
        when(producaoRepository.save(any(Producao.class))).thenReturn(producaoMock);

        // Simulando sucesso na query de banco (1 linha afetada)
        when(produtoRepository.darBaixaEstoque(eq(produtoId), any(BigDecimal.class))).thenReturn(1);

        // Act
        ProducaoResponseDTO response = producaoService.concluirProducao(producaoId);

        // Assert
        assertNotNull(response);
        assertEquals(StatusProducao.CONCLUIDO, response.status());
        verify(produtoRepository, times(1)).darBaixaEstoque(eq(produtoId), any(BigDecimal.class));
    }

    @Test
    @DisplayName("CT-03: Deve lançar exceção de estoque quando saldo for insuficiente")
    void concluirProducao_DeveLancarExcecao_QuandoEstoqueInsuficiente() {
        // Arrange
        Producao producaoMock = ProducaoCreator.criarProducaoProcessando();
        String producaoId = producaoMock.getId();
        String produtoId = producaoMock.getFormula().getItens().getFirst().getInsumo().getId();

        when(producaoRepository.findById(producaoId)).thenReturn(Optional.of(producaoMock));
        when(pesagemEventoRepository.existsByProducaoId(producaoId)).thenReturn(true);
        when(producaoRepository.save(any(Producao.class))).thenReturn(producaoMock);

        // Simulando falha de estoque (0 linhas afetadas)
        when(produtoRepository.darBaixaEstoque(eq(produtoId), any(BigDecimal.class))).thenReturn(0);

        // Act & Assert
        EstoqueInsuficienteException exception = assertThrows(
                EstoqueInsuficienteException.class,
                () -> producaoService.concluirProducao(producaoId)
        );

        assertTrue(exception.getMessage().contains("Estoque insuficiente"));
        assertEquals("RN02 – Baixa de Estoque", exception.getCodigoRegraNegocio());
    }
}
