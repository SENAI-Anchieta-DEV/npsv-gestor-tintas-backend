package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.PesagemEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PesagemEventoRepository extends JpaRepository<PesagemEvento, String> {
    boolean existsByProducaoId(String producaoId);

    // Usado pelo endpoint GET /producoes/{id}/pesagem/atual
    Optional<PesagemEvento> findTopByProducaoIdOrderByTimestampDesc(String producaoId);

    // Usado pelo endpoint GET /producoes/{id}/eventos-pesagem
    List<PesagemEvento> findAllByProducaoIdOrderByTimestampDesc(String producaoId);

}
