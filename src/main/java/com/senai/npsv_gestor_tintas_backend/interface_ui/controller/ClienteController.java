package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.cliente.ClienteRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.cliente.ClienteResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Registrar um novo cliente", description = "Cria um novo cliente com as informações fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> registrarCliente(@Valid @RequestBody ClienteRequestDTO dto){
        ClienteResponseDTO response = clienteService.registrarCliente(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Listas todos os clientes ativos", description = "Retorna uma lista de clientes ativos.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientesAtivos() {
        return ResponseEntity.ok(clienteService.listarClientesAtivos());
    }

    @Operation(summary = "Listar cliente por ID", description = "Retorna os detalhes de um cliente específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> listarClientePorId(@PathVariable String id) {
        return ResponseEntity.ok(clienteService.listarClientePorId(id));
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza as informações de um cliente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable String id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(clienteService.atualizarCliente(id, dto));
    }

    @Operation(summary = "Deletar cliente", description = "Deleta um cliente existente do banco de dados caso não possua venda associada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "409", description = "Conflito: Cliente possui vendas associadas e não pode ser deletado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desativar cliente", description = "Desativa um cliente existente, impedindo que ele seja listado ou associado a novas vendas, mas mantendo seu histórico de vendas intacto.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarCliente(@PathVariable String id) {
        clienteService.desativarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
