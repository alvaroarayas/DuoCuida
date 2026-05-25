package com.duocuida.evaluaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EvaluacionRequestDTO {

    @NotNull(message = "El solicitud_id es obligatorio")
    private Long solicitudId;

    @NotNull(message = "El gestor_id es obligatorio")
    private Long gestorId;

    @NotBlank(message = "El resultado es obligatorio")
    @Pattern(regexp = "APROBADO|RECHAZADO|PENDIENTE", message = "Resultado debe ser APROBADO, RECHAZADO o PENDIENTE")
    private String resultado;

    private String observacion;

    @Min(value = 0, message = "El puntaje no puede ser negativo")
    @Max(value = 100, message = "El puntaje no puede superar 100")
    private Integer puntaje;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}