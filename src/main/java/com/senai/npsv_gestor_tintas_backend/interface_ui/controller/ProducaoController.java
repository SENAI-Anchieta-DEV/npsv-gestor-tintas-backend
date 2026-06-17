package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemAtualResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.PesagemMqttService;
import com.senai.npsv_gestor_tintas_backend.application.service.ProducaoService;
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
@RequestMapping("/api/producoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Produção", description = "Endpoints relacionados à produção, incluindo criação, listagem e consulta por ID.")
public class ProducaoController {

    private final ProducaoService producaoService;
    private final PesagemMqttService pesagemMqttService;

    @Operation(summary = "Cria uma nova produção", description = "Recebe os dados da produção e cria um novo registro no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produção criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes ou formato inválido)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ProducaoResponseDTO> iniciarProducao(@Valid @RequestBody ProducaoRequestDTO dto) {
        ProducaoResponseDTO response = producaoService.iniciarProducao(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Listar produções ativas", description = "Retorna uma lista de produções ativas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de produções ativas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ProducaoResponseDTO>> listarProducoesAtivas() {
        return ResponseEntity.ok(producaoService.listarProducoesAtivas());
    }

    @Operation(summary = "Consultar produção por ID", description = "Retorna os detalhes de uma produção específica pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produção encontrada e retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produção não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProducaoResponseDTO> listarProducaoPorId(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.listarProducaoPorId(id));
    }

    @Operation(summary = "Cancelar produção", description = "Cancela uma produção ativa pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produção cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produção não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarProducao(@PathVariable String id) {
        producaoService.cancelarProducao(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Registrar perda total na produção",
            description = "Registra uma perda total para uma produção, encerrando-a imediatamente e registrando baixa no estoque dos insumos utilizados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Perda total registrada e produção encerrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produção não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/perda-total")
    public ResponseEntity<ProducaoResponseDTO> registrarPerdaTotal(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.registrarPerdaTotal(id));
    }

    @Operation(summary = "Concluir produção", description = "Conclui uma produção ativa, registrando baixa no estoque dos insumos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produção concluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produção não encontrada",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<ProducaoResponseDTO> concluirProducao(@PathVariable String id) {
        return ResponseEntity.ok(producaoService.concluirProducao(id));
    }

    @Operation(
            summary = "Obter estado atual da pesagem",
            description = "Retorna o estado atual da pesagem para a Aba da Máquina. Consumido pelo frontend via polling a cada 500ms."
    )
    @ApiResponse(responseCode = "200", description = "Estado atual da pesagem retornado com sucesso")
    @GetMapping("/{id}/pesagem/atual")
    public ResponseEntity<PesagemAtualResponseDTO> getPesagemAtual(@PathVariable String id) {
        return ResponseEntity.ok(pesagemMqttService.getPesagemAtual(id));
    }

    @Operation(
            summary = "Obter histórico completo de pesagens",
            description = "Retorna o histórico completo de pesagens para auditoria."
    )
    @ApiResponse(responseCode = "200", description = "Histórico completo de pesagens retornado com sucesso")
    @GetMapping("/{id}/eventos-pesagem")
    public ResponseEntity<List<PesagemEventoResponseDTO>> getEventosPesagem(@PathVariable String id) {
        return ResponseEntity.ok(pesagemMqttService.getEventosPesagem(id));
    }
}