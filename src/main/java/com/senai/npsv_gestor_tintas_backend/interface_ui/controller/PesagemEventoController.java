package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.PesagemEventoService;
import com.senai.npsv_gestor_tintas_backend.infrastructure.openapi.ApiErrorResponseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pesagem-eventos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Pesagem de Eventos", description = "Endpoints relacionados à pesagem de eventos, incluindo criação, listagem e consulta por ID.")
public class PesagemEventoController {
    private final PesagemEventoService service;

    @Operation(summary = "Cria um novo evento de pesagem", description = "Recebe os dados do evento de pesagem e cria um novo registro no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Evento de pesagem criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes ou formato inválido)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<PesagemEventoResponseDTO> registrarPesagemEvento(@Valid @RequestBody PesagemEventoRequestDTO dto) {
        return ResponseEntity.ok(service.registrarPesagemEvento(dto));
    }
}