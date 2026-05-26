package com.duocuida.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {

    private String mensaje;
    private Long usuarioId;
    private String correo;
    private String rol;
}