package com.duocuida.beneficios.controller;

import com.duocuida.beneficios.dto.BeneficioRequestDTO;
import com.duocuida.beneficios.dto.BeneficioResponseDTO;
import com.duocuida.beneficios.service.BeneficioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private static final Logger log = LoggerFactory.getLogger(BeneficioController.class);

    private final BeneficioService beneficioService;

    public BeneficioController(BeneficioService beneficioService) {
        this.beneficioService = beneficioService;
    }

    @GetMapping
    public ResponseEntity<List<BeneficioResponseDTO>> getAll() {
        log.info("GET /api/beneficios");
        return ResponseEntity.ok(beneficioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> getById(@PathVariable Long id) {
        log.info("GET /api/beneficios/{}", id);
        return ResponseEntity.ok(beneficioService.findById(id));
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<BeneficioResponseDTO>> getByEstudiante(@PathVariable Long estudianteId) {
        log.info("GET /api/beneficios/estudiante/{}", estudianteId);
        return ResponseEntity.ok(beneficioService.findByEstudianteId(estudianteId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<BeneficioResponseDTO>> getByEstado(@PathVariable String estado) {
        log.info("GET /api/beneficios/estado/{}", estado);
        return ResponseEntity.ok(beneficioService.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<BeneficioResponseDTO> create(@Valid @RequestBody BeneficioRequestDTO dto) {
        log.info("POST /api/beneficios - estudianteId: {}", dto.getEstudianteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(beneficioService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> update(@PathVariable Long id,
                                                       @Valid @RequestBody BeneficioRequestDTO dto) {
        log.info("PUT /api/beneficios/{}", id);
        return ResponseEntity.ok(beneficioService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/beneficios/{}", id);
        beneficioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}