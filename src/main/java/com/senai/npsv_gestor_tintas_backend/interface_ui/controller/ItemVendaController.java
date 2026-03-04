package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemVendaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ItemVendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-venda")
@RequiredArgsConstructor
public class ItemVendaController {

    private final ItemVendaService service;

    // 1. Listar todos os itens vendidos (Geral)
    @GetMapping
    public ResponseEntity<List<ItemVendaResponseDTO>> listarTodos() {
        List<ItemVendaResponseDTO> lista = service.listarTodos().stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // 2. Buscar um item específico por ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemVendaResponseDTO> buscarPorId(@PathVariable String id) {
        var item = service.buscarPorId(id);
        return ResponseEntity.ok(ItemVendaResponseDTO.fromEntity(item));
    }

    // 3. Listar itens de uma Venda específica (Ex: /api/itens-venda/venda/ID-DA-VENDA)
    @GetMapping("/venda/{vendaId}")
    public ResponseEntity<List<ItemVendaResponseDTO>> listarPorVenda(@PathVariable String vendaId) {
        List<ItemVendaResponseDTO> lista = service.listarPorVenda(vendaId).stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }
}