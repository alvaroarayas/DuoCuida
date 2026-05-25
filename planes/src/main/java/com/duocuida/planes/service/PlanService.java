package com.duocuida.planes.service;

import com.duocuida.planes.client.EvaluacionClient;
import com.duocuida.planes.dto.PlanRequestDTO;
import com.duocuida.planes.dto.PlanResponseDTO;
import com.duocuida.planes.exception.ResourceNotFoundException;
import com.duocuida.planes.model.Plan;
import com.duocuida.planes.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private static final Logger log = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository repository;
    private final EvaluacionClient evaluacionClient;

    public List<PlanResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los planes");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public PlanResponseDTO obtenerPorId(Long id) {
        log.info("Buscando plan con id {}", id);
        Plan plan = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con id: " + id));
        return toDTO(plan);
    }

    public List<PlanResponseDTO> obtenerPorEvaluacion(Long evaluacionId) {
        log.info("Buscando planes por evaluacion_id {}", evaluacionId);
        return repository.findByEvaluacionId(evaluacionId).stream().map(this::toDTO).toList();
    }

    public List<PlanResponseDTO> obtenerPorEstado(String estado) {
        log.info("Buscando planes por estado: {}", estado);
        return repository.findByEstado(estado).stream().map(this::toDTO).toList();
    }

    public PlanResponseDTO crear(PlanRequestDTO dto) {
        log.info("Creando plan para evaluacion_id {}", dto.getEvaluacionId());
        if (!evaluacionClient.existeEvaluacion(dto.getEvaluacionId())) {
            throw new ResourceNotFoundException("Evaluacion no encontrada con id: " + dto.getEvaluacionId());
        }
        Plan plan = toEntity(dto);
        Plan guardado = repository.save(plan);
        log.info("Plan creado con id {}", guardado.getId());
        return toDTO(guardado);
    }

    public PlanResponseDTO actualizar(Long id, PlanRequestDTO dto) {
        log.info("Actualizando plan con id {}", id);
        Plan plan = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan no encontrado con id: " + id));
        plan.setEvaluacionId(dto.getEvaluacionId());
        plan.setGestorId(dto.getGestorId());
        plan.setDescripcion(dto.getDescripcion());
        plan.setObjetivo(dto.getObjetivo());
        plan.setEstado(dto.getEstado());
        plan.setFechaInicio(dto.getFechaInicio());
        plan.setFechaFin(dto.getFechaFin());
        return toDTO(repository.save(plan));
    }

    public void eliminar(Long id) {
        log.info("Eliminando plan con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Plan no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Plan con id {} eliminado", id);
    }

    private PlanResponseDTO toDTO(Plan p) {
        PlanResponseDTO dto = new PlanResponseDTO();
        dto.setId(p.getId());
        dto.setEvaluacionId(p.getEvaluacionId());
        dto.setGestorId(p.getGestorId());
        dto.setDescripcion(p.getDescripcion());
        dto.setObjetivo(p.getObjetivo());
        dto.setEstado(p.getEstado());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }

    private Plan toEntity(PlanRequestDTO dto) {
        Plan p = new Plan();
        p.setEvaluacionId(dto.getEvaluacionId());
        p.setGestorId(dto.getGestorId());
        p.setDescripcion(dto.getDescripcion());
        p.setObjetivo(dto.getObjetivo());
        p.setEstado(dto.getEstado());
        p.setFechaInicio(dto.getFechaInicio());
        p.setFechaFin(dto.getFechaFin());
        return p;
    }
}