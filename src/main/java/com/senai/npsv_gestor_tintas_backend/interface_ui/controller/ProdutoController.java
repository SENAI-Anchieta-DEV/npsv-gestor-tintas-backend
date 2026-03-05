package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> registrarNovoProdutoNoCatalogo(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO response = service.registrarNovoProdutoNoCatalogo(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> consultarCatalogoDeEstoque() { return ResponseEntity.ok(service.consultarCatalogoDeEstoque()); }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> consultarFichaTecnicaDoProduto(@PathVariable String id) { return ResponseEntity.ok(service.consultarFichaTecnicaDoProduto(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarFichaTecnicaDoProduto(@PathVariable String id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarFichaTecnicaDoProduto(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProdutoDoCatalogo(@PathVariable String id) {
        service.removerProdutoDoCatalogo(id);
        return ResponseEntity.noContent().build();
    }
}