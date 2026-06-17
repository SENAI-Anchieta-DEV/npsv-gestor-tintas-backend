package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Cliente;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        @Schema(description = "Identificador único do cliente", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,

        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String nome,

        @Schema(description = "CPF do cliente, formato válido para CPF brasileiro", example = "123.456.789-00")
        String cpf,

        @Schema(description = "Email do cliente para contato", example = "email@email.com")
        String email,

        @Schema(description = "Telefone de contato do cliente", example = "11912345678")
        String telefone,

        @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123, Bairro Jardim, Cidade, Estado, CEP 12345-678")
        String endereco,

        @Schema(description = "Indica se o cliente está ativo ou inativo", example = "true")
        boolean ativo,

        @Schema(description = "Data e hora de criação do cliente", example = "2024-01-01T12:00:00")
        LocalDateTime dataCadastro
) {
    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.isAtivo(),
                cliente.getDataCadastro()
        );
    }
}
