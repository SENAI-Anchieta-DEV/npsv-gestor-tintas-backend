package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Producao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducaoRepository extends JpaRepository<Producao, String> {
}
