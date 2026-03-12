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
    public ResponseEntity<CategoriaProdutoResponseDTO> registrarCategoriaProduto(@Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        CategoriaProdutoResponseDTO response = service.registrarCategoriaProduto(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> listarCategoriasProdutos() { return ResponseEntity.ok(service.listarCategoriasProdutos()); }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> listarCategoriaProdutoPorId(@PathVariable String id) { return ResponseEntity.ok(service.listarCategoriaProdutoPorId(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> atualizarCategoriaProduto(@PathVariable String id, @Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarCategoriaProduto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoriaProduto(@PathVariable String id) {
        service.deletarCategoriaProduto(id);
        return ResponseEntity.noContent().build();
    }
}
