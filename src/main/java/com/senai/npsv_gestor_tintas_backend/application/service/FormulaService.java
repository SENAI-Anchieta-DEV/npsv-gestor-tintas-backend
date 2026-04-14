package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemFormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.ItemFormula;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Produto;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CodigoJaExisteException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormulaService {
    private final FormulaRepository formulaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public FormulaResponseDTO registrarFormula(FormulaRequestDTO dto) {
        validarCodigoInternoUnico(dto.codigoInterno(), null);

        Formula formula = dto.toEntity();

        adicionarItensNaFormula(formula, dto.itens());

        return FormulaResponseDTO.fromEntity(formulaRepository.saveAndFlush(formula));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA', 'VENDEDOR')")
    public List<FormulaResponseDTO> listarFormulas() {
        return formulaRepository.findAll().stream().map(FormulaResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA', 'VENDEDOR')")
    public FormulaResponseDTO listarFormulaPorId(String id) {
        Formula formula = buscarFormulaPorId(id);
        return FormulaResponseDTO.fromEntity(formula);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public FormulaResponseDTO atualizarFormula(String id, FormulaRequestDTO dto) {
        validarCodigoInternoUnico(dto.codigoInterno(), id);

        Formula formula = buscarFormulaPorId(id);

        formula.setCodigoInterno(dto.codigoInterno());
        formula.setNomeCor(dto.nomeCor());

        formula.getItens().clear();
        adicionarItensNaFormula(formula, dto.itens());

        return FormulaResponseDTO.fromEntity(formulaRepository.saveAndFlush(formula));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'COLORISTA')")
    public void deletarFormula(String id) {
        formulaRepository.delete(buscarFormulaPorId(id));
    }

    private void adicionarItensNaFormula(Formula formula, List<ItemFormulaRequestDTO> itensDto) {
        for (ItemFormulaRequestDTO itemDto : itensDto) {
            Produto insumo = produtoRepository.findById(itemDto.insumoId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Insumo não encontrado com o ID: " + itemDto.insumoId()));

            ItemFormula novoItem = ItemFormula.builder()
                    .formula(formula)
                    .insumo(insumo)
                    .quantidadeNecessaria(itemDto.quantidadeNecessaria())
                    .ordemAdicao(itemDto.ordemAdicao())
                    .build();

            formula.getItens().add(novoItem);
        }
    }

    private void validarCodigoInternoUnico(String codigoInterno, String formulaIdAtual) {
        formulaRepository.findByCodigoInterno(codigoInterno).ifPresent(formulaExistente -> {
            if (!formulaExistente.getId().equals(formulaIdAtual)) {
                throw new CodigoJaExisteException("Já existe uma fórmula cadastrada com o código interno: " + codigoInterno);
            }
        });
    }

    private Formula buscarFormulaPorId(String id) {
        return formulaRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Fórmula técnica não encontrada."));
    }
}