package com.duocuida.solicitudes.repository;

import com.duocuida.solicitudes.model.SolicitudApoyo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudApoyoRepository extends JpaRepository<SolicitudApoyo, Long> {

    List<SolicitudApoyo> findByPerfilEstudianteId(Long perfilEstudianteId);
}