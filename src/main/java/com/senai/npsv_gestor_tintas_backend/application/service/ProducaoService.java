package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ProducaoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.exception.*;
import com.senai.npsv_gestor_tintas_backend.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UsuarioRepository usuarioRepository;
    private final FormulaRepository formulaRepository;
    private final PesagemEventoRepository pesagemEventoRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public ProducaoResponseDTO iniciarProducao(ProducaoRequestDTO dto) {
        Producao producao = dto.toEntity();
        producao.setColorista(usuarioRepository.findByIdAndAtivoTrue(dto.coloristaId())
                .orElseThrow(() -> new RuntimeException("Colorista não encontrado ou inativo.")));
        producao.setFormula(formulaRepository.findById(dto.formulaId())
                .orElseThrow(() -> new RuntimeException("Fórmula não encontrada no sistema.")));

        producao.setDataHora(LocalDateTime.now());
        producao.setStatus(StatusProducao.PENDENTE);

        return ProducaoResponseDTO.fromEntity(producaoRepository.save(producao));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA', 'VENDEDOR')")
    public List<ProducaoResponseDTO> listarProducoesAtivas() {
        return producaoRepository.findAll().stream()
                .filter(p -> p.getStatus() != StatusProducao.CANCELADO)
                .map(ProducaoResponseDTO::fromEntity).toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA', 'VENDEDOR')")
    public ProducaoResponseDTO listarProducaoPorId(String id) {
        Producao producao = buscarProducaoPorId(id);
        return ProducaoResponseDTO.fromEntity(producao);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public void cancelarProducao(String id) {
        Producao producao = buscarProducaoPorId(id);

        if (producao.getStatus() != StatusProducao.PENDENTE) {
            throw new RegraNegocioException(
                    "Apenas ordens PENDENTES podem ser canceladas. Se a mistura já foi iniciada, utilize o registro de PERDA TOTAL.",
                    null
            );
        }

        producao.setStatus(StatusProducao.CANCELADO);
        producaoRepository.save(producao);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public ProducaoResponseDTO registrarPerdaTotal(String id) {
        Producao producao = validarProducao(id);

        producao.setStatus(StatusProducao.PERDA_TOTAL);
        Producao producaoSalva = producaoRepository.save(producao);

        darBaixaEstoqueProducao(producaoSalva);

        return ProducaoResponseDTO.fromEntity(producaoSalva);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public ProducaoResponseDTO concluirProducao(String id) {
        Producao producao = validarProducao(id);

        producao.setStatus(StatusProducao.CONCLUIDO);
        Producao producaoSalva = producaoRepository.save(producao);

        darBaixaEstoqueProducao(producaoSalva);

        return ProducaoResponseDTO.fromEntity(producaoSalva);
    }

    private void darBaixaEstoqueProducao(Producao producao) {
        log.info("--- INÍCIO DA BAIXA DE ESTOQUE (PRODUÇÃO {}) ---", producao.getId());
        List<ItemFormula> insumosNecessarios = producao.getFormula().getItens();

        for (ItemFormula item : insumosNecessarios) {
            Produto insumo = item.getInsumo();
            BigDecimal qtdNecessaria = item.getQuantidadeNecessaria();

            int linhasAfetadas = produtoRepository.darBaixaEstoque(insumo.getId(), qtdNecessaria);
            boolean possuiEstoqueSuficiente = linhasAfetadas > 0;

            if (!possuiEstoqueSuficiente) {
                throw new EstoqueInsuficienteException(String.format(
                        "Estoque insuficiente para o insumo '%s'.",
                        insumo.getDescricao()),
                        "RN02 – Baixa de Estoque");
            }
        }
        log.info("--- FIM DA BAIXA DE ESTOQUE ---");
    }

    private Producao validarProducao(String id) {
        Producao producao = buscarProducaoPorId(id);

        if (producao.getStatus() != StatusProducao.PENDENTE && producao.getStatus() != StatusProducao.PROCESSANDO) {
            throw new TransicaoDeStatusInvalidaException("Não é possível concluir uma produção que está no status: " + producao.getStatus());
        }

        if (!pesagemEventoRepository.existsByProducaoId(producao.getId())) {
            throw new ProducaoSemPesagemException("A produção não pode ser concluída pois não possui nenhum evento de pesagem registrado.");
        }
        return producao;
    }

    private Producao buscarProducaoPorId(String id) {
        return producaoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de produção não encontrada."));
    }
}