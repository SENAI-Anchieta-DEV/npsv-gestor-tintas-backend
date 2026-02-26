package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO novoUsuario = service.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosAtivos() {
        return ResponseEntity.ok(service.listarUsuariosAtivos());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuarioPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.listarUsuarioPorEmail(email));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UsuarioResponseDTO> listarUsuarioPorId(@PathVariable String id) {
        return ResponseEntity.ok(service.listarUsuarioPorId(id));
    }

    @PutMapping ("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable String email,
                                                               @Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarUsuario(email, dto));
    }

    @PatchMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> atualizarSenhaUsuario(@PathVariable String email,
                                                                    @Valid @RequestBody String novaSenha) {
        return ResponseEntity.ok(service.atualizarSenhaUsuario(email, novaSenha));
    }

    @DeleteMapping ("/email/{email}")
    public ResponseEntity<Void> deletarUsuario(@Valid @PathVariable String email) {
        service.deletarUsuario(email);
        return ResponseEntity.noContent().build();
    }
}
