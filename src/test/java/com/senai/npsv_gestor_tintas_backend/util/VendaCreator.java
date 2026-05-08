package com.senai.npsv_gestor_tintas_backend.util;

import com.senai.npsv_gestor_tintas_backend.application.dto.ConcluirVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.IniciarVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.ItemVendaRequestDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Venda;
import com.senai.npsv_gestor_tintas_backend.domain.enums.FormaPagamento;
import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusVenda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaCreator {

    public static Venda criarVendaAbertaNova() {
        return Venda.builder()
                .status(StatusVenda.ABERTA)
                .dataAbertura(LocalDateTime.now())
                .valorTotal(BigDecimal.ZERO)
                .itens(new ArrayList<>())
                .build();
    }

    public static Venda criarVendaAbertaSalva() {
        Venda venda = criarVendaAbertaNova();
        venda.setId("venda-123");
        venda.setVendedor(UsuarioCreator.criarUsuarioAdminSalvo());
        return venda;
    }

    public static Venda criarVendaConcluidaSalva() {
        Venda venda = criarVendaAbertaSalva();
        venda.setStatus(StatusVenda.CONCLUIDA);
        venda.setDataFechamento(LocalDateTime.now());
        venda.setValorTotal(new BigDecimal("250.00"));
        return venda;
    }

    public static IniciarVendaRequestDTO criarIniciarVendaRequestDTO(String vendedorId) {
        return new IniciarVendaRequestDTO(vendedorId);
    }

    public static ItemVendaRequestDTO criarItemVendaRequestDTO(String produtoId, String quantidade) {
        return new ItemVendaRequestDTO(produtoId, new BigDecimal(quantidade));
    }

    public static ConcluirVendaRequestDTO criarConcluirVendaRequestDTO(FormaPagamento formaPagamento, List<ItemVendaRequestDTO> itens) {
        return new ConcluirVendaRequestDTO(formaPagamento, itens);
    }
}