package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormulaRepository extends JpaRepository<Formula, String> {
    Optional<Formula> findByCodigoInterno(String codigoInterno);
}
