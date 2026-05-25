package com.duocuida.planes.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PlanResponseDTO {

    private Long id;
    private Long evaluacionId;
    private Long gestorId;
    private String descripcion;
    private String objetivo;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime createdAt;
}