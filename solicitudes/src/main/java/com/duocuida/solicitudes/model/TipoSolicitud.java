package com.duocuida.solicitudes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_solicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoSolicitud
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;
}