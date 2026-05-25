package com.duocuida.planes.controller;

import com.duocuida.planes.dto.PlanRequestDTO;
import com.duocuida.planes.dto.PlanResponseDTO;
import com.duocuida.planes.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanController {

    private static final Logger log = LoggerFactory.getLogger(PlanController.class);

    private final PlanService service;

    @GetMapping
    public ResponseEntity<List<PlanResponseDTO>> obtenerTodos() {
        log.info("GET /api/planes");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/planes/{}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    public ResponseEntity<List<PlanResponseDTO>> obtenerPorEvaluacion(@PathVariable Long evaluacionId) {
        log.info("GET /api/planes/evaluacion/{}", evaluacionId);
        return ResponseEntity.ok(service.obtenerPorEvaluacion(evaluacionId));
    }

    @GetMapping("/estado")
    public ResponseEntity<List<PlanResponseDTO>> obtenerPorEstado(@RequestParam String valor) {
        log.info("GET /api/planes/estado?valor={}", valor);
        return ResponseEntity.ok(service.obtenerPorEstado(valor));
    }

    @PostMapping
    public ResponseEntity<PlanResponseDTO> crear(@Valid @RequestBody PlanRequestDTO dto) {
        log.info("POST /api/planes");
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponseDTO> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody PlanRequestDTO dto) {
        log.info("PUT /api/planes/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/planes/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}