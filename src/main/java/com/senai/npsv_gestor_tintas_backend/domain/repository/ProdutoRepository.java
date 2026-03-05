package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

    // Atualização atômica para evitar que o estoque fique negativo em compras simultâneas.
    // Retorna 1 (Sucesso) se tinha estoque e atualizou, ou 0 (Falha) se o estoque era menor que o pedido.
    @Modifying
    @Query("UPDATE Produto p SET p.quantidadeEstoque = p.quantidadeEstoque - :quantidade WHERE p.id = :produtoId AND p.quantidadeEstoque >= :quantidade")
    boolean darBaixaEstoque(@Param("produtoId") String produtoId, @Param("quantidade") BigDecimal quantidade);
}