package com.duocuida.derivaciones.service;

import com.duocuida.derivaciones.dto.DerivacionRequestDTO;
import com.duocuida.derivaciones.dto.DerivacionResponseDTO;
import com.duocuida.derivaciones.exception.ResourceNotFoundException;
import com.duocuida.derivaciones.model.Derivacion;
import com.duocuida.derivaciones.repository.DerivacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DerivacionService {

    private static final Logger log = LoggerFactory.getLogger(DerivacionService.class);

    private final DerivacionRepository repository;

    public List<DerivacionResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las derivaciones");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public DerivacionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando derivacion con id {}", id);
        Derivacion derivacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Derivacion no encontrada con id: " + id));
        return toDTO(derivacion);
    }

    public List<DerivacionResponseDTO> obtenerPorSolicitud(Long solicitudId) {
        log.info("Buscando derivaciones por solicitud_id {}", solicitudId);
        return repository.findBySolicitudId(solicitudId).stream().map(this::toDTO).toList();
    }

    public List<DerivacionResponseDTO> obtenerPorEstado(String estado) {
        log.info("Buscando derivaciones por estado: {}", estado);
        return repository.findByEstado(estado).stream().map(this::toDTO).toList();
    }

    public DerivacionResponseDTO crear(DerivacionRequestDTO dto) {
        log.info("Creando derivacion para solicitud_id {}", dto.getSolicitudId());
        Derivacion derivacion = toEntity(dto);
        Derivacion guardada = repository.save(derivacion);
        log.info("Derivacion creada con id {}", guardada.getId());
        return toDTO(guardada);
    }

    public DerivacionResponseDTO actualizar(Long id, DerivacionRequestDTO dto) {
        log.info("Actualizando derivacion con id {}", id);
        Derivacion derivacion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Derivacion no encontrada con id: " + id));
        derivacion.setSolicitudId(dto.getSolicitudId());
        derivacion.setGestorId(dto.getGestorId());
        derivacion.setUnidadDestino(dto.getUnidadDestino());
        derivacion.setMotivo(dto.getMotivo());
        derivacion.setEstado(dto.getEstado());
        derivacion.setFecha(dto.getFecha());
        return toDTO(repository.save(derivacion));
    }

    public void eliminar(Long id) {
        log.info("Eliminando derivacion con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Derivacion no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Derivacion con id {} eliminada", id);
    }

    private DerivacionResponseDTO toDTO(Derivacion d) {
        DerivacionResponseDTO dto = new DerivacionResponseDTO();
        dto.setId(d.getId());
        dto.setSolicitudId(d.getSolicitudId());
        dto.setGestorId(d.getGestorId());
        dto.setUnidadDestino(d.getUnidadDestino());
        dto.setMotivo(d.getMotivo());
        dto.setEstado(d.getEstado());
        dto.setFecha(d.getFecha());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }

    private Derivacion toEntity(DerivacionRequestDTO dto) {
        Derivacion d = new Derivacion();
        d.setSolicitudId(dto.getSolicitudId());
        d.setGestorId(dto.getGestorId());
        d.setUnidadDestino(dto.getUnidadDestino());
        d.setMotivo(dto.getMotivo());
        d.setEstado(dto.getEstado());
        d.setFecha(dto.getFecha());
        return d;
    }
}