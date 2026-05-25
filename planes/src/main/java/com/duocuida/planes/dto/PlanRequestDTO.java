package com.duocuida.planes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PlanRequestDTO {

    @NotNull(message = "El evaluacion_id es obligatorio")
    private Long evaluacionId;

    @NotNull(message = "El gestor_id es obligatorio")
    private Long gestorId;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcion;

    @NotBlank(message = "El objetivo es obligatorio")
    @Size(max = 255)
    private String objetivo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|COMPLETADO|CANCELADO", message = "Estado debe ser ACTIVO, COMPLETADO o CANCELADO")
    private String estado;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}