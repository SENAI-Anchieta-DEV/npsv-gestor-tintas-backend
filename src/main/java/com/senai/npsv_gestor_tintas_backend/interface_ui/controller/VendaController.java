package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
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
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<VendaResponseDTO> concluirVenda(
            @PathVariable String id,
            @Valid @RequestBody ConcluirVendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.concluirVenda(id, dto));
    }
}