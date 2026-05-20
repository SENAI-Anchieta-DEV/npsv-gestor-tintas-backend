package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.FornecedorRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FornecedorResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.FornecedorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> registrarFornecedor(@Valid @RequestBody FornecedorRequestDTO dto) {
        FornecedorResponseDTO response = fornecedorService.registrarFornecedor(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listarFornecedores(
            @RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(fornecedorService.listarFornecedores(ativo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> listarFornecedorPorId(@PathVariable String id) {
        return ResponseEntity.ok(fornecedorService.listarFornecedorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(
            @PathVariable String id,
            @Valid @RequestBody FornecedorRequestDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizarFornecedor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(@PathVariable String id) {
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarFornecedor(@PathVariable String id) {
        fornecedorService.desativarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
