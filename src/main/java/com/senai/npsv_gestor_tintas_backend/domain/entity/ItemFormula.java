package com.senai.npsv_gestor_tintas_backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false)
    private BigDecimal quantidadeNecessaria;

    @NotNull
    @Column(nullable = false)
    private Integer ordemAdicao;

    @ManyToOne
    @JoinColumn(name = "formula_id", nullable = false)
    private Formula formula;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private Produto insumo;
}