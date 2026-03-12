package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public ProdutoResponseDTO registrarProduto(ProdutoRequestDTO dto) {
        Produto produto = dto.toEntity();
        produto.setCategoria(buscarCategoriaProdutoPorId(dto.categoriaId()));
        return ProdutoResponseDTO.fromEntity(produtoRepository.save(produto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'COLORISTA')")
    public List<ProdutoResponseDTO> listarProdutos() {
        return produtoRepository.findAll().stream().map(ProdutoResponseDTO::fromEntity).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'COLORISTA')")
    public ProdutoResponseDTO listarProdutoPorId(String id) {
        Produto produto = buscarProdutoPorId(id);
        return ProdutoResponseDTO.fromEntity(produto);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarProduto(String id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarProdutoPorId(String id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    private CategoriaProduto buscarCategoriaProdutoPorId(String id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria de produto não encontrada."));
    }
}
