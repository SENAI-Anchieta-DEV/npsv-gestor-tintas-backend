package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record FornecedorRequestDTO(
        @NotBlank(message = "A razão social é obrigatória.")
        String razaoSocial,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @CNPJ(message = "O formato do CNPJ é inválido.")
        String cnpj,

        @NotBlank(message = "O nome do contato é obrigatório.")
        String nomeContato,

        @NotBlank(message = "O telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

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
