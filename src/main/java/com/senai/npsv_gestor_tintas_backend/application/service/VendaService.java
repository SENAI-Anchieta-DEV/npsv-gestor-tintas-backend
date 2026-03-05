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

    @Transactional
    public IniciarVendaResponseDTO iniciarVenda(IniciarVendaRequestDTO dto) {
        // 1. Validar se o vendedor existe
        Usuario vendedor = usuarioRepository.findById(dto.vendedorId())
                .orElseThrow(() -> new IllegalArgumentException("Vendedor não encontrado com o ID informado."));

        // 2. Criar a Venda inicial
        Venda novaVenda = new Venda();
        novaVenda.setDataHora(LocalDateTime.now());
        novaVenda.setVendedor(vendedor);
        novaVenda.setValorTotal(BigDecimal.ZERO); // Inicia zerada, pois ainda não tem itens
        novaVenda.setStatus(StatusVenda.ABERTA); // Define o status inicial

        // Se for usar cliente:
        // Cliente cliente = clienteRepository.findById(dto.clienteId()).orElseThrow(...);
        // novaVenda.setCliente(cliente);

        // 3. Salvar no banco
        Venda vendaSalva = vendaRepository.save(novaVenda);

        // 4. Retornar DTO
        return IniciarVendaResponseDTO.fromEntity(vendaSalva);
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

    public List<VendaResponseDTO> listarPorVendedor(String vendedorId) {
        return vendaRepository.findByVendedorId(vendedorId).stream()
                .map(VendaResponseDTO::fromEntity)
                .toList();
    }
    @Transactional
    public VendaResponseDTO concluirVenda(String vendaId, ConcluirVendaRequestDTO dto) {
        // 1. Verificar se a venda existe
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada com o ID informado."));

        // 2. Validar Status (Não pode concluir venda já fechada ou cancelada)
        if (venda.getStatus() == StatusVenda.CONCLUIDA || venda.getStatus() == StatusVenda.CANCELADA) {
            throw new IllegalStateException("A venda não pode ser concluída pois está no status: " + venda.getStatus());
        }

        // 3. Validação de Peso (Mock/Exemplo da sua regra de negócio)
        // Se a venda possuir tinta manipulada que precisa passar pela balança:
        /*
        boolean pesoValidado = pesagemService.validarPesoDaVenda(venda.getId());
        if (!pesoValidado) {
            throw new IllegalArgumentException("O peso lido na balança não condiz com a fórmula solicitada.");
        }
        */

        // 4. Baixa de Estoque Iterativa e Atômica
        for (ItemVenda item : venda.getItens()) {

            // Tenta dar baixa e guarda o resultado (true ou false)
            boolean baixaComSucesso = produtoRepository.darBaixaEstoque(item.getProduto().getId(), item.getQuantidade());

            // Se falhou (false), lança a exceção e o @Transactional faz o Rollback
            if (!baixaComSucesso) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente no momento da conclusão para o produto: " + item.getProduto().getDescricao()
                );
            }
        }

        // 5. Fechamento da Venda
        venda.setStatus(StatusVenda.CONCLUIDA);

        // NOTA: Certifique-se de que os atributos abaixo existem na sua entidade Venda!
        // venda.setDataFechamento(LocalDateTime.now());
        // venda.setFormaPagamento(dto.formaPagamento());
        // venda.setValorRecebido(dto.valorRecebido());

        // 6. Persistir as alterações
        Venda vendaConcluida = vendaRepository.save(venda);

        return VendaResponseDTO.fromEntity(vendaConcluida);
    }
}