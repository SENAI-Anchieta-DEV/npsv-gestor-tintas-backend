package com.senai.npsv_gestor_tintas_backend.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemAtualResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemEventoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PesagemMqttPayloadDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PesagemEventoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProducaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PesagemMqttService {


    // Margem de erro definida pela RN01: 5%
    private static final BigDecimal MARGEM_RN01 = new BigDecimal("5.00");

    private final PesagemEventoRepository pesagemEventoRepository;
    private final ProducaoRepository producaoRepository;
    private final ObjectMapper objectMapper;

    // ==========================================================================
    // PROCESSAMENTO DO PAYLOAD MQTT — chamado pelo subscriber (sem HTTP context)
    // NÃO usa @PreAuthorize: a segurança aqui é garantida pela rede interna
    // (somente o broker local publica neste tópico).
    // ==========================================================================

    @Transactional
    public void processarPayloadMqtt(String payloadJson) {
        // 1. Deserializar — NPSV-313: payload malformado → log + descarte
        PesagemMqttPayloadDTO dto;
        try {
            dto = objectMapper.readValue(payloadJson, PesagemMqttPayloadDTO.class);
        } catch (JsonProcessingException e) {
            log.error("[NPSV-304] Payload MQTT malformado descartado. Conteúdo: {} | Erro: {}",
                    payloadJson, e.getMessage());
            return; // Subscriber continua vivo — próxima mensagem é processada normalmente
        }

        // 2. Buscar produção — NPSV-313: producaoId inválido → log + descarte
        Producao producao = producaoRepository.findById(dto.producaoId()).orElse(null);
        if (producao == null) {
            log.warn("[NPSV-304] producaoId='{}' não encontrado no banco. Evento MQTT descartado.", dto.producaoId());
            return;
        }

        // 3. Validar status da produção (só processa se está em pesagem ativa)
        if (producao.getStatus() == StatusProducao.CONCLUIDO
                || producao.getStatus() == StatusProducao.CANCELADO
                || producao.getStatus() == StatusProducao.PERDA_TOTAL) {
            log.warn("[NPSV-304] Produção '{}' está no status {}. Evento MQTT ignorado.",
                    dto.producaoId(), producao.getStatus());
            return;
        }

        // 4. Calcular pesoAlvo — soma de quantidadeNecessaria de todos os itens da fórmula
        BigDecimal pesoAlvo = producao.getFormula().getItens().stream()
                .map(ItemFormula::getQuantidadeNecessaria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Calcular ResultadoRN01 — NPSV-309
        ResultadoRN01 resultadoRN01 = calcularResultadoRN01(dto.pesoLido(), pesoAlvo, dto.estavel());

        // 6. Resolver UnidadeMedida — converte string do ESP32 para enum local
        UnidadeMedida unidadeMedida = resolverUnidadeMedida(dto.unidadeMedida());

        // 7. Persistir PesagemEvento — NPSV-310
        PesagemEvento evento = PesagemEvento.builder()
                .pesoLido(dto.pesoLido())
                .timestamp(LocalDateTime.now()) // Hora de recepção no servidor
                .foiAprovado(resultadoRN01 == ResultadoRN01.APROVADO)
                .unidadeMedida(unidadeMedida)
                .estavel(dto.estavel())
                .resultadoRN01(resultadoRN01)
                .producao(producao)
                .build();

        pesagemEventoRepository.save(evento);

        log.info("[NPSV-304] PesagemEvento persistido | producaoId={} | pesoLido={} | pesoAlvo={} | resultado={}",
                dto.producaoId(), dto.pesoLido(), pesoAlvo, resultadoRN01);
    }

    // ==========================================================================
    // ENDPOINT: GET /producoes/{id}/pesagem/atual — NPSV-311
    // ==========================================================================

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public PesagemAtualResponseDTO getPesagemAtual(String producaoId) {
        Producao producao = producaoRepository.findById(producaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Produção não encontrada com o ID: " + producaoId));

        BigDecimal pesoAlvo = producao.getFormula().getItens().stream()
                .map(ItemFormula::getQuantidadeNecessaria)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PesagemEvento ultimoEvento = pesagemEventoRepository
                .findTopByProducaoIdOrderByTimestampDesc(producaoId)
                .orElse(null);

        BigDecimal pesoLido = ultimoEvento != null ? ultimoEvento.getPesoLido() : BigDecimal.ZERO;
        ResultadoRN01 resultado = ultimoEvento != null
                ? ultimoEvento.getResultadoRN01()
                : ResultadoRN01.AGUARDANDO_ESTABILIDADE;

        return new PesagemAtualResponseDTO(pesoAlvo, pesoLido, resultado, producao.getStatus());
    }

    // ==========================================================================
    // ENDPOINT: GET /producoes/{id}/eventos-pesagem — NPSV-312
    // ==========================================================================

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public List<PesagemEventoResponseDTO> getEventosPesagem(String producaoId) {
        if (!producaoRepository.existsById(producaoId)) {
            throw new EntidadeNaoEncontradaException(
                    "Produção não encontrada com o ID: " + producaoId);
        }

        return pesagemEventoRepository
                .findAllByProducaoIdOrderByTimestampDesc(producaoId)
                .stream()
                .map(PesagemEventoResponseDTO::fromEntity)
                .toList();
    }

    // ==========================================================================
    // MÉTODOS PRIVADOS
    // ==========================================================================

    /**
     * Aplica a RN01: |pesoLido - pesoAlvo| / pesoAlvo * 100 <= 5%
     * Só executa o cálculo quando estavel=true (flag vinda do firmware).
     */
    private ResultadoRN01 calcularResultadoRN01(BigDecimal pesoLido,
                                                BigDecimal pesoAlvo,
                                                boolean estavel) {
        if (!estavel) {
            return ResultadoRN01.AGUARDANDO_ESTABILIDADE;
        }

        if (pesoAlvo.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("[RN01] pesoAlvo é zero — impossível calcular desvio percentual. Retornando FORA_DA_MARGEM.");
            return ResultadoRN01.FORA_DA_MARGEM;
        }

        BigDecimal desvioPercentual = pesoLido.subtract(pesoAlvo).abs()
                .divide(pesoAlvo, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return desvioPercentual.compareTo(MARGEM_RN01) <= 0
                ? ResultadoRN01.APROVADO
                : ResultadoRN01.FORA_DA_MARGEM;
    }

    /**
     * Converte a string do ESP32 (ex: "G") para o enum UnidadeMedida.
     * Se inválida, retorna null e loga aviso — não bloqueia a persistência.
     */
    private UnidadeMedida resolverUnidadeMedida(String valor) {
        if (valor == null || valor.isBlank()) return null;
        try {
            return UnidadeMedida.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("[NPSV-304] UnidadeMedida '{}' não reconhecida. Campo salvo como null.", valor);
            return null;
        }
    }
}
