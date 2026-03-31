package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EstoqueBaixoException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.RegraNegocioException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.VendaBloqueadaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;


    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO iniciarVenda(IniciarVendaRequestDTO dto) {
        Usuario vendedor = usuarioRepository.findByIdAndAtivoTrue(dto.vendedorId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor não encontrado ou inativo."));

        Venda venda = dto.toEntity();
        venda.setDataAbertura(LocalDateTime.now());
        venda.setVendedor(vendedor);

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarVendas() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO listarVendaPorId(String id) {
        return vendaRepository.findById(id)
                .map(VendaResponseDTO::fromEntity)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada."));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarVendasPorVendedor(String vendedorId) {
        return vendaRepository.findByVendedorId(vendedorId).stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO concluirVenda(String vendaId, ConcluirVendaRequestDTO dto) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada com o ID informado."));

        if (venda.getStatus() != StatusVenda.ABERTA) {
            throw new VendaBloqueadaException("Apenas vendas abertas podem ser concluídas.");
        }

        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new RegraNegocioException("Não é possível concluir uma venda sem itens.", "RN04");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaRequestDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado: " + itemDto.produtoId()));

            int linhasAfetadas = produtoRepository.darBaixaEstoque(produto.getId(), itemDto.quantidade());
            boolean possuiEstoqueSuficiente = linhasAfetadas > 0;

            if (!possuiEstoqueSuficiente) {
                throw new EstoqueBaixoException(String.format(
                        "Estoque insuficiente para o produto '%s'. Solicitado: %s, Disponível na última leitura: %s",
                        produto.getDescricao(), itemDto.quantidade(), produto.getQuantidadeEstoque()));
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque().subtract(itemDto.quantidade()));

            ItemVenda novoItem = itemDto.toEntity();
            novoItem.setVenda(venda);
            novoItem.setProduto(produto);
            novoItem.setPrecoPraticado(produto.getPrecoVenda());

            venda.getItens().add(novoItem);

            valorTotal = valorTotal.add(produto.getPrecoVenda().multiply(itemDto.quantidade()));
        }

        venda.setValorTotal(valorTotal);
        venda.setFormaPagamento(dto.formaPagamento());
        venda.setDataFechamento(LocalDateTime.now());
        venda.setStatus(StatusVenda.CONCLUIDA);

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }
}