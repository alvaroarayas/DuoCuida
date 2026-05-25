package com.duocuida.perfiles.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PerfilRequestDTO {

    @NotNull(message = "El usuario_id es obligatorio")
    private Long usuarioId;

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

    @Size(max = 20)
    private String telefono;

    @NotBlank(message = "La carrera es obligatoria")
    @Size(max = 150)
    private String carrera;

    @NotBlank(message = "La sede es obligatoria")
    @Size(max = 100)
    private String sede;
}
