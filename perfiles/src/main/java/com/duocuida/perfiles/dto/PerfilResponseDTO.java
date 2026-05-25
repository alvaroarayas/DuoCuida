package com.duocuida.perfiles.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PerfilResponseDTO {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String carrera;
    private String sede;
    private Boolean activo;
    private LocalDateTime createdAt;
}
