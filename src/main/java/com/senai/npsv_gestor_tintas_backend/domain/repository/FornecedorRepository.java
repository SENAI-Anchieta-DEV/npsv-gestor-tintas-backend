package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, String> {
    boolean existsByCnpj(String cnpj);
    List<Fornecedor> findAllByAtivo(boolean ativo);
    Optional<Fornecedor> findByIdAndAtivoTrue(String id);
}
