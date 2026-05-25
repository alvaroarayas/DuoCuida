package com.duocuida.atenciones.repository;

import com.duocuida.atenciones.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    List<Atencion> findBySolicitudId(Long solicitudId);
    List<Atencion> findByEstado(String estado);
    List<Atencion> findByGestorId(Long gestorId);
    List<Atencion> findByTipo(String tipo);
}