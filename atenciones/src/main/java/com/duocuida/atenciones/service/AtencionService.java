package com.duocuida.atenciones.service;

import com.duocuida.atenciones.client.SolicitudClient;
import com.duocuida.atenciones.dto.AtencionRequestDTO;
import com.duocuida.atenciones.dto.AtencionResponseDTO;
import com.duocuida.atenciones.exception.ResourceNotFoundException;
import com.duocuida.atenciones.model.Atencion;
import com.duocuida.atenciones.repository.AtencionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtencionService {

    private static final Logger log = LoggerFactory.getLogger(AtencionService.class);

    private final AtencionRepository repository;
    private final SolicitudClient solicitudClient;

    public List<AtencionResponseDTO> obtenerTodos() {
        log.info("Obteniendo todas las atenciones");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public AtencionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando atencion con id {}", id);
        Atencion atencion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));
        return toDTO(atencion);
    }

    public List<AtencionResponseDTO> obtenerPorSolicitud(Long solicitudId) {
        log.info("Buscando atenciones por solicitud_id {}", solicitudId);
        return repository.findBySolicitudId(solicitudId).stream().map(this::toDTO).toList();
    }

    public List<AtencionResponseDTO> obtenerPorEstado(String estado) {
        log.info("Buscando atenciones por estado: {}", estado);
        return repository.findByEstado(estado).stream().map(this::toDTO).toList();
    }

    public AtencionResponseDTO crear(AtencionRequestDTO dto) {
        log.info("Creando atencion para solicitud_id {}", dto.getSolicitudId());
        if (!solicitudClient.existeSolicitud(dto.getSolicitudId())) {
            throw new ResourceNotFoundException("Solicitud no encontrada con id: " + dto.getSolicitudId());
        }
        Atencion atencion = toEntity(dto);
        Atencion guardada = repository.save(atencion);
        log.info("Atencion creada con id {}", guardada.getId());
        return toDTO(guardada);
    }

    public AtencionResponseDTO actualizar(Long id, AtencionRequestDTO dto) {
        log.info("Actualizando atencion con id {}", id);
        Atencion atencion = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atencion no encontrada con id: " + id));
        atencion.setSolicitudId(dto.getSolicitudId());
        atencion.setGestorId(dto.getGestorId());
        atencion.setTipo(dto.getTipo());
        atencion.setDescripcion(dto.getDescripcion());
        atencion.setEstado(dto.getEstado());
        atencion.setFecha(dto.getFecha());
        atencion.setHora(dto.getHora());
        return toDTO(repository.save(atencion));
    }

    public void eliminar(Long id) {
        log.info("Eliminando atencion con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Atencion no encontrada con id: " + id);
        }
        repository.deleteById(id);
        log.info("Atencion con id {} eliminada", id);
    }

    private AtencionResponseDTO toDTO(Atencion a) {
        AtencionResponseDTO dto = new AtencionResponseDTO();
        dto.setId(a.getId());
        dto.setSolicitudId(a.getSolicitudId());
        dto.setGestorId(a.getGestorId());
        dto.setTipo(a.getTipo());
        dto.setDescripcion(a.getDescripcion());
        dto.setEstado(a.getEstado());
        dto.setFecha(a.getFecha());
        dto.setHora(a.getHora());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }

    private Atencion toEntity(AtencionRequestDTO dto) {
        Atencion a = new Atencion();
        a.setSolicitudId(dto.getSolicitudId());
        a.setGestorId(dto.getGestorId());
        a.setTipo(dto.getTipo());
        a.setDescripcion(dto.getDescripcion());
        a.setEstado(dto.getEstado());
        a.setFecha(dto.getFecha());
        a.setHora(dto.getHora());
        return a;
    }
}