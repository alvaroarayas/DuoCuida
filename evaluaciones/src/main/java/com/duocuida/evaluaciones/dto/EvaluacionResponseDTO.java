package com.duocuida.evaluaciones.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EvaluacionResponseDTO {

    private Long id;
    private Long solicitudId;
    private Long gestorId;
    private String resultado;
    private String observacion;
    private Integer puntaje;
    private LocalDate fecha;
    private LocalDateTime createdAt;
}