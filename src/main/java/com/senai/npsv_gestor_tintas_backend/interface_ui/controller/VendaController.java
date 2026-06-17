package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
public class VendaController {

    private final VendaService vendaService;

    @PostMapping("/iniciar")
    public ResponseEntity<VendaResponseDTO> iniciarVenda(@Valid @RequestBody IniciarVendaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.iniciarVenda(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarVendas());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<VendaResponseDTO> listarVendaPorId(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.listarVendaPorId(id));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<VendaResponseDTO>> listarVendasPorVendedor(@PathVariable String vendedorId) {
        return ResponseEntity.ok(vendaService.listarVendasPorVendedor(vendedorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> atualizarVenda(
            @PathVariable String id,
            @Valid @RequestBody AtualizarVendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.atualizarVenda(id, dto));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<VendaResponseDTO> concluirVenda(
            @PathVariable String id,
            @Valid @RequestBody ConcluirVendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.concluirVenda(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable String id) {
        vendaService.deletarVenda(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estornar")
    public ResponseEntity<VendaResponseDTO> estornarVenda(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.estornarVenda(id));
    }
}