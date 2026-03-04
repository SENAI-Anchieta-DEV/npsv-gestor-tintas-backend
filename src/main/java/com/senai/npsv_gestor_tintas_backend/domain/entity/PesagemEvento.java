package com.senai.npsv_gestor_tintas_backend.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pesagem_evento")
public class PesagemEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @Column(nullable = false)
    private BigDecimal pesoLido;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;

    private boolean foiAprovado;

    @ManyToOne
    @JoinColumn(name = "producao_id")
    private Producao producao;
}