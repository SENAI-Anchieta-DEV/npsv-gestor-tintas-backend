package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EstoqueInsuficienteException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.TransicaoDeStatusInvalidaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
import com.senai.npsv_gestor_tintas_backend.util.UsuarioCreator;
import com.senai.npsv_gestor_tintas_backend.util.VendaCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private VendaService vendaService;

    @Test
    @DisplayName("Deve concluir venda com sucesso, calcular o total corretamente e atualizar status")
    void concluirVenda_DeveCalcularTotalEAtualizarStatus_QuandoEstoqueSuficiente() {
        // Arrange
        Venda vendaAberta = VendaCreator.criarVendaAbertaSalva();
        String vendaId = vendaAberta.getId();

        Produto produto = ProdutoCreator.criarProdutoSalvo();
        produto.setPrecoVenda(new BigDecimal("150.0"));

        ItemVendaRequestDTO itemDTO = VendaCreator.criarItemVendaRequestDTO(produto.getId(), "2.0");
        ConcluirVendaRequestDTO requestDTO = VendaCreator.criarConcluirVendaRequestDTO(FormaPagamento.PIX, List.of(itemDTO));

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(vendaAberta));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoRepository.darBaixaEstoque(eq(produto.getId()), any(BigDecimal.class))).thenReturn(1);
        when(vendaRepository.save(any(Venda.class))).thenReturn(vendaAberta);

        // Act
        VendaResponseDTO response = vendaService.concluirVenda(vendaId, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(StatusVenda.CONCLUIDA, response.status());
        assertEquals(0, new BigDecimal("300.00").compareTo(response.valorTotal()));

        verify(produtoRepository, times(1)).darBaixaEstoque(produto.getId(), new BigDecimal("2.0"));
        verify(vendaRepository, times(1)).save(vendaAberta);
    }

    @Test
    @DisplayName("Deve lançar EstoqueInsuficienteException ao concluir venda sem saldo no estoque")
    void concluirVenda_DeveLancarExcecao_QuandoEstoqueInsuficiente() {
        // Arrange
        Venda vendaAberta = VendaCreator.criarVendaAbertaSalva();
        String vendaId = vendaAberta.getId();
        Produto produto = ProdutoCreator.criarProdutoSalvo();

        ItemVendaRequestDTO itemDTO = VendaCreator.criarItemVendaRequestDTO(produto.getId(), "1000.0");
        ConcluirVendaRequestDTO requestDTO = VendaCreator.criarConcluirVendaRequestDTO(FormaPagamento.PIX, List.of(itemDTO));

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(vendaAberta));
        when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));
        when(produtoRepository.darBaixaEstoque(eq(produto.getId()), any(BigDecimal.class))).thenReturn(0); // 0 linhas afetadas = sem stock

        // Act
        EstoqueInsuficienteException exception = assertThrows(
                EstoqueInsuficienteException.class,
                () -> vendaService.concluirVenda(vendaId, requestDTO)
        );
        //Assert
        assertTrue(exception.getMessage().contains("Estoque insuficiente para o produto"));
        verify(vendaRepository, never()).save(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar TransicaoDeStatusInvalidaException ao tentar concluir venda já concluída")
    void concluirVenda_DeveLancarExcecao_QuandoStatusNaoForAberta() {
        // Arrange
        Venda vendaConcluida = VendaCreator.criarVendaConcluidaSalva();
        String vendaId = vendaConcluida.getId();
        ConcluirVendaRequestDTO requestDTO = VendaCreator.criarConcluirVendaRequestDTO(FormaPagamento.DINHEIRO, List.of());

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(vendaConcluida));

        // Act
        TransicaoDeStatusInvalidaException exception = assertThrows(
                TransicaoDeStatusInvalidaException.class,
                () -> vendaService.concluirVenda(vendaId, requestDTO)
        );
        //Assert
        assertTrue(exception.getMessage().contains("Apenas vendas abertas podem ser concluídas"));
    }

    @Test
    @DisplayName("Deve retornar histórico de vendas atrelado a um vendedor específico (RF10)")
    void listarVendasPorVendedor_DeveRetornarListaDeVendas_QuandoVendedorExistir() {
        // Arrange
        Usuario vendedor = UsuarioCreator.criarUsuarioAdminSalvo();
        String vendedorId = vendedor.getId();

        Venda venda1 = VendaCreator.criarVendaConcluidaSalva();
        venda1.setId("v1");
        venda1.setVendedor(vendedor);

        Venda venda2 = VendaCreator.criarVendaConcluidaSalva();
        venda2.setId("v2");
        venda2.setVendedor(vendedor);

        when(vendaRepository.findByVendedorId(vendedorId)).thenReturn(List.of(venda1, venda2));

        // Act
        List<VendaResponseDTO> response = vendaService.listarVendasPorVendedor(vendedorId);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(vendedor.getNome(), response.getFirst().nomeVendedor());
        verify(vendaRepository, times(1)).findByVendedorId(vendedorId);
    }
}