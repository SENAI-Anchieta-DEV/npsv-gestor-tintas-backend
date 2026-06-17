package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor.FornecedorRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor.FornecedorResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.FornecedorService;
import com.senai.npsv_gestor_tintas_backend.infrastructure.openapi.ApiErrorResponseSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Fornecedores", description = "Endpoints para gestão do ciclo de vida dos fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Operation(summary = "Regista um novo fornecedor", description = "Cria um fornecedor ativo na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(
                    responseCode = "400", description = "Erro de validação (ex: campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: CNPJ já cadastrado)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> registrarFornecedor(@Valid @RequestBody FornecedorRequestDTO dto) {
        FornecedorResponseDTO response = fornecedorService.registrarFornecedor(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Lista todos os fornecedores", description = "Retorna uma lista de fornecedores. Permite filtragem por status (ativo/inativo).")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listarFornecedores(
            @Parameter(description = "Filtro opcional. True para listar apenas ativos, false para inativos.")
            @RequestParam(required = false) Boolean ativo) {
        return ResponseEntity.ok(fornecedorService.listarFornecedores(ativo));
    }

    @Operation(summary = "Busca fornecedor por ID", description = "Retorna os detalhes de um fornecedor específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> listarFornecedorPorId(
            @Parameter(description = "UUID do fornecedor") @PathVariable String id) {
        return ResponseEntity.ok(fornecedorService.listarFornecedorPorId(id));
    }

    @Operation(summary = "Atualiza um fornecedor", description = "Permite atualizar os dados de um fornecedor existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: CNPJ já cadastrado)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarFornecedor(
            @Parameter(description = "UUID do fornecedor") @PathVariable String id,
            @Valid @RequestBody FornecedorRequestDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizarFornecedor(id, dto));
    }

    @Operation(summary = "Deleta um fornecedor", description = "Deleta o fornecedor do banco de dados, caso não tenha pedidos associados.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: fornecedor possui pedidos associados)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFornecedor(
            @Parameter(description = "UUID do fornecedor") @PathVariable String id) {
        fornecedorService.deletarFornecedor(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Inativa um fornecedor", description = "Marca o fornecedor como inativo, impedindo novas associações, mas mantendo os dados para histórico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fornecedor inativado com sucesso)"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarFornecedor(
            @Parameter(description = "UUID do fornecedor") @PathVariable String id) {
        fornecedorService.desativarFornecedor(id);
        return ResponseEntity.noContent().build();
    }
}
