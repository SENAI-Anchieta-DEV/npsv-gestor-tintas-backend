package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducaoService {

    private final ProducaoRepository producaoRepository;

    @Transactional
    public Producao iniciarProducao(Producao producao) {
        producao.setDataHora(LocalDateTime.now());
        producao.setStatus(StatusProducao.PENDENTE);
        return producaoRepository.save(producao);
    }

    public List<Producao> buscarTodas() {
        return producaoRepository.findAll();
    }

    public Producao buscarPorId(String id) {
        return producaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produção não encontrada com o ID: " + id));
    }

    @Transactional
    public Producao atualizar(String id, Producao producaoAtualizada) {
        Producao producaoExistente = buscarPorId(id);

        producaoExistente.setStatus(producaoAtualizada.getStatus());
        producaoExistente.setColorista(producaoAtualizada.getColorista());
        producaoExistente.setFormula(producaoAtualizada.getFormula());

        return producaoRepository.save(producaoExistente);
    }

    @Transactional
    public void deletar(String id) {
        Producao producaoExistente = buscarPorId(id);

        producaoExistente.setStatus(StatusProducao.CANCELADO);

        producaoRepository.save(producaoExistente);
    }

    @Transactional
    public Producao concluirProducaoSimulada(String producaoId) {
        Producao producao = buscarPorId(producaoId);

        producao.setStatus(StatusProducao.CONCLUIDO);
        Producao producaoSalva = producaoRepository.save(producao);

        simularBaixaEstoqueConsole(producaoSalva);

        return producaoSalva;
    }

    private void simularBaixaEstoqueConsole(Producao producao) {
        log.info("--- INÍCIO DA BAIXA DE ESTOQUE (PRODUÇÃO {}) ---", producao.getId());
        log.info("Efetuando subtração da quantidadeEstoque do Produto associado...");
        log.info("Estoque atualizado com sucesso! Novo estoque: [VALOR_SIMULADO]");
        log.info("--- FIM DA BAIXA DE ESTOQUE ---");
    }
}