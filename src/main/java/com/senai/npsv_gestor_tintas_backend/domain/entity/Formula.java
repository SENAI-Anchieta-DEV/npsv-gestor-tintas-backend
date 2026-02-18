package com.senai.npsv_gestor_tintas_backend.domain.entity;

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
@Table(name = "formula")
public class Formula {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String codigoInterno;

    @NotBlank
    @Column(nullable = false)
    private String nomeCor;

    @NotBlank
    @Column(nullable = false)
    private LocalDateTime dataCriacao;
}
