package com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record FornecedorResponseDTO(
        @Schema(description = "Identificador único do fornecedor", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Razão social do fornecedor", example = "Tintas XYZ Ltda.")
        String razaoSocial,

        @Schema(description = "CNPJ do fornecedor, formato válido para CNPJ brasileiro", example = "12.345.678/0001-90")
        String cnpj,

        @Schema(description = "Nome de contato do fornecedor", example = "Maria Silva")
        String nomeContato,

        @Schema(description = "Telefone de contato do fornecedor", example = "11987654321")
        String telefone,

        @Schema(description = "Email de contato do fornecedor", example = "fornecedor@email.com")
        String email,

        @Schema(description = "Endereço completo do fornecedor", example = "Avenida Central, 456, Bairro Industrial, Cidade, Estado, CEP 98765-432")
        String endereco,

        @Schema(description = "Indica se o fornecedor está ativo ou inativo", example = "true")
        boolean ativo,

        @Schema(description = "Data e hora de cadastro do fornecedor", example = "2026-06-01T14:30:00")
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
