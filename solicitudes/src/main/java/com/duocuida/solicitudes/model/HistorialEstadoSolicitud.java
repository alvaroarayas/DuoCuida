package com.duocuida.solicitudes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado_solicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoSolicitud
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estado_anterior_id")
    private EstadoSolicitud estadoAnterior;

    @ManyToOne
    @JoinColumn(name = "estado_nuevo_id", nullable = false)
    private EstadoSolicitud estadoNuevo;

    @Column(nullable = false, length = 500)
    private String observacion;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    @JsonIgnore
    private SolicitudApoyo solicitud;
}