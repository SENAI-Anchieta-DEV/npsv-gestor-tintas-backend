package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, String> {
    List<Venda> findByVendedorId(String vendedorId);
}
