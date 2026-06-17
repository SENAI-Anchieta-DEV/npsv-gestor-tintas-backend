package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.producao.PesagemEventoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.producao.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.TransicaoDeStatusInvalidaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PesagemEventoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PesagemEventoService {
    private final PesagemEventoRepository pesagemRepository;
    private final ProducaoRepository producaoRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public PesagemEventoResponseDTO registrarPesagemEvento(PesagemEventoRequestDTO dto) {
        PesagemEvento evento = dto.toEntity();
        Producao producao = producaoRepository.findById(dto.producaoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produção não encontrada. A balança perdeu a referência."));

        if (producao.getStatus() == StatusProducao.PENDENTE) {
            producao.setStatus(StatusProducao.PROCESSANDO);
            log.info("Produção {} iniciada fisicamente. Status alterado para PROCESSANDO.", producao.getId());
        }
        if (producao.getStatus() != StatusProducao.PROCESSANDO) {
            throw new TransicaoDeStatusInvalidaException(
                    "Não é possível registrar pesagem para uma produção que não está em status PENDENTE ou PROCESSANDO."
            );
        }

        evento.setProducao(producao);
        evento.setTimestamp(LocalDateTime.now());

        return PesagemEventoResponseDTO.fromEntity(pesagemRepository.save(evento));
    }
}