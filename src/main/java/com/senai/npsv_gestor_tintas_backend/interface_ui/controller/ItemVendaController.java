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

    @GetMapping
    public ResponseEntity<List<ItemVendaResponseDTO>> listarItensVenda() {
        List<ItemVendaResponseDTO> lista = service.listarTodos().stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemVendaResponseDTO> listarItemVendaPorId(@PathVariable String id) {
        var item = service.buscarPorId(id);
        return ResponseEntity.ok(ItemVendaResponseDTO.fromEntity(item));
    }

    @GetMapping("/venda/{vendaId}")
    public ResponseEntity<List<ItemVendaResponseDTO>> listarItemVendaPorVenda(@PathVariable String vendaId) {
        List<ItemVendaResponseDTO> lista = service.listarPorVenda(vendaId).stream()
                .map(ItemVendaResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }
}