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
@CrossOrigin(origins = "http://localhost:1234", originPatterns = "http://localhost:56135")
@RequiredArgsConstructor
public class FormulaController {
    private final FormulaService service;

    @PostMapping
    public ResponseEntity<FormulaResponseDTO> criarNovaReceitaDeFormula(@Valid @RequestBody FormulaRequestDTO dto) {
        FormulaResponseDTO response = service.criarNovaReceitaDeFormula(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FormulaResponseDTO>> listarReceitasDeFormulas() { return ResponseEntity.ok(service.listarReceitasDeFormulas()); }

    @GetMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> consultarEspecificacoesDaFormula(@PathVariable String id) { return ResponseEntity.ok(service.consultarEspecificacoesDaFormula(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> alterarEspecificacoesDaFormula(@PathVariable String id, @Valid @RequestBody FormulaRequestDTO dto) {
        return ResponseEntity.ok(service.alterarEspecificacoesDaFormula(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarReceitaDeFormula(@PathVariable String id) {
        service.inativarReceitaDeFormula(id);
        return ResponseEntity.noContent().build();
    }
}