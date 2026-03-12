package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemVenda;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ItemVendaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemVendaService {

    private final ItemVendaRepository repository;

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<ItemVenda> listarTodos() {
        return repository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ItemVenda buscarPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de venda não encontrado com ID: " + id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<ItemVenda> listarPorVenda(String vendaId) {
        return repository.findAllByVendaId(vendaId);
    }
}