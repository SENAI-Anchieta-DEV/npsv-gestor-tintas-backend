package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;

import java.time.LocalDateTime;

public record FornecedorResponseDTO(
        String id,
        String razaoSocial,
        String cnpj,
        String nomeContato,
        String telefone,
        String email,
        String endereco,
        boolean ativo,
        LocalDateTime dataCadastro
) {
    public static FornecedorResponseDTO fromEntity(Fornecedor fornecedor) {
        if (fornecedor == null) return null;
        return new FornecedorResponseDTO(
                fornecedor.getId(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getNomeContato(),
                fornecedor.getTelefone(),
                fornecedor.getEmail(),
                fornecedor.getEndereco(),
                fornecedor.isAtivo(),
                fornecedor.getDataCadastro()
        );
    }
}
