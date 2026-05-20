package com.senai.npsv_gestor_tintas_backend.domain.entity;

import com.senai.npsv_gestor_tintas_backend.domain.enums.ResultadoRN01;
import com.senai.npsv_gestor_tintas_backend.domain.enums.UnidadeMedida;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
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
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private BigDecimal pesoLido;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Mantido para compatibilidade com o PesagemEventoService REST existente.
    // Derivado de resultadoRN01: true quando APROVADO.
    @Column(nullable = false)
    private boolean foiAprovado;

    // --- Campos novos do contrato MQTT ---

    // Unidade enviada pelo ESP32 conforme o Enum do contrato NPSV-116
    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida")
    private UnidadeMedida unidadeMedida;

    // Flag de estabilidade calculada no firmware (RN01 — margem relativa 5%)
    @Column(name = "estavel")
    private Boolean estavel;

    // Resultado da validação RN01 calculada no backend
    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_rn01")
    private ResultadoRN01 resultadoRN01;

    @ManyToOne
    @JoinColumn(name = "producao_id")
    private Producao producao;
}