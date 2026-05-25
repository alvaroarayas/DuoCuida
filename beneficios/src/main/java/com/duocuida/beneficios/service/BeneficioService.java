package com.duocuida.beneficios.service;

import com.duocuida.beneficios.dto.BeneficioRequestDTO;
import com.duocuida.beneficios.dto.BeneficioResponseDTO;
import com.duocuida.beneficios.exception.ResourceNotFoundException;
import com.duocuida.beneficios.model.Beneficio;
import com.duocuida.beneficios.repository.BeneficioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioService {

    private static final Logger log = LoggerFactory.getLogger(BeneficioService.class);

    private final BeneficioRepository beneficioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    public List<BeneficioResponseDTO> findAll() {
        log.info("Obteniendo todos los beneficios");
        return beneficioRepository.findAll().stream().map(this::toDTO).toList();
    }

    public BeneficioResponseDTO findById(Long id) {
        log.info("Buscando beneficio con id: {}", id);
        Beneficio beneficio = beneficioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficio no encontrado con id: " + id));
        return toDTO(beneficio);
    }

    public List<BeneficioResponseDTO> findByEstudianteId(Long estudianteId) {
        log.info("Buscando beneficios del estudiante id: {}", estudianteId);
        return beneficioRepository.findByEstudianteId(estudianteId).stream().map(this::toDTO).toList();
    }

    public List<BeneficioResponseDTO> findByEstado(String estado) {
        log.info("Buscando beneficios con estado: {}", estado);
        return beneficioRepository.findByEstado(estado).stream().map(this::toDTO).toList();
    }

    public BeneficioResponseDTO save(BeneficioRequestDTO dto) {
        log.info("Creando nuevo beneficio para estudiante id: {}", dto.getEstudianteId());
        Beneficio beneficio = toEntity(dto);
        Beneficio saved = beneficioRepository.save(beneficio);
        log.info("Beneficio creado con id: {}", saved.getId());
        return toDTO(saved);
    }

    public BeneficioResponseDTO update(Long id, BeneficioRequestDTO dto) {
        log.info("Actualizando beneficio con id: {}", id);
        Beneficio beneficio = beneficioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficio no encontrado con id: " + id));
        beneficio.setEstudianteId(dto.getEstudianteId());
        beneficio.setTipo(dto.getTipo());
        beneficio.setDescripcion(dto.getDescripcion());
        beneficio.setEstado(dto.getEstado());
        beneficio.setFechaInicio(dto.getFechaInicio());
        beneficio.setFechaFin(dto.getFechaFin());
        Beneficio updated = beneficioRepository.save(beneficio);
        log.info("Beneficio actualizado con id: {}", updated.getId());
        return toDTO(updated);
    }

    public void delete(Long id) {
        log.info("Eliminando beneficio con id: {}", id);
        if (!beneficioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Beneficio no encontrado con id: " + id);
        }
        beneficioRepository.deleteById(id);
        log.info("Beneficio eliminado con id: {}", id);
    }

    private BeneficioResponseDTO toDTO(Beneficio b) {
        BeneficioResponseDTO dto = new BeneficioResponseDTO();
        dto.setId(b.getId());
        dto.setEstudianteId(b.getEstudianteId());
        dto.setTipo(b.getTipo());
        dto.setDescripcion(b.getDescripcion());
        dto.setEstado(b.getEstado());
        dto.setFechaInicio(b.getFechaInicio());
        dto.setFechaFin(b.getFechaFin());
        dto.setCreatedAt(b.getCreatedAt());
        return dto;
    }

    private Beneficio toEntity(BeneficioRequestDTO dto) {
        Beneficio b = new Beneficio();
        b.setEstudianteId(dto.getEstudianteId());
        b.setTipo(dto.getTipo());
        b.setDescripcion(dto.getDescripcion());
        b.setEstado(dto.getEstado());
        b.setFechaInicio(dto.getFechaInicio());
        b.setFechaFin(dto.getFechaFin());
        return b;
    }
}