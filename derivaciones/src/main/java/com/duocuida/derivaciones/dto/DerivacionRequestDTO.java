package com.duocuida.derivaciones.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DerivacionRequestDTO {

    @NotNull(message = "El solicitud_id es obligatorio")
    private Long solicitudId;

    @NotNull(message = "El gestor_id es obligatorio")
    private Long gestorId;

    @NotBlank(message = "La unidad destino es obligatoria")
    @Size(max = 150)
    private String unidadDestino;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PENDIENTE|EN_PROCESO|COMPLETADO|CANCELADO", message = "Estado inválido")
    private String estado;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}