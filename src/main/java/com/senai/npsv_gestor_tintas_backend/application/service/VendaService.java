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
    public VendaResponseDTO iniciarVenda(IniciarVendaRequestDTO dto) {
        Usuario vendedor = usuarioRepository.findByIdAndAtivoTrue(dto.vendedorId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Vendedor não encontrado ou inativo."));

        Venda venda = Venda.builder()
                .dataHora(LocalDateTime.now())
                .vendedor(vendedor)
                .valorTotal(BigDecimal.ZERO)
                .status(StatusVenda.ABERTA)
                .itens(new ArrayList<>())
                .build();

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }

    // Leitura (Read)
    public List<VendaResponseDTO> listarTodas() {
        return vendaRepository.findAll().stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    public VendaResponseDTO buscarPorId(String id) {
        return vendaRepository.findById(id)
                .map(VendaResponseDTO::fromEntity)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada."));
    }

    public List<VendaResponseDTO> listarPorVendedor(String vendedorId) {
        return vendaRepository.findByVendedorId(vendedorId).stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public VendaResponseDTO concluirVenda(String vendaId, ConcluirVendaRequestDTO dto) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada com o ID informado."));

        if (venda.getStatus() != StatusVenda.ABERTA) {
            throw new VendaBloqueadaException("Apenas vendas abertas podem ser concluídas.");
        }

        // Validação da Venda Vazia (Lança a regra genérica com um código customizado RN04)
        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new RegraNegocioException("Não é possível concluir uma venda sem itens.", "RN04");
        }

        BigDecimal valorTotal = BigDecimal.ZERO;

        // Iteração de Itens
        for (VendaItemRequestDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado: " + itemDto.produtoId()));

            // Validação de Stock (com a exceção customizada RN02)
            if (produto.getQuantidadeEstoque().compareTo(itemDto.quantidade()) < 0) {
                throw new EstoqueBaixoException(String.format(
                        "Estoque insuficiente para o produto '%s'. Solicitado: %s, Disponível: %s",
                        produto.getDescricao(), itemDto.quantidade(), produto.getQuantidadeEstoque()));
            }

            // Atualização de Stock
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque().subtract(itemDto.quantidade()));
            produtoRepository.save(produto);

            // Registo do ItemVenda
            ItemVenda novoItem = ItemVenda.builder()
                    .venda(venda)
                    .produto(produto)
                    .quantidade(itemDto.quantidade())
                    .precoPraticado(produto.getPrecoVenda())
                    .build();

            venda.getItens().add(novoItem);

            // Cálculo Financeiro
            valorTotal = valorTotal.add(produto.getPrecoVenda().multiply(itemDto.quantidade()));
        }

        // Finalização da Venda
        venda.setValorTotal(valorTotal);
        venda.setFormaPagamento(dto.formaPagamento());
        venda.setDataFechamento(LocalDateTime.now());
        venda.setStatus(StatusVenda.CONCLUIDA);

        return VendaResponseDTO.fromEntity(vendaRepository.save(venda));
    }
}