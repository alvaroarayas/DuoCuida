package com.duocuida.solicitudes.dto;

import lombok.Data;

@Data
public class PerfilEstudianteDTO
{

    private Long id;
    private Long usuarioId;
    private String run;
    private String nombres;
    private String apellidos;
    private String carrera;
    private String jornada;
    private Integer anioIngreso;
    private String situacionCuidado;
    private Boolean tieneResponsabilidadCuidado;
}