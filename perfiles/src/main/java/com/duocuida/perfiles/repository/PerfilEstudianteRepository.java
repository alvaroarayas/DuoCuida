package com.duocuida.perfiles.repository;

import com.duocuida.perfiles.model.PerfilEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PerfilEstudianteRepository extends JpaRepository<PerfilEstudiante, Long> {

    Optional<PerfilEstudiante> findByEmail(String email);
    List<PerfilEstudiante> findByActivo(Boolean activo);
    List<PerfilEstudiante> findByCarrera(String carrera);
    boolean existsByEmail(String email);
}
