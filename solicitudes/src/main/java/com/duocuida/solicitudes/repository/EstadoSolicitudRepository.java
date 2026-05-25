package com.duocuida.solicitudes.repository;

import com.duocuida.solicitudes.model.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitud, Long> {

    Optional<EstadoSolicitud> findByDescripcion(String descripcion);
}