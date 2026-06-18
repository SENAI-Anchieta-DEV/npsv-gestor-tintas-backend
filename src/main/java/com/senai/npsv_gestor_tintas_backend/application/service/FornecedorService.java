package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor.FornecedorRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor.FornecedorResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CnpjJaCadastradoException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.VinculoExistenteException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.FornecedorRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public FornecedorResponseDTO registrarFornecedor(FornecedorRequestDTO dto) {
        if (fornecedorRepository.existsByCnpj(dto.cnpj())) {
            throw new CnpjJaCadastradoException("Já existe um fornecedor cadastrado com este CNPJ.");
        }

        Fornecedor fornecedor = dto.toEntity();
        return FornecedorResponseDTO.fromEntity(fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<FornecedorResponseDTO> listarFornecedores(Boolean ativo) {
        List<Fornecedor> fornecedores;
        if (ativo == null) {
            fornecedores = fornecedorRepository.findAll();
        } else {
            fornecedores = fornecedorRepository.findAllByAtivo(ativo);
        }

        return fornecedores.stream()
                .map(FornecedorResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public FornecedorResponseDTO listarFornecedorPorId(String id) {
        Fornecedor fornecedor = buscarFornecedorPorId(id);
        return FornecedorResponseDTO.fromEntity(fornecedor);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public FornecedorResponseDTO atualizarFornecedor(String id, FornecedorRequestDTO dto) {
        Fornecedor existente = buscarFornecedorPorId(id);

        if (!existente.getCnpj().equals(dto.cnpj()) && fornecedorRepository.existsByCnpj(dto.cnpj())) {
            throw new CnpjJaCadastradoException("Já existe um fornecedor cadastrado com este CNPJ.");
        }

        existente.setRazaoSocial(dto.razaoSocial());
        existente.setCnpj(dto.cnpj());
        existente.setNomeContato(dto.nomeContato());
        existente.setTelefone(dto.telefone());
        existente.setEmail(dto.email());
        existente.setEndereco(dto.endereco());

        return FornecedorResponseDTO.fromEntity(fornecedorRepository.save(existente));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarFornecedor(String id) {
        Fornecedor fornecedor = buscarFornecedorPorId(id);

        if (pedidoRepository.existsByFornecedorId(id)) {
            throw new VinculoExistenteException("Este fornecedor possui pedidos vinculados e não pode ser removido fisicamente.");
        }

        fornecedorRepository.delete(fornecedor);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void desativarFornecedor(String id) {
        Fornecedor fornecedor = buscarFornecedorPorId(id);
        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }

    private Fornecedor buscarFornecedorPorId(String id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Fornecedor não encontrado com o ID: " + id));
    }
}
