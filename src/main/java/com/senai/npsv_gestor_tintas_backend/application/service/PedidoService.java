package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ItemPedidoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.PedidoResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.*;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.PedidoNaoEditavelException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.TransicaoDeStatusInvalidaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FornecedorRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PedidoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO registrarPedido(PedidoRequestDTO dto) {
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        Usuario admin = usuarioRepository.findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Admin não encontrado."));

        Fornecedor fornecedor = buscarFornecedorPorId(dto.fornecedorId());

        Pedido pedido = dto.toEntity(admin, fornecedor);

        adicionarItensNoPedido(pedido, dto.itens());

        return PedidoResponseDTO.fromEntity(pedidoRepository.saveAndFlush(pedido));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<PedidoResponseDTO> listarPedidos(StatusPedido status, String fornecedorId) {
        List<Pedido> pedidos;

        if (status != null && fornecedorId != null) {
            pedidos = pedidoRepository.findByStatusAndFornecedorId(status, fornecedorId);
        } else if (status != null) {
            pedidos = pedidoRepository.findByStatus(status);
        } else if (fornecedorId != null) {
            pedidos = pedidoRepository.findByFornecedorId(fornecedorId);
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return pedidos.stream().map(PedidoResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO listarPedidoPorId(String id) {
        return PedidoResponseDTO.fromEntity(buscarPedidoPorId(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO atualizarPedido(String id, PedidoRequestDTO dto) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() == StatusPedido.RECEBIDO || pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new PedidoNaoEditavelException(
                    "Pedido com status " + pedido.getStatus() + " não pode ser editado.");
        }

        Fornecedor fornecedor = buscarFornecedorPorId(dto.fornecedorId());

        pedido.setFornecedor(fornecedor);
        pedido.setDataPrevisaoEntrega(dto.dataPrevisaoEntrega());
        pedido.setObservacao(dto.observacao());

        pedido.getItens().clear();
        adicionarItensNoPedido(pedido, dto.itens());

        return PedidoResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO enviarPedido(String id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.PENDENTE) {
            throw new TransicaoDeStatusInvalidaException(
                    "Apenas pedidos PENDENTES podem ser enviados. Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.ENVIADO);
        return PedidoResponseDTO.fromEntity(pedidoRepository.save(pedido));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponseDTO receberPedido(String id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() != StatusPedido.ENVIADO) {
            throw new TransicaoDeStatusInvalidaException(
                    "Apenas pedidos ENVIADOS podem ser recebidos. Status atual: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.RECEBIDO);
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        incrementarEstoque(pedidoSalvo);

        return PedidoResponseDTO.fromEntity(pedidoSalvo);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelarPedido(String id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getStatus() == StatusPedido.RECEBIDO) {
            throw new TransicaoDeStatusInvalidaException(
                    "Pedido já recebido não pode ser cancelado.");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new TransicaoDeStatusInvalidaException(
                    "Este pedido já está cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void adicionarItensNoPedido(Pedido pedido, List<ItemPedidoRequestDTO> itensDto) {
        for (ItemPedidoRequestDTO itemDto : itensDto) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException(
                            "Produto não encontrado: " + itemDto.produtoId()));

            pedido.getItens().add(itemDto.toEntity(pedido, produto));
        }
    }

    private void incrementarEstoque(Pedido pedido) {
        log.info("--- INÍCIO DO INCREMENTO DE ESTOQUE (PEDIDO {}) ---", pedido.getId());
        for (ItemPedido item : pedido.getItens()) {
            produtoRepository.incrementarEstoque(item.getProduto().getId(), item.getQuantidade());
            log.info("Produto '{}' incrementado em {}.",
                    item.getProduto().getId(), item.getQuantidade());
        }
        log.info("--- FIM DO INCREMENTO DE ESTOQUE ---");
    }

    private Pedido buscarPedidoPorId(String id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado"));
    }

    private Fornecedor buscarFornecedorPorId(String id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado."));
    }
}