package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaRepository;

    @Transactional
    public ProdutoResponseDTO registrarProduto(ProdutoRequestDTO dto) {
        Produto produto = dto.toEntity();
        produto.setCategoria(categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria informada não existe.")));
        return ProdutoResponseDTO.fromEntity(produtoRepository.save(produto));
    }

    public List<ProdutoResponseDTO> listarProdutos() {
        return produtoRepository.findAll().stream().map(ProdutoResponseDTO::fromEntity).toList();
    }

    public ProdutoResponseDTO listarProdutoPorId(String id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado no estoque."));
        return ProdutoResponseDTO.fromEntity(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(String id, ProdutoRequestDTO dto) {
        Produto produto = buscarProdutoPorId(id);
        produto.setDescricao(dto.descricao());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setCategoria(buscarCategoriaProdutoPorId(dto.categoriaId()));

        return ProdutoResponseDTO.fromEntity(produtoRepository.save(produto));
    }

    @Transactional
    public void deletarProduto(String id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarProdutoPorId(String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado."));
    }

    private CategoriaProduto buscarCategoriaProdutoPorId(String id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria de produto não encontrada."));
    }
}
