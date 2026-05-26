package com.duocuida.auth.dto;

import lombok.Data;

@Data
public class UsuarioDTO
{

    private Long id;
    private String nombres;
    private String apellido;
    private String email;
    private RolDTO rol;
    private Boolean activo;

}