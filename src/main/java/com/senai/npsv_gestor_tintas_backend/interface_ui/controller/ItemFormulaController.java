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
    public ResponseEntity<ItemFormulaResponseDTO> registrarItemFormula(@Valid @RequestBody ItemFormulaRequestDTO dto) {
        return ResponseEntity.ok(service.registrarItemFormula(dto));
    }

    @GetMapping("/formula/{formulaId}")
    public ResponseEntity<List<ItemFormulaResponseDTO>> listarItemFormulaPorId(@PathVariable String formulaId) {
        return ResponseEntity.ok(service.listarItemFormulaPorId(formulaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarItemFormula(@PathVariable String id) {
        service.deletarItemFormula(id);
        return ResponseEntity.noContent().build();
    }
}