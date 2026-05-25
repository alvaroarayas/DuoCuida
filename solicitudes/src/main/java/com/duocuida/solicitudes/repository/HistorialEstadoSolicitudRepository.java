package com.duocuida.solicitudes.repository;

import com.duocuida.solicitudes.model.HistorialEstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoSolicitudRepository extends JpaRepository<HistorialEstadoSolicitud, Long> {

    List<HistorialEstadoSolicitud> findBySolicitudId(Long solicitudId);
}