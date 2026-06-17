package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.ItemPedidoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.PedidoService;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Registrar um novo pedido", description = "Cria um novo pedido com as informações fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> registrarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO response = pedidoService.registrarPedido(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Listar pedidos com paginacao", description = "Retorna uma lista paginada de pedidos, com opções de filtro por status e cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de consulta inválidos",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) String fornecedorId) {
        return ResponseEntity.ok(pedidoService.listarPedidos(status, fornecedorId));
    }

    @Operation(summary = "Listar um pedido por ID", description = "Retorna os detalhes de um pedido específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> listarPedidoPorId(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.listarPedidoPorId(id));
    }

    @Operation(summary = "Listar os itens de um pedido", description = "Retorna a lista de itens associados a um pedido específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Itens do pedido retornados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ItemPedidoResponseDTO>> listarItensDePedido(@PathVariable String id) {
        List<ItemPedidoResponseDTO> itens = pedidoService.listarPedidoPorId(id).itens();
        return ResponseEntity.ok(itens);
    }

    @Operation(summary = "Atualizar pedido", description = "Atualiza as informações de um pedido existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizarPedido(
            @PathVariable String id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizarPedido(id, dto));
    }

    @Operation(summary = "Enviar pedido", description = "Envia um pedido para processamento, alterando seu status para 'ENVIADO'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido enviado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/enviar")
    public ResponseEntity<PedidoResponseDTO> enviarPedido(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.enviarPedido(id));
    }

    @Operation(summary = "Receber pedido", description = "Marca um pedido como recebido, alterando seu status para 'RECEBIDO'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido recebido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/receber")
    public ResponseEntity<PedidoResponseDTO> receberPedido(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.receberPedido(id));
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido, alterando seu status para 'CANCELADO'.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable String id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
