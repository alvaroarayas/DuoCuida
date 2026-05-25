package com.duocuida.beneficios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BeneficioRequestDTO {

    @NotNull(message = "El id del estudiante es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "BECA|APOYO_PSICOLOGICO|APOYO_SOCIAL|TUTORIA|ALIMENTACION",
            message = "Tipo debe ser: BECA, APOYO_PSICOLOGICO, APOYO_SOCIAL, TUTORIA o ALIMENTACION")
    private String tipo;

    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "ACTIVO|INACTIVO|SUSPENDIDO",
            message = "Estado debe ser: ACTIVO, INACTIVO o SUSPENDIDO")
    private String estado;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}