package com.duocuida.atenciones.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AtencionResponseDTO {

    private Long id;
    private Long solicitudId;
    private Long gestorId;
    private String tipo;
    private String descripcion;
    private String estado;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDateTime createdAt;
}