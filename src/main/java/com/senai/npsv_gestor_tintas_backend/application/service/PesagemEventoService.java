package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PesagemEventoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PesagemEventoService {
    private final PesagemEventoRepository pesagemRepository;
    private final ProducaoRepository producaoRepository;

    @Transactional
    public PesagemEventoResponseDTO registrarPesagemEvento(PesagemEventoRequestDTO dto) {
        PesagemEvento evento = dto.toEntity();
        Producao producao = producaoRepository.findById(dto.producaoId())
                .orElseThrow(() -> new RuntimeException("Ordem de Produção não encontrada. A balança perdeu a referência."));

        evento.setProducao(producao);
        evento.setTimestamp(LocalDateTime.now());

        return PesagemEventoResponseDTO.fromEntity(pesagemRepository.save(evento));
    }
}