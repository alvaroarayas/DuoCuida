package com.duocuida.usuarios.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene formato válido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 255)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "ESTUDIANTE|GESTOR|ADMIN", message = "Rol debe ser ESTUDIANTE, GESTOR o ADMIN")
    private String rol;
}