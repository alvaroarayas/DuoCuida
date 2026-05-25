package com.duocuida.planes.repository;

import com.duocuida.planes.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByEvaluacionId(Long evaluacionId);
    List<Plan> findByEstado(String estado);
    List<Plan> findByGestorId(Long gestorId);
}