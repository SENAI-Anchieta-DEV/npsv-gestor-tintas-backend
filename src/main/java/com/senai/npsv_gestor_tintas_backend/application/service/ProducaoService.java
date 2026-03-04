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

    // CREATE
    @Transactional
    public Producao iniciarProducao(Producao producao) {
        producao.setDataHora(LocalDateTime.now());
        producao.setStatus(StatusProducao.PENDENTE);
        return producaoRepository.save(producao);
    }

    // READ (Todos)
    public List<Producao> buscarTodas() {
        return producaoRepository.findAll();
    }

    // READ (Por ID)
    public Producao buscarPorId(String id) {
        return producaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produção não encontrada com o ID: " + id));
    }

    // UPDATE
    @Transactional
    public Producao atualizar(String id, Producao producaoAtualizada) {
        Producao producaoExistente = buscarPorId(id);

        // Atualizando os campos permitidos
        producaoExistente.setStatus(producaoAtualizada.getStatus());
        producaoExistente.setColorista(producaoAtualizada.getColorista());
        producaoExistente.setFormula(producaoAtualizada.getFormula());
        // A DataHora da criação geralmente não é alterada, mas se houver necessidade de nova data, pode adicionar aqui.

        return producaoRepository.save(producaoExistente);
    }

    // DELETE (Safe Delete / Exclusão Lógica)
    @Transactional
    public void deletar(String id) {
        Producao producaoExistente = buscarPorId(id);

        // Em vez de remover fisicamente do banco, alteramos o status para CANCELADO
        producaoExistente.setStatus(StatusProducao.CANCELADO);

        producaoRepository.save(producaoExistente);
    }

    // MÉTODO AUXILIAR DA TAREFA (NPSV-238) - Simulação de Conclusão e Baixa de Estoque
    @Transactional
    public Producao concluirProducaoSimulada(String producaoId) {
        Producao producao = buscarPorId(producaoId);

        producao.setStatus(StatusProducao.CONCLUIDO);
        Producao producaoSalva = producaoRepository.save(producao);

        // Gera o log exigido como evidência no ticket
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