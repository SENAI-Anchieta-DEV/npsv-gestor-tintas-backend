package com.senai.npsv_gestor_tintas_backend.domain.entity;

import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
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
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigoBarras;

    @NotBlank
    @Column(nullable = false)
    private String descricao;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal quantidadeEstoque;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal precoCusto;

    @NotBlank
    @Column(nullable = false)
    private BigDecimal precoVenda;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnidadeMedida unidadeMedida;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProduto categoria;
}
