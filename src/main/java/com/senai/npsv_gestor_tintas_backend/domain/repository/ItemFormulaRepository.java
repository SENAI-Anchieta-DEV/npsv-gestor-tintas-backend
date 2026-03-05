package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemFormulaRepository extends JpaRepository<ItemFormula, String> {

    @Query("SELECT i FROM ItemFormula i WHERE i.formula.id = :formulaId")
    List<ItemFormula> findByFormulaId(@Param("formulaId") String formulaId);
}