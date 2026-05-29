package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.CategoriaProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.CategoriaProdutoService;
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
@RequestMapping("/api/categorias-produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Categorias de Produtos", description = "Endpoints para gestão do ciclo de vida das categorias de produtos")
public class CategoriaProdutoController {
    private final CategoriaProdutoService service;

    @Operation(summary = "Registra uma nova categoria de produto", description = "Cria uma categoria de produto ativa na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria de produto criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<CategoriaProdutoResponseDTO> registrarCategoriaProduto(@Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        CategoriaProdutoResponseDTO response = service.registrarCategoriaProduto(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Lista todas as categorias de produtos", description = "Recupera uma lista de todas as categorias de produtos cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias de produtos recuperada com sucesso")
    @GetMapping
    public ResponseEntity<List<CategoriaProdutoResponseDTO>> listarCategoriasProdutos() { return ResponseEntity.ok(service.listarCategoriasProdutos()); }

    @Operation(summary = "Busca categoria de produto por ID", description = "Retorna os detalhes de uma categoria de produto específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria de produto encontrada"),
            @ApiResponse(responseCode = "404", description = "Categoria de produto não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> listarCategoriaProdutoPorId(
            @Parameter(description = "ID da categoria de produtos") @PathVariable String id) {
        return ResponseEntity.ok(service.listarCategoriaProdutoPorId(id));
    }

    @Operation(summary = "Atualiza uma categoria de produto", description = "Atualiza os dados de uma categoria de produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria de produto atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Categoria de produto não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaProdutoResponseDTO> atualizarCategoriaProduto(@PathVariable String id, @Valid @RequestBody CategoriaProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarCategoriaProduto(id, dto));
    }

    @Operation(summary = "Deleta uma categoria de produto", description = "Remove uma categoria de produto do sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria de produto deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria de produto não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoriaProduto(@PathVariable String id) {
        service.deletarCategoriaProduto(id);
        return ResponseEntity.noContent().build();
    }
}
