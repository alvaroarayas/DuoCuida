package com.duocuida.auth.dto;

import lombok.Data;

@Data
public class UsuarioDTO
{

    private Long id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private Boolean activo;
    private RolDTO rol;
}