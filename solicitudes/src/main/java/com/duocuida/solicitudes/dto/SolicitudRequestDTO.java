package com.duocuida.solicitudes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitudRequestDTO
{

    @NotNull(message = "El ID del perfil estudiante es obligatorio")
    private Long perfilEstudianteId;

    @NotNull(message = "El ID del tipo de solicitud es obligatorio")
    private Long tipoSolicitudId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "Los antecedentes son obligatorios")
    private String antecedentes;
}