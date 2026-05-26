package com.duocuida.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambioEstadoRequestDTO
{

    @NotNull(message = "El ID del nuevo estado es obligatorio")
    private Long nuevoEstadoId;

    @NotBlank(message = "La observación es obligatoria")
    private String observacion;
}