package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.VendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.VendaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponseDTO> criar(@Valid @RequestBody VendaRequestDTO dto) {
        VendaResponseDTO novaVenda = vendaService.criarVenda(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaVenda.id())
                .toUri();
        return ResponseEntity.created(uri).body(novaVenda);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listar() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }
}