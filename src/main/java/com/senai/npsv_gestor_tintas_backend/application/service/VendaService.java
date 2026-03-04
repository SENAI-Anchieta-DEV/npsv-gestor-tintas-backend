package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.VendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.VendaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public VendaResponseDTO criarVenda(VendaRequestDTO dto) {
        // 1. Validar Vendedor
        Usuario vendedor = usuarioRepository.findById(dto.vendedorId())
                .orElseThrow(() -> new EntityNotFoundException("Vendedor não encontrado."));

        // 2. Preparar Cabeçalho da Venda
        Venda venda = new Venda();
        venda.setDataHora(LocalDateTime.now());
        venda.setVendedor(vendedor);
        venda.setItens(new ArrayList<>()); // Inicializa lista

        BigDecimal totalCalculado = BigDecimal.ZERO;

        // 3. Processar Itens
        for (VendaRequestDTO.ItemVendaRequest itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + itemDto.produtoId()));

            // --- TASK NPSV-249: Validação de Saldo ---
            if (produto.getQuantidadeEstoque().compareTo(itemDto.quantidade()) < 0) {
                // Usando IllegalArgumentException (Erro genérico nativo do Java para argumentos inválidos)
                throw new IllegalArgumentException(
                        "Estoque insuficiente para '" + produto.getDescricao() + "'. Disponível: " + produto.getQuantidadeEstoque()
                );
            }

            // Atualizar estoque (Baixa)
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque().subtract(itemDto.quantidade()));
            produtoRepository.save(produto);

            // Criar Item
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoPraticado(produto.getPrecoVenda());
            item.setVenda(venda); // Vínculo essencial para o Cascade

            venda.getItens().add(item);

            // --- TASK NPSV-250: Cálculo Automático ---
            totalCalculado = totalCalculado.add(item.getPrecoPraticado().multiply(item.getQuantidade()));
        }

        venda.setValorTotal(totalCalculado);

        // --- TASK NPSV-251: Persistência em Cascata ---
        Venda vendaSalva = vendaRepository.save(venda);

        return VendaResponseDTO.fromEntity(vendaSalva);
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
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada."));
    }
}