package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.venda.AtualizarVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.venda.ConcluirVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.venda.IniciarVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.venda.VendaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Vendas", description = "Endpoints para gerenciamento de vendas, incluindo criação, listagem e consulta por ID.")
public class VendaController {

    private final VendaService vendaService;

    @Operation(summary = "Registrar uma nova venda", description = "Cria uma nova venda com as informações fornecidas, incluindo o cliente, forma de pagamento e itens vendidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venda criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes ou formato inválido)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping("/iniciar")
    public ResponseEntity<VendaResponseDTO> iniciarVenda(@Valid @RequestBody IniciarVendaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.iniciarVenda(dto));
    }

    @Operation(summary = "Listar todas as vendas", description = "Retorna uma lista de todas as vendas registradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de vendas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarVendas());
    }

    @Operation(summary = "Consultar venda por ID", description = "Retorna os detalhes de uma venda específica pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda encontrada e retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<VendaResponseDTO> listarVendaPorId(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.listarVendaPorId(id));
    }

    @Operation(summary = "Listar vendas por vendedor", description = "Retorna uma lista de vendas associadas a um vendedor específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de vendas retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vendedor não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<VendaResponseDTO>> listarVendasPorVendedor(@PathVariable String vendedorId) {
        return ResponseEntity.ok(vendaService.listarVendasPorVendedor(vendedorId));
    }

    @Operation(summary = "Atualizar venda", description = "Atualiza os detalhes de uma venda existente, como forma de pagamento ou itens vendidos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes ou formato inválido)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> atualizarVenda(
            @PathVariable String id,
            @Valid @RequestBody AtualizarVendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.atualizarVenda(id, dto));
    }

    @Operation(summary = "Concluir uma venda existente", description = "Marca uma venda como concluída, finalizando o processo de venda e registrando a transação.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda concluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<VendaResponseDTO> concluirVenda(
            @PathVariable String id,
            @Valid @RequestBody ConcluirVendaRequestDTO dto) {
        return ResponseEntity.ok(vendaService.concluirVenda(id, dto));
    }

    @Operation(summary = "Deleta uma venda", description = "Deleta uma venda do banco de dados, caso aberta.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Venda deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable String id) {
        vendaService.deletarVenda(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Estornar uma venda", description = "Estorna uma venda concluída, revertendo o processo de venda")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venda estornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/estornar")
    public ResponseEntity<VendaResponseDTO> estornarVenda(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.estornarVenda(id));
    }
}