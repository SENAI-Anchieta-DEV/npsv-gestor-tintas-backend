package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
}
