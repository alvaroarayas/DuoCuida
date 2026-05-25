package com.duocuida.beneficios.repository;

import com.duocuida.beneficios.model.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<Beneficio, Long> {

    List<Beneficio> findByEstudianteId(Long estudianteId);
    List<Beneficio> findByEstado(String estado);
    List<Beneficio> findByTipo(String tipo);
}