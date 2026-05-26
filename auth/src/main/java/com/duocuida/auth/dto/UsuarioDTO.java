package com.duocuida.auth.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
    private Boolean activo;
}