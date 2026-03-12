package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ItemFormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public ItemFormulaResponseDTO registrarItemFormula(ItemFormulaRequestDTO dto) {
        ItemFormula item = dto.toEntity();

        item.setFormula(
                formulaRepository.findById(dto.formulaId())
                        .orElseThrow(() -> new EntityNotFoundException("Fórmula não encontrada."))
        );

        item.setInsumo(
                produtoRepository.findById(dto.insumoId())
                        .orElseThrow(() -> new EntityNotFoundException("Insumo não encontrado no estoque."))
        );

        ItemFormula itemSalvo = itemRepository.save(item);
        return ItemFormulaResponseDTO.fromEntity(itemSalvo);
    }

    @Transactional(readOnly = true)
    public List<ItemFormulaResponseDTO> listarItemFormulaPorId(String formulaId) {
        return itemRepository.findByFormulaId(formulaId)
                .stream()
                .map(ItemFormulaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public void deletarItemFormula(String id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item da fórmula não encontrado.");
        }

        itemRepository.deleteById(id);
    }
}