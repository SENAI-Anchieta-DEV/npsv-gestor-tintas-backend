package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.usuario.AtualizarUsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.usuario.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.usuario.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-jwt")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService service;

    @Operation(summary = "Registrar um novo usuário", description = "Cria um novo usuário com as informações fornecidas.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO novoUsuario = service.registrarUsuario(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoUsuario.id())
                .toUri();

        return ResponseEntity.created(uri).body(novoUsuario);
    }

    @Operation(summary = "Listar usuários ativos", description = "Retorna uma lista de usuários ativos.")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosAtivos() {
        return ResponseEntity.ok(service.listarUsuariosAtivos());
    }

    @Operation(summary = "Listar um usuário por email", description = "Retorna os detalhes de um usuário específico pelo seu email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.listarUsuarioPorEmail(email));
    }

    @Operation(summary = "Listar um usuário por ID", description = "Retorna os detalhes de um usuário específico pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado e retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuarioPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.listarUsuarioPorId(id));
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza as informações de um usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PutMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable String email,
                                                               @Valid @RequestBody AtualizarUsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarUsuario(email, dto));
    }

    @Operation(summary = "Atualiza a senha de um usuário", description = "Atualiza a senha de um usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex): campos obrigatórios ausentes)",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @PatchMapping("/email/{email}/senha")
    public ResponseEntity<UsuarioResponseDTO> atualizarSenhaUsuario(@PathVariable String email,
                                                                    @Valid @RequestBody String novaSenha) {
        return ResponseEntity.ok(service.atualizarSenhaUsuario(email, novaSenha));
    }

    @Operation(summary = "Deleta um usuário", description = "Deleta um usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ApiErrorResponseSchema.class)))
    })
    @DeleteMapping ("/email/{email}")
    public ResponseEntity<Void> deletarUsuario(@Valid @PathVariable String email) {
        service.deletarUsuario(email);
        return ResponseEntity.noContent().build();
    }
}
