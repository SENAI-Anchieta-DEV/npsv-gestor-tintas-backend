package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ItemVendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemVendaService {

    private final ItemVendaRepository repository;

    // 1. Listar todos os itens já vendidos no sistema (Geral)
    public List<ItemVenda> listarTodos() {
        return repository.findAll();
    }

    // 2. Buscar um item específico pelo seu ID
    public ItemVenda buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de venda não encontrado com ID: " + id));
    }

    // 3. Listar todos os itens de uma Venda específica (Útil para detalhar a venda na tela)
    public List<ItemVenda> listarPorVenda(String vendaId) {
        return repository.findAllByVendaId(vendaId);
    }
}