package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Cliente;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        String id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String endereco,
        boolean ativo,
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
