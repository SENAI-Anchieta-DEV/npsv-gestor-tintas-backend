package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.FormulaResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Formula;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FormulaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FormulaService {
    private final FormulaRepository repository;

    @Transactional
    public FormulaResponseDTO criarNovaReceitaDeFormula(FormulaRequestDTO dto) {
        Formula formula = dto.toEntity();
        formula.setDataCriacao(LocalDateTime.now());
        return FormulaResponseDTO.fromEntity(repository.save(formula));
    }

    public List<FormulaResponseDTO> listarReceitasDeFormulas() {
        return repository.findAll().stream().map(FormulaResponseDTO::fromEntity).toList();
    }

    public FormulaResponseDTO consultarEspecificacoesDaFormula(String id) {
        Formula formula = buscarFormulaPorId(id);
        return FormulaResponseDTO.fromEntity(formula);
    }

    @Transactional
    public FormulaResponseDTO alterarEspecificacoesDaFormula(String id, FormulaRequestDTO dto) {
        Formula formula = buscarFormulaPorId(id);
        formula.setCodigoInterno(dto.codigoInterno());
        formula.setNomeCor(dto.nomeCor());
        return FormulaResponseDTO.fromEntity(repository.save(formula));
    }

    @Transactional
    public void inativarReceitaDeFormula(String id) {
        repository.delete(buscarFormulaPorId(id));
    }

    private Formula buscarFormulaPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Fórmula técnica não encontrada."));
    }
}