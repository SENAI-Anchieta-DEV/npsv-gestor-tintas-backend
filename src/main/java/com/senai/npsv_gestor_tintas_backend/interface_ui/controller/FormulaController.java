package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.FormulaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/formulas")
@RequiredArgsConstructor
public class FormulaController {
    private final FormulaService service;

    @PostMapping
    public ResponseEntity<FormulaResponseDTO> registrarFormula(@Valid @RequestBody FormulaRequestDTO dto) {
        FormulaResponseDTO response = service.registrarFormula(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FormulaResponseDTO>> listarFormulas() { return ResponseEntity.ok(service.listarFormulas()); }

    @GetMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> listarFormulaPorId(@PathVariable String id) { return ResponseEntity.ok(service.listarFormulaPorId(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> atualizarFormula(@PathVariable String id, @Valid @RequestBody FormulaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarFormula(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFormula(@PathVariable String id) {
        service.deletarFormula(id);
        return ResponseEntity.noContent().build();
    }
}