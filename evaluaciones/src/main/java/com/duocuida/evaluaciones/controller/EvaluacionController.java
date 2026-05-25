package com.duocuida.evaluaciones.controller;

import com.duocuida.evaluaciones.dto.EvaluacionRequestDTO;
import com.duocuida.evaluaciones.dto.EvaluacionResponseDTO;
import com.duocuida.evaluaciones.service.EvaluacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionController.class);

    private final EvaluacionService service;

    @GetMapping
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerTodos() {
        log.info("GET /api/evaluaciones");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/evaluaciones/{}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerPorSolicitud(@PathVariable Long solicitudId) {
        log.info("GET /api/evaluaciones/solicitud/{}", solicitudId);
        return ResponseEntity.ok(service.obtenerPorSolicitud(solicitudId));
    }

    @GetMapping("/resultado")
    public ResponseEntity<List<EvaluacionResponseDTO>> obtenerPorResultado(@RequestParam String valor) {
        log.info("GET /api/evaluaciones/resultado?valor={}", valor);
        return ResponseEntity.ok(service.obtenerPorResultado(valor));
    }

    @PostMapping
    public ResponseEntity<EvaluacionResponseDTO> crear(@Valid @RequestBody EvaluacionRequestDTO dto) {
        log.info("POST /api/evaluaciones");
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluacionResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody EvaluacionRequestDTO dto) {
        log.info("PUT /api/evaluaciones/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/evaluaciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}