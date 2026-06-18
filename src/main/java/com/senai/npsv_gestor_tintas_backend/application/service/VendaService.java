package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.venda.*;
import com.senai.npsv_gestor_tintas_backend.domain.entity.*;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EstoqueInsuficienteException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.TransicaoDeStatusInvalidaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ClienteRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

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

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarVendas() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO listarVendaPorId(String id) {
        Venda venda = buscarVendaPorId(id);
        return VendaResponseDTO.fromEntity(venda);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarVendasPorVendedor(String vendedorId) {
        return vendaRepository.findByVendedorId(vendedorId).stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO atualizarVenda(String id, AtualizarVendaRequestDTO dto) {
        Venda venda = buscarVendaPorId(id);

        if (venda.getStatus() != StatusVenda.ABERTA) {
            throw new TransicaoDeStatusInvalidaException(
                    "Apenas vendas com status ABERTA podem ser atualizadas. Status atual: " + venda.getStatus()
            );
        }

        if (dto.clienteId() != null && !dto.clienteId().isBlank()) {
            Cliente cliente = buscarClientePorId(dto.clienteId());
            venda.setCliente(cliente);
        }

        venda.getItens().clear();
        BigDecimal valorTotal = BigDecimal.ZERO;

        if (dto.itens() != null) {
            for (ItemVendaRequestDTO itemDto : dto.itens()) {
                Produto produto = buscarProdutoPorId(itemDto.produtoId());

                if (produto.getQuantidadeEstoque().compareTo(itemDto.quantidade()) < 0) {
                    throw new EstoqueInsuficienteException(String.format(
                            "Estoque insuficiente para o produto '%s'. Estoque atual: %s",
                            produto.getDescricao(), produto.getQuantidadeEstoque()),
                            "RN03 – Bloqueio de Venda");
                }

                ItemVenda novoItem = itemDto.toEntity();
                novoItem.setVenda(venda);
                novoItem.setProduto(produto);
                novoItem.setPrecoPraticado(produto.getPrecoVenda());

                venda.getItens().add(novoItem);

                valorTotal = valorTotal.add(produto.getPrecoVenda().multiply(itemDto.quantidade()));
            }
        }

        venda.setValorTotal(valorTotal);
        venda.setFormaPagamento(dto.formaPagamento());

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO concluirVenda(String vendaId, ConcluirVendaRequestDTO dto) {
        Venda venda = buscarVendaPorId(vendaId);

        if (venda.getStatus() != StatusVenda.ABERTA) {
            throw new TransicaoDeStatusInvalidaException(
                    "Apenas vendas abertas podem ser concluídas. Status atual: " + venda.getStatus()
            );
        }

        if (dto.clienteId() != null && !dto.clienteId().isBlank()) {
            Cliente cliente = buscarClientePorId(dto.clienteId());
            venda.setCliente(cliente);
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        venda.getItens().clear();

        for (ItemVendaRequestDTO itemDto : dto.itens()) {
            Produto produto = buscarProdutoPorId(itemDto.produtoId());

            int linhasAfetadas = produtoRepository.darBaixaEstoque(produto.getId(), itemDto.quantidade());
            boolean possuiEstoqueSuficiente = linhasAfetadas > 0;

            if (!possuiEstoqueSuficiente) {
                throw new EstoqueInsuficienteException(String.format(
                        "Estoque insuficiente para o produto '%s'.",
                        produto.getDescricao()),
                        "RN03 – Bloqueio de Venda");
            }

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

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public void deletarVenda(String id) {
        Venda venda = buscarVendaPorId(id);

        if (venda.getStatus() == StatusVenda.CONCLUIDA) {
            throw new TransicaoDeStatusInvalidaException(
                    "Não é possível deletar uma venda que já foi concluída."
            );
        }

        vendaRepository.delete(venda);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO estornarVenda(String vendaId) {
        Venda venda = buscarVendaPorId(vendaId);

        if (venda.getStatus() != StatusVenda.CONCLUIDA) {
            throw new TransicaoDeStatusInvalidaException("Apenas vendas concluídas podem ser estornadas.");
        }

        for (ItemVenda item : venda.getItens()) {
            produtoRepository.estornarEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        venda.setStatus(StatusVenda.CANCELADA);
        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    private Venda buscarVendaPorId(String id) {
        return vendaRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada."));
    }

    private Cliente buscarClientePorId(String id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado ou inativo."));
    }

    private Produto buscarProdutoPorId(String id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado."));
    }
}