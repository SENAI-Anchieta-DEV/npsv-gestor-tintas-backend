package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.FormulaService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/formulas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Formulas", description = "Endpoints para gerenciamento de formulas")
public class FormulaController {
    private final FormulaService service;

    @Operation(summary = "Registrar uma nova fórmula", description = "Cria uma nova fórmula com as informações fornecidas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fórmula criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<FormulaResponseDTO> registrarFormula(@Valid @RequestBody FormulaRequestDTO dto) {
        FormulaResponseDTO response = service.registrarFormula(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Listar todas as fórmulas ativas", description = "Retorna uma lista de fórmulas ativas.")
    @ApiResponse(responseCode = "200", description = "Lista de fórmulas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FormulaResponseDTO>> listarFormulas() { return ResponseEntity.ok(service.listarFormulas()); }

    @Operation(summary = "Listar fórmula por ID", description = "Retorna os detalhes de uma fórmula específica pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fórmula encontrada e retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fórmula não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> listarFormulaPorId(@PathVariable String id) { return ResponseEntity.ok(service.listarFormulaPorId(id)); }

    @Operation(summary = "Atualizar uma fórmula", description = "Atualiza os detalhes de uma fórmula existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fórmula atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Fórmula não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<FormulaResponseDTO> atualizarFormula(@PathVariable String id, @Valid @RequestBody FormulaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarFormula(id, dto));
    }

    @Operation(summary = "Deletar uma fórmula", description = "Remove uma fórmula existente pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fórmula deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fórmula não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFormula(@PathVariable String id) {
        service.deletarFormula(id);
        return ResponseEntity.noContent().build();
    }
}