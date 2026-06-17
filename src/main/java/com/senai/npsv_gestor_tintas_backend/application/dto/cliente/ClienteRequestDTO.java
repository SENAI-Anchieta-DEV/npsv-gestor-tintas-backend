package com.senai.npsv_gestor_tintas_backend.application.dto.cliente;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome,

        @Schema(description = "CPF do cliente, formato válido para CPF brasileiro", example = "123.456.789-00")
        @NotBlank(message = "O CPF não pode estar em branco.")
        String cpf,

        @Schema(description = "Email do cliente para contato", example = "email@email.com")
        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Telefone de contato do cliente", example = "11912345678")
        @NotBlank(message = "O telefone não pode estar em branco.")
        String telefone,

        @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123, Bairro Jardim, Cidade, Estado, CEP 12345-678")
        @NotBlank(message = "O endereço não pode estar em branco.")
        String endereco
) {
    public Cliente toEntity() {
        return Cliente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .telefone(this.telefone)
                .endereco(this.endereco)
                .build();
    }
}
