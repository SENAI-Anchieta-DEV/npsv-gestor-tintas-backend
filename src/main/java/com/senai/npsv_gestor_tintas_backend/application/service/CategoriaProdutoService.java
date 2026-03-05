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
    public CategoriaProdutoResponseDTO definirNovaCategoria(CategoriaProdutoRequestDTO dto) {
        CategoriaProduto categoria = repository.save(dto.toEntity());
        return CategoriaProdutoResponseDTO.fromEntity(categoria);
    }

    public List<CategoriaProdutoResponseDTO> listarCategoriasDisponiveis() {
        return repository.findAll().stream().map(CategoriaProdutoResponseDTO::fromEntity).toList();
    }

    public CategoriaProdutoResponseDTO obterDetalhesDaCategoria(String id) {
        CategoriaProduto categoria = repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada no catálogo."));
        return CategoriaProdutoResponseDTO.fromEntity(categoria);
    }

    @Transactional
    public CategoriaProdutoResponseDTO atualizarDadosDaCategoria(String id, CategoriaProdutoRequestDTO dto) {
        CategoriaProduto existente = repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
        existente.setNome(dto.nome());
        existente.setDescricao(dto.descricao());
        return CategoriaProdutoResponseDTO.fromEntity(repository.save(existente));
    }

    @Transactional
    public void removerCategoriaDoCatalogo(String id) {
        repository.delete(repository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada.")));
    }
}