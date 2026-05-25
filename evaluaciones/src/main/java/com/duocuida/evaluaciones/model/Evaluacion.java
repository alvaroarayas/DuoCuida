package com.duocuida.evaluaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluacion")
@Data
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solicitud_id", nullable = false)
    private Long solicitudId;

    @Column(name = "gestor_id", nullable = false)
    private Long gestorId;

    @Column(nullable = false, length = 50)
    private String resultado;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    private Integer puntaje;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}