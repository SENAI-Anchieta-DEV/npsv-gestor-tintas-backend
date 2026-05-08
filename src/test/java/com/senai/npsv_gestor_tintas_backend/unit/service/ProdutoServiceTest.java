package com.senai.npsv_gestor_tintas_backend.unit.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ProdutoService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CodigoJaExisteException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.util.ProdutoCreator;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaProdutoRepository categoriaRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve registrar um produto com sucesso quando os dados forem válidos")
    void registrarProduto_DeveRetornarProduto_QuandoDadosValidos() {
        // Arrange
        CategoriaProduto categoriaMock = ProdutoCreator.criarCategoriaNova();
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO(categoriaMock.getId());
        Produto produtoMock = ProdutoCreator.criarProdutoNovo();

        when(produtoRepository.findByCodigoBarras(requestDTO.codigoBarras())).thenReturn(Optional.empty());
        when(categoriaRepository.findById(requestDTO.categoriaId())).thenReturn(Optional.of(categoriaMock));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoMock);

        // Act
        ProdutoResponseDTO response = produtoService.registrarProduto(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(requestDTO.codigoBarras(), response.codigoBarras());

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar registrar produto com código de barras já existente")
    void registrarProduto_DeveLancarExcecao_QuandoCodigoBarrasJaExiste() {
        // Arrange
        CategoriaProduto categoriaMock = ProdutoCreator.criarCategoriaNova();
        ProdutoRequestDTO requestDTO = ProdutoCreator.criarProdutoRequestDTO(categoriaMock.getId());
        Produto produtoExistente = ProdutoCreator.criarProdutoSalvo();

        when(produtoRepository.findByCodigoBarras(requestDTO.codigoBarras())).thenReturn(Optional.of(produtoExistente));

        // Act
        CodigoJaExisteException exception = assertThrows(
                CodigoJaExisteException.class,
                () -> produtoService.registrarProduto(requestDTO)
        );

        // Assert
        assertEquals("Já existe um produto cadastrado com o código de barras: " + requestDTO.codigoBarras(), exception.getMessage());

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve listar apenas os produtos em alerta convertendo para DTO")
    void listarProdutosEmAlerta_DeveRetornarListaDeAlertas() {
        // Arrange
        Produto produtoEmAlerta = ProdutoCreator.criarProdutoNovo();
        produtoEmAlerta.setEstoqueEmAlerta(true);
        produtoEmAlerta.setQuantidadeEstoque(new BigDecimal("5.0"));

        when(produtoRepository.findByEstoqueEmAlertaTrue()).thenReturn(List.of(produtoEmAlerta));

        // Act
        var listaDeAlertas = produtoService.listarProdutosEmAlerta();

        // Assert
        assertNotNull(listaDeAlertas);
        assertEquals(1, listaDeAlertas.size());
        assertEquals(0, listaDeAlertas.getFirst().quantidadeEstoque().compareTo(new BigDecimal("5.0")));
        verify(produtoRepository, times(1)).findByEstoqueEmAlertaTrue();
    }
}
