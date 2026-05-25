package com.duocuida.evaluaciones.repository;

import com.duocuida.evaluaciones.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findBySolicitudId(Long solicitudId);
    List<Evaluacion> findByResultado(String resultado);
    List<Evaluacion> findByGestorId(Long gestorId);
}