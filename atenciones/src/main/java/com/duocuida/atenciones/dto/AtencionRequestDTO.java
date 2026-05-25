package com.duocuida.atenciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AtencionRequestDTO {

    @NotNull(message = "El solicitud_id es obligatorio")
    private Long solicitudId;

    @NotNull(message = "El gestor_id es obligatorio")
    private Long gestorId;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 100)
    private String tipo;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "AGENDADA|COMPLETADA|CANCELADA", message = "Estado debe ser AGENDADA, COMPLETADA o CANCELADA")
    private String estado;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
}