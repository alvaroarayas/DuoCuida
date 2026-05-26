package com.duocuida.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacionRequestDTO
{

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    private Long solicitudId;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}