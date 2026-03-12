package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.PesagemEventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pesagem-eventos")
@CrossOrigin(origins = "http://localhost:1234", originPatterns = "http://localhost:56135")
@RequiredArgsConstructor
public class PesagemEventoController {
    private final PesagemEventoService service;

    @PostMapping
    public ResponseEntity<PesagemEventoResponseDTO> registrarLeituraDoSensorIot(@Valid @RequestBody PesagemEventoRequestDTO dto) {
        return ResponseEntity.ok(service.registrarLeituraDoSensorIot(dto));
    }
}