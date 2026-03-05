package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.application.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping("/iniciar")
    public ResponseEntity<IniciarVendaResponseDTO> iniciarVenda(@Valid @RequestBody IniciarVendaRequestDTO dto) {
        IniciarVendaResponseDTO vendaIniciada = vendaService.iniciarVenda(dto);

        // Cria a URI para o cabeçalho Location (Boas práticas REST)
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/vendas/{id}")
                .buildAndExpand(vendaIniciada.id())
                .toUri();

        // Retorna HTTP 201 Created
        return ResponseEntity.created(uri).body(vendaIniciada);
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarVendas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<VendaResponseDTO> listarVendaPorId(@PathVariable String id) {
        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<VendaResponseDTO>> listarVendasPorVendedor(@PathVariable String vendedorId) {
        return ResponseEntity.ok(vendaService.listarPorVendedor(vendedorId));

    }
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<VendaResponseDTO> concluirVenda(
            @PathVariable String id,
            @Valid @RequestBody ConcluirVendaRequestDTO dto) {

        VendaResponseDTO vendaConcluida = vendaService.concluirVenda(id, dto);

        // Retorna HTTP 200 OK com os dados atualizados
        return ResponseEntity.ok(vendaConcluida);
    }
}