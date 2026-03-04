package com.senai.npsv_gestor_tintas_backend.interface_ui.controller;

import com.senai.npsv_gestor_tintas_backend.application.service.ProducaoService;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/producoes")
@RequiredArgsConstructor
public class ProducaoController {

    private final ProducaoService producaoService;

    @PostMapping
    public ResponseEntity<Producao> criarProducao(@RequestBody Producao producao) {
        Producao novaProducao = producaoService.iniciarProducao(producao);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaProducao.getId())
                .toUri();

        return ResponseEntity.created(uri).body(novaProducao);
    }

    @GetMapping
    public ResponseEntity<List<Producao>> listarTodas() {
        List<Producao> producoes = producaoService.buscarTodas();
        return ResponseEntity.ok(producoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producao> buscarPorId(@PathVariable String id) {
        Producao producao = producaoService.buscarPorId(id);
        return ResponseEntity.ok(producao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producao> atualizarProducao(@PathVariable String id, @RequestBody Producao producao) {
        Producao producaoAtualizada = producaoService.atualizar(id, producao);
        return ResponseEntity.ok(producaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProducao(@PathVariable String id) {
        producaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Producao> concluirProducao(@PathVariable String id) {
        Producao producaoConcluida = producaoService.concluirProducaoSimulada(id);
        return ResponseEntity.ok(producaoConcluida);
    }
}