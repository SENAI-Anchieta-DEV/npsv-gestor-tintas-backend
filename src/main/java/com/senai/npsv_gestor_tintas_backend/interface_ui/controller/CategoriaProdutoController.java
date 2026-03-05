package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.CategoriaProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-produtos")
@RequiredArgsConstructor
public class CategoriaProdutoController {
    private final CategoriaProdutoService service;

    @PostMapping
    public ResponseEntity<CategoriaProdutoResponseDTO> definirNovaCategoria(@Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        CategoriaProdutoResponseDTO response = service.definirNovaCategoria(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> listarCategoriasDisponiveis() { return ResponseEntity.ok(service.listarCategoriasDisponiveis()); }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> obterDetalhesDaCategoria(@PathVariable String id) { return ResponseEntity.ok(service.obterDetalhesDaCategoria(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> atualizarDadosDaCategoria(@PathVariable String id, @Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarDadosDaCategoria(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCategoriaDoCatalogo(@PathVariable String id) {
        service.removerCategoriaDoCatalogo(id);
        return ResponseEntity.noContent().build();
    }
}