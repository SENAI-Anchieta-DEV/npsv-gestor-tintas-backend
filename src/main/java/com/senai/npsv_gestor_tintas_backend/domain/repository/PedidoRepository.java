package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    boolean existsByFornecedorId(String fornecedorId);

    @EntityGraph(attributePaths = {"fornecedor", "admin", "itens", "itens.produto"})
    List<Pedido> findByStatus(StatusPedido status);

    @EntityGraph(attributePaths = {"fornecedor", "admin", "itens", "itens.produto"})
    List<Pedido> findByFornecedorId(String fornecedorId);

    @EntityGraph(attributePaths = {"fornecedor", "admin", "itens", "itens.produto"})
    List<Pedido> findByStatusAndFornecedorId(StatusPedido status, String fornecedorId);

    @Override
    @EntityGraph(attributePaths = {"fornecedor", "admin", "itens", "itens.produto"})
    List<Pedido> findAll();

    @Override
    @EntityGraph(attributePaths = {"fornecedor", "admin", "itens", "itens.produto"})
    Optional<Pedido> findById(String id);
}
