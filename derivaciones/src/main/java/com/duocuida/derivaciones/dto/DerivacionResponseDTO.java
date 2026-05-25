package com.duocuida.derivaciones.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DerivacionResponseDTO {

    private Long id;
    private Long solicitudId;
    private Long gestorId;
    private String unidadDestino;
    private String motivo;
    private String estado;
    private LocalDate fecha;
    private LocalDateTime createdAt;
}