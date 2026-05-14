package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PedidoRequestDTO(
        @NotBlank(message = "O ID do fornecedor é obrigatório.")
        String fornecedorId,

        LocalDate dataPrevisaoEntrega,

        String observacao,

        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        List<@NotNull @Valid ItemPedidoRequestDTO> itens
) {
        public Pedido toEntity(Usuario admin, Fornecedor fornecedor) {
                return Pedido.builder()
                        .fornecedor(fornecedor)
                        .admin(admin)
                        .status(StatusPedido.PENDENTE)
                        .dataPedido(LocalDateTime.now())
                        .dataPrevisaoEntrega(this.dataPrevisaoEntrega)
                        .observacao(this.observacao)
                        .itens(new ArrayList<>())
                        .build();
        }
}
