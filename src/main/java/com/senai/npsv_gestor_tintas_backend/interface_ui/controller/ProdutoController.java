package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.produto.AlertaEstoqueResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.produto.ProdutoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.produto.ProdutoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ProdutoService;
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
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService service;

    @Operation(summary = "Registrar um novo produto", description = "Cria um novo produto com as informações fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> registrarProduto(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO response = service.registrarProduto(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Listar produtos", description = "Retorna uma lista de produtos.")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarProdutos() { return ResponseEntity.ok(service.listarProdutos()); }

    @Operation(
            summary = "Listar produtos com estoque baixo em alerta",
            description = "Retorna uma lista de produtos cujo estoque está abaixo do limite definido, indicando que precisam ser reabastecidos."
    )
    @ApiResponse(responseCode = "200", description = "Lista de produtos em alerta de estoque retornada com sucesso")
    @GetMapping("/alertas")
    public ResponseEntity<List<AlertaEstoqueResponseDTO>> listarProdutosEmAlerta() {
        return ResponseEntity.ok(service.listarProdutosEmAlerta());
    }

    @Operation(summary = "Listar um produto por ID", description = "Retorna os detalhes de um produto específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> listarProdutoPorId(@PathVariable String id) { return ResponseEntity.ok(service.listarProdutoPorId(id)); }

    @Operation(summary = "Atualizar um produto", description = "Atualiza as informações de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable String id, @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarProduto(id, dto));
    }

    @Operation(summary = "Deletar um produto", description = "Deleta um produto do sistema pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable String id) {
        service.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
