package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.CategoriaProduto;
import com.senai.npsv_gestor_tintas_backend.domain.repository.CategoriaProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaProdutoService {
    private final CategoriaProdutoRepository repository;

    @Transactional
    public CategoriaProdutoResponseDTO registrarCategoriaProduto(CategoriaProdutoRequestDTO dto) {
        CategoriaProduto categoria = repository.save(dto.toEntity());
        return CategoriaProdutoResponseDTO.fromEntity(categoria);
    }

    public List<CategoriaProdutoResponseDTO> listarCategoriasProdutos() {
        return repository.findAll().stream().map(CategoriaProdutoResponseDTO::fromEntity).toList();
    }

    public CategoriaProdutoResponseDTO listarCategoriaProdutoPorId(String id) {
        CategoriaProduto categoria = buscarCategoriaProdutoPorId(id);
        return CategoriaProdutoResponseDTO.fromEntity(categoria);
    }

    @Transactional
    public CategoriaProdutoResponseDTO atualizarCategoriaProduto(String id, CategoriaProdutoRequestDTO dto) {
        CategoriaProduto existente = buscarCategoriaProdutoPorId(id);
        existente.setNome(dto.nome());
        existente.setDescricao(dto.descricao());
        return CategoriaProdutoResponseDTO.fromEntity(repository.save(existente));
    }

    @Transactional
    public void deletarCategoriaProduto(String id) {
        repository.delete(buscarCategoriaProdutoPorId(id));
    }

    private CategoriaProduto buscarCategoriaProdutoPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria de produto não encontrada."));
    }
}
