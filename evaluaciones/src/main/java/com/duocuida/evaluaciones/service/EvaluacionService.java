package com.duocuida.evaluaciones.service;

import com.duocuida.evaluaciones.dto.EvaluacionRequestDTO;
import com.duocuida.evaluaciones.dto.EvaluacionResponseDTO;
import com.duocuida.evaluaciones.exception.ResourceNotFoundException;
import com.duocuida.evaluaciones.model.Evaluacion;
import com.duocuida.evaluaciones.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluacionService {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionService.class);

    private final EvaluacionRepository repository;

    public List<EvaluacionResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las evaluaciones");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public EvaluacionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando evaluacion con id {}", id);
        Evaluacion evaluacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id: " + id));
        return toDTO(evaluacion);
    }

    public List<EvaluacionResponseDTO> obtenerPorSolicitud(Long solicitudId) {
        log.info("Buscando evaluaciones por solicitud_id {}", solicitudId);
        return repository.findBySolicitudId(solicitudId).stream().map(this::toDTO).toList();
    }

    public List<EvaluacionResponseDTO> obtenerPorResultado(String resultado) {
        log.info("Buscando evaluaciones por resultado: {}", resultado);
        return repository.findByResultado(resultado).stream().map(this::toDTO).toList();
    }

    public EvaluacionResponseDTO crear(EvaluacionRequestDTO dto) {
        log.info("Creando evaluacion para solicitud_id {}", dto.getSolicitudId());
        Evaluacion evaluacion = toEntity(dto);
        Evaluacion guardada = repository.save(evaluacion);
        log.info("Evaluacion creada con id {}", guardada.getId());
        return toDTO(guardada);
    }

    public EvaluacionResponseDTO actualizar(Long id, EvaluacionRequestDTO dto) {
        log.info("Actualizando evaluacion con id {}", id);
        Evaluacion evaluacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada con id: " + id));
        evaluacion.setSolicitudId(dto.getSolicitudId());
        evaluacion.setGestorId(dto.getGestorId());
        evaluacion.setResultado(dto.getResultado());
        evaluacion.setObservacion(dto.getObservacion());
        evaluacion.setPuntaje(dto.getPuntaje());
        evaluacion.setFecha(dto.getFecha());
        return toDTO(repository.save(evaluacion));
    }

    public void eliminar(Long id) {
        log.info("Eliminando evaluacion con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Evaluacion no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Evaluacion con id {} eliminada", id);
    }

    private EvaluacionResponseDTO toDTO(Evaluacion e) {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setId(e.getId());
        dto.setSolicitudId(e.getSolicitudId());
        dto.setGestorId(e.getGestorId());
        dto.setResultado(e.getResultado());
        dto.setObservacion(e.getObservacion());
        dto.setPuntaje(e.getPuntaje());
        dto.setFecha(e.getFecha());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }

    private Evaluacion toEntity(EvaluacionRequestDTO dto) {
        Evaluacion e = new Evaluacion();
        e.setSolicitudId(dto.getSolicitudId());
        e.setGestorId(dto.getGestorId());
        e.setResultado(dto.getResultado());
        e.setObservacion(dto.getObservacion());
        e.setPuntaje(dto.getPuntaje());
        e.setFecha(dto.getFecha());
        return e;
    }
}