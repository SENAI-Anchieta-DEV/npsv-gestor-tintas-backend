package com.senai.npsv_gestor_tintas_backend.domain.service;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EstoqueBaixoException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProdutoDomainService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public void darBaixaEstoque(Produto produto, BigDecimal quantidade) {
        if (produto.getQuantidadeEstoque().compareTo(quantidade) < 0) {
            throw new EstoqueBaixoException(String.format(
                    "Estoque insuficiente para '%s'. Solicitado: %s, Disponível: %s",
                    produto.getDescricao(), quantidade, produto.getQuantidadeEstoque()));
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque().subtract(quantidade));
        produtoRepository.save(produto);
    }
}
