package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.*;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado ou inativo."));

        Venda venda = Venda.builder()
                .dataHora(LocalDateTime.now())
                .vendedor(vendedor)
                .valorTotal(BigDecimal.ZERO)
                .status(StatusVenda.ABERTA)
                .itens(new ArrayList<>())
                .build();

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO buscarPorId(String id) {
        return vendaRepository.findById(id)
                .map(VendaResponseDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada."));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<VendaResponseDTO> listarPorVendedor(String vendedorId) {
        return vendaRepository.findByVendedorId(vendedorId).stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public VendaResponseDTO concluirVenda(String vendaId, ConcluirVendaRequestDTO dto) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada com o ID informado."));

        if (venda.getStatus() != StatusVenda.ABERTA) {
            throw new IllegalArgumentException("Apenas vendas abertas podem ser concluídas.");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (VendaItemRequestDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.produtoId()));

            if (produto.getQuantidadeEstoque().compareTo(itemDto.quantidade()) < 0) {
                throw new IllegalArgumentException(String.format(
                        "Estoque insuficiente para o produto '%s'. Solicitado: %s, Disponível: %s",
                        produto.getDescricao(), itemDto.quantidade(), produto.getQuantidadeEstoque()));
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque().subtract(itemDto.quantidade()));
            produtoRepository.save(produto);

            ItemVenda novoItem = ItemVenda.builder()
                    .venda(venda)
                    .produto(produto)
                    .quantidade(itemDto.quantidade())
                    .precoPraticado(produto.getPrecoVenda())
                    .build();

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