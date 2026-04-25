package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, String> {

    // Atualização atômica para evitar que o estoque fique negativo em compras simultâneas.
    // Retorna o número de linhas afetadas: 1 (Sucesso) ou 0 (Falha por falta de estoque).
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Produto p " +
            "SET p.quantidadeEstoque = p.quantidadeEstoque - :quantidade, " +
            "    p.estoqueEmAlerta = CASE WHEN (p.quantidadeEstoque - :quantidade) <= p.estoqueMinimo THEN true ELSE false END " +
            "WHERE p.id = :produtoId AND p.quantidadeEstoque >= :quantidade")
    int darBaixaEstoque(@Param("produtoId") String produtoId,
                        @Param("quantidade") BigDecimal quantidade);

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Produto p " +
            "SET p.quantidadeEstoque = p.quantidadeEstoque + :quantidade, " +
            "    p.estoqueEmAlerta = CASE WHEN (p.quantidadeEstoque + :quantidade) <= p.estoqueMinimo THEN true ELSE false END " +
            "WHERE p.id = :produtoId")
    void estornarEstoque(@Param("produtoId") String produtoId,
                        @Param("quantidade") BigDecimal quantidade);

    List<Produto> findByEstoqueEmAlertaTrue();
}