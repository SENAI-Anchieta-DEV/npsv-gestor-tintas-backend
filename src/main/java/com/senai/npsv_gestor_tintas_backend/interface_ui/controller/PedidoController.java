package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.service.PedidoService;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
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
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> registrarPedido(@Valid @RequestBody PedidoRequestDTO dto) {
        PedidoResponseDTO response = pedidoService.registrarPedido(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) String fornecedorId) {
        return ResponseEntity.ok(pedidoService.listarPedidos(status, fornecedorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> listarPedidoPorId(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.listarPedidoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> atualizarPedido(
            @PathVariable String id,
            @Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.ok(pedidoService.atualizarPedido(id, dto));
    }

    @PatchMapping("/{id}/enviar")
    public ResponseEntity<PedidoResponseDTO> enviarPedido(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.enviarPedido(id));
    }

    @PatchMapping("/{id}/receber")
    public ResponseEntity<PedidoResponseDTO> receberPedido(@PathVariable String id) {
        return ResponseEntity.ok(pedidoService.receberPedido(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable String id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
