package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ProducaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/producoes")
@CrossOrigin(origins = "http://localhost:1234", originPatterns = "http://localhost:56135")
@RequiredArgsConstructor
public class ProducaoController {
    private final ProducaoService producaoService;

    @PostMapping
    public ResponseEntity<ProducaoResponseDTO> iniciarProducao(@Valid @RequestBody ProducaoRequestDTO dto) {
        ProducaoResponseDTO response = producaoService.iniciarProducao(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProducaoResponseDTO>> listarProducoesAtivas() {
        return ResponseEntity.ok(producaoService.listarProducoesAtivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducaoResponseDTO> listarProducaoPorId(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.listarProducaoPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarProducao(@PathVariable String id) {
        producaoService.cancelarProducao(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/perda-total")
    public ResponseEntity<ProducaoResponseDTO> registrarPerdaTotal(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.registrarPerdaTotal(id));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<ProducaoResponseDTO> concluirProducao(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.concluirProducao(id));
    }
}