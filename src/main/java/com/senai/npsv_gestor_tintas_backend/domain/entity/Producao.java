package com.senai.npsv_gestor_tintas_backend.domain.entity;

import com.senai.npsv_gestor_tintas_backend.domain.enums.StatusProducao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "producao")
public class Producao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProducao status;

    @ManyToOne
    @JoinColumn(name = "colorista_id")
    private Usuario colorista;

    @ManyToOne
    @JoinColumn(name = "formula_id")
    private Formula formula;
}
