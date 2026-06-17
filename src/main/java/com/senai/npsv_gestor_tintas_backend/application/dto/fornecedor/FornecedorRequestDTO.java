package com.senai.npsv_gestor_tintas_backend.application.dto.fornecedor;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FornecedorRequestDTO(
        @Schema(description = "A razão social do fornecedor", example = "Tintas XYZ Ltda.")
        @NotBlank(message = "A razão social é obrigatória.")
        String razaoSocial,

        @Schema(description = "O CNPJ do fornecedor, formato válido para CNPJ brasileiro", example = "12.345.678/0001-90")
        @NotBlank(message = "O CNPJ é obrigatório.")
        String cnpj,

        @Schema(description = "O nome de contato do fornecedor", example = "Maria Silva")
        @NotBlank(message = "O nome do contato é obrigatório.")
        String nomeContato,

        @Schema(description = "O telefone de contado do fornecedor", example = "11987654321")
        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @Schema(description = "O email de contato do fornecedor", example = "fornecedor@email.com")
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Endereço completo do fornecedor", example = "Avenida Central, 456, Bairro Industrial, Cidade, Estado, CEP 98765-432")
        @NotBlank(message = "O endereço é obrigatório.")
        String endereco
) {
    public Fornecedor toEntity() {
        return Fornecedor.builder()
                .razaoSocial(this.razaoSocial)
                .cnpj(this.cnpj)
                .nomeContato(this.nomeContato)
                .telefone(this.telefone)
                .email(this.email)
                .endereco(this.endereco)
                .ativo(true)
                .build();
    }
}
