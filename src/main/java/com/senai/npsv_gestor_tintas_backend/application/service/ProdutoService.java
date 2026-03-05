package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
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
    public ProdutoResponseDTO registrarNovoProdutoNoCatalogo(ProdutoRequestDTO dto) {
        Produto produto = dto.toEntity();
        produto.setCategoria(categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria informada não existe.")));
        return ProdutoResponseDTO.fromEntity(produtoRepository.save(produto));
    }

    public List<ProdutoResponseDTO> consultarCatalogoDeEstoque() {
        return produtoRepository.findAll().stream().map(ProdutoResponseDTO::fromEntity).toList();
    }

    public ProdutoResponseDTO consultarFichaTecnicaDoProduto(String id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque."));
        return ProdutoResponseDTO.fromEntity(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarFichaTecnicaDoProduto(String id, ProdutoRequestDTO dto) {
        Produto existente = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));
        existente.setDescricao(dto.descricao());
        existente.setPrecoCusto(dto.precoCusto());
        existente.setPrecoVenda(dto.precoVenda());
        existente.setQuantidadeEstoque(dto.quantidadeEstoque());
        existente.setUnidadeMedida(dto.unidadeMedida());
        existente.setCategoria(categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Nova categoria informada não existe.")));

        return ProdutoResponseDTO.fromEntity(produtoRepository.save(existente));
    }

    @Transactional
    public void removerProdutoDoCatalogo(String id) {
        produtoRepository.delete(produtoRepository.findById(id).orElseThrow(RuntimeException::new));
    }
}