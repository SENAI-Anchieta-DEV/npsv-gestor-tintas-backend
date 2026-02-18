package com.senai.npsv_gestor_tintas_backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "item_formula")
public class ItemFormula {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal quantidadeNecessaria;

    @NotNull
    @Column(nullable = false)
    private Integer ordemAdicao;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Produto insumo;
}
