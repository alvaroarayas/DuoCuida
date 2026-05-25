package com.duocuida.derivaciones.repository;

import com.duocuida.derivaciones.model.Derivacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DerivacionRepository extends JpaRepository<Derivacion, Long> {

    List<Derivacion> findBySolicitudId(Long solicitudId);
    List<Derivacion> findByEstado(String estado);
    List<Derivacion> findByGestorId(Long gestorId);
    List<Derivacion> findByUnidadDestino(String unidadDestino);
}