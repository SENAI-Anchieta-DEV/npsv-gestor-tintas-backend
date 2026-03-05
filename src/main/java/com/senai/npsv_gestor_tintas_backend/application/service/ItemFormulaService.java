package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ItemFormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemFormulaService {
    private final ItemFormulaRepository itemRepository;
    private final FormulaRepository formulaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public ItemFormulaResponseDTO adicionarInsumoNaReceitaDaFormula(ItemFormulaRequestDTO dto) {
        ItemFormula item = dto.toEntity();
        item.setFormula(formulaRepository.findById(dto.formulaId()).orElseThrow(() -> new RuntimeException("Fórmula não encontrada.")));
        item.setInsumo(produtoRepository.findById(dto.insumoId()).orElseThrow(() -> new RuntimeException("Insumo não encontrado no estoque.")));
        return ItemFormulaResponseDTO.fromEntity(itemRepository.save(item));
    }

    public List<ItemFormulaResponseDTO> consultarInsumosDaReceita(String formulaId) {
        return itemRepository.findByFormulaId(formulaId).stream().map(ItemFormulaResponseDTO::fromEntity).toList();
    }

    @Transactional
    public void removerInsumoDaReceita(String id) {
        itemRepository.deleteById(id);
    }
}