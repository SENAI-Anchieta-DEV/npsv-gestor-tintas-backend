package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface VendaRepository extends JpaRepository<Venda, String> {
    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor"})
    List<Venda> findAll();

    @EntityGraph(attributePaths = {"cliente", "vendedor"})
    List<Venda> findByVendedorId(String vendedorId);

    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor", "itens", "itens.produto"})
    Optional<Venda> findById(String id);

    boolean existsByClienteId(String id);
}
