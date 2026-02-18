package com.senai.npsv_gestor_tintas_backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "item_venda")
public class ItemVenda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal quantidade;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal precoPraticado;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
