package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

    // Atualização atômica para evitar que o estoque fique negativo em compras simultâneas.
    // Retorna o número de linhas afetadas: 1 (Sucesso) ou 0 (Falha por falta de estoque).
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Produto p " +
            "SET p.quantidadeEstoque = p.quantidadeEstoque - :quantidade " +
            "WHERE p.id = :produtoId AND p.quantidadeEstoque >= :quantidade")
    int darBaixaEstoque(@Param("produtoId") String produtoId,
                        @Param("quantidade") BigDecimal quantidade);
}