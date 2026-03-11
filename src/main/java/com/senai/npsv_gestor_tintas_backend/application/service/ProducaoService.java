package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ItemFormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducaoService {
    private final ProducaoRepository producaoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemFormulaRepository itemFormulaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FormulaRepository formulaRepository;

    @Transactional
    public ProducaoResponseDTO iniciarOrdemDeProducao(ProducaoRequestDTO dto) {
        Producao producao = dto.toEntity();
        producao.setColorista(usuarioRepository.findByIdAndAtivoTrue(dto.coloristaId())
                .orElseThrow(() -> new RuntimeException("Colorista não encontrado ou inativo.")));
        producao.setFormula(formulaRepository.findById(dto.formulaId())
                .orElseThrow(() -> new RuntimeException("Fórmula não encontrada no sistema.")));

        producao.setDataHora(LocalDateTime.now());
        producao.setStatus(StatusProducao.PENDENTE);

        return ProducaoResponseDTO.fromEntity(producaoRepository.save(producao));
    }

    public List<ProducaoResponseDTO> consultarOrdensDeProducaoAtivas() {
        return producaoRepository.findAll().stream()
                .filter(p -> p.getStatus() != StatusProducao.CANCELADO)
                .map(ProducaoResponseDTO::fromEntity).toList();
    }

    public ProducaoResponseDTO consultarDetalhesDaOrdem(String id) {
        Producao producao = buscarProducaoPorId(id);
        return ProducaoResponseDTO.fromEntity(producao);
    }

    @Transactional
    public void cancelarOrdemDeProducao(String id) {
        Producao producao = buscarProducaoPorId(id);
        producao.setStatus(StatusProducao.CANCELADO);
        producaoRepository.save(producao);
    }

    @Transactional
    public ProducaoResponseDTO concluirProcessoDeProducao(String id) {
        Producao producao = buscarProducaoPorId(id);

        if (producao.getStatus() == StatusProducao.CONCLUIDO) {
            throw new RuntimeException("Esta ordem de produção já foi finalizada anteriormente.");
        }

        producao.setStatus(StatusProducao.CONCLUIDO);
        Producao producaoSalva = producaoRepository.save(producao);

        deduzirInsumosDoEstoqueGlobal(producaoSalva);

        return ProducaoResponseDTO.fromEntity(producaoSalva);
    }

    private void deduzirInsumosDoEstoqueGlobal(Producao producao) {
        log.info("--- INÍCIO DA BAIXA DE ESTOQUE (PRODUÇÃO {}) ---", producao.getId());
        List<ItemFormula> insumosNecessarios = itemFormulaRepository.findByFormulaId(producao.getFormula().getId());

        for (ItemFormula item : insumosNecessarios) {
            Produto insumo = item.getInsumo();
            BigDecimal qtdNecessaria = item.getQuantidadeNecessaria();
            BigDecimal estoqueAtual = insumo.getQuantidadeEstoque();

            if (estoqueAtual.compareTo(qtdNecessaria) < 0) {
                throw new RuntimeException("Estoque insuficiente para o insumo: " + insumo.getDescricao());
            }

            insumo.setQuantidadeEstoque(estoqueAtual.subtract(qtdNecessaria));
            produtoRepository.save(insumo);
        }
        log.info("--- FIM DA BAIXA DE ESTOQUE ---");
    }

    private Producao buscarProducaoPorId(String id) {
        return producaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Ordem de produção não encontrada."));
    }
}