package com.duocuida.solicitudes.repository;

import com.duocuida.solicitudes.model.TipoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoSolicitudRepository extends JpaRepository<TipoSolicitud, Long> {
}