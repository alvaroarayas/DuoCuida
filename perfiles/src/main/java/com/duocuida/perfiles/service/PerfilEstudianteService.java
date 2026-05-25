package com.duocuida.perfiles.service;

import com.duocuida.perfiles.dto.PerfilRequestDTO;
import com.duocuida.perfiles.dto.PerfilResponseDTO;
import com.duocuida.perfiles.model.PerfilEstudiante;
import com.duocuida.perfiles.repository.PerfilEstudianteRepository;
import com.duocuida.perfiles.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilEstudianteService {

    private static final Logger log = LoggerFactory.getLogger(PerfilEstudianteService.class);

    private final PerfilEstudianteRepository repository;

    public List<PerfilResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los perfiles");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public PerfilResponseDTO obtenerPorId(Long id) {
        log.info("Buscando perfil con id {}", id);
        PerfilEstudiante perfil = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con id: " + id));
        return toDTO(perfil);
    }

    public List<PerfilResponseDTO> obtenerPorCarrera(String carrera) {
        log.info("Buscando perfiles por carrera: {}", carrera);
        return repository.findByCarrera(carrera).stream().map(this::toDTO).toList();
    }

    public List<PerfilResponseDTO> obtenerActivos() {
        log.info("Obteniendo perfiles activos");
        return repository.findByActivo(true).stream().map(this::toDTO).toList();
    }

    public PerfilResponseDTO crear(PerfilRequestDTO dto) {
        log.info("Creando perfil para usuario_id {}", dto.getUsuarioId());
        PerfilEstudiante perfil = toEntity(dto);
        PerfilEstudiante guardado = repository.save(perfil);
        log.info("Perfil creado con id {}", guardado.getId());
        return toDTO(guardado);
    }

    public PerfilResponseDTO actualizar(Long id, PerfilRequestDTO dto) {
        log.info("Actualizando perfil con id {}", id);
        PerfilEstudiante perfil = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado con id: " + id));
        perfil.setNombre(dto.getNombre());
        perfil.setApellido(dto.getApellido());
        perfil.setEmail(dto.getEmail());
        perfil.setTelefono(dto.getTelefono());
        perfil.setCarrera(dto.getCarrera());
        perfil.setSede(dto.getSede());
        return toDTO(repository.save(perfil));
    }

    public void eliminar(Long id) {
        log.info("Eliminando perfil con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Perfil no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Perfil con id {} eliminado", id);
    }

    private PerfilResponseDTO toDTO(PerfilEstudiante p) {
        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(p.getId());
        dto.setUsuarioId(p.getUsuarioId());
        dto.setNombre(p.getNombre());
        dto.setApellido(p.getApellido());
        dto.setEmail(p.getEmail());
        dto.setTelefono(p.getTelefono());
        dto.setCarrera(p.getCarrera());
        dto.setSede(p.getSede());
        dto.setActivo(p.getActivo());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }

    private PerfilEstudiante toEntity(PerfilRequestDTO dto) {
        PerfilEstudiante p = new PerfilEstudiante();
        p.setUsuarioId(dto.getUsuarioId());
        p.setNombre(dto.getNombre());
        p.setApellido(dto.getApellido());
        p.setEmail(dto.getEmail());
        p.setTelefono(dto.getTelefono());
        p.setCarrera(dto.getCarrera());
        p.setSede(dto.getSede());
        return p;
    }
}
