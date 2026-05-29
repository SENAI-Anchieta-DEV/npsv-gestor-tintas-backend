package com.senai.npsv_gestor_tintas_backend.application.dto;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Fornecedor;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Pedido;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PedidoRequestDTO(
        @Schema(description = "Identificador do fornecedor para quem o pedido está sendo realizado", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "O ID do fornecedor é obrigatório.")
        String fornecedorId,

        @Schema(description = "Data prevista para entrega do pedido", example = "2026-05-31")
        LocalDate dataPrevisaoEntrega,

        @Schema(description = "Nota de observação relaciodada ao pedido")
        String observacao,

        @Schema(description = "Lista de itens que compõem o pedido")
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
