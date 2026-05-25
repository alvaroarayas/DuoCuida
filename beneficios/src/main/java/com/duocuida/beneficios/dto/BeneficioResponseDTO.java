package com.duocuida.beneficios.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BeneficioResponseDTO {

    private Long id;
    private Long estudianteId;
    private String tipo;
    private String descripcion;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDateTime createdAt;
}