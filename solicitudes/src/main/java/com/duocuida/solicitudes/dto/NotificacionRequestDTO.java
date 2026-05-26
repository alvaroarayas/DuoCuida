package com.duocuida.solicitudes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionRequestDTO
{

    private Long usuarioId;
    private Long solicitudId;
    private String titulo;
    private String mensaje;
}