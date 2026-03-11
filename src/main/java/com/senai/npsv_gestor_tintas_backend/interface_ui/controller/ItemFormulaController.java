package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ItemFormulaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens-formula")
@RequiredArgsConstructor
public class ItemFormulaController {
    private final ItemFormulaService service;

    @PostMapping
    public ResponseEntity<ItemFormulaResponseDTO> adicionarInsumoNaReceitaDaFormula(@Valid @RequestBody ItemFormulaRequestDTO dto) {
        return ResponseEntity.ok(service.adicionarInsumoNaReceitaDaFormula(dto));
    }

    @GetMapping("/formula/{formulaId}")
    public ResponseEntity<List<ItemFormulaResponseDTO>> consultarInsumosDaReceita(@PathVariable String formulaId) {
        return ResponseEntity.ok(service.consultarInsumosDaReceita(formulaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerInsumoDaReceita(@PathVariable String id) {
        service.removerInsumoDaReceita(id);
        return ResponseEntity.noContent().build();
    }
}