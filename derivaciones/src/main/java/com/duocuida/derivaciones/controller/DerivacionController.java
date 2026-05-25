package com.duocuida.derivaciones.controller;

import com.duocuida.derivaciones.dto.DerivacionRequestDTO;
import com.duocuida.derivaciones.dto.DerivacionResponseDTO;
import com.duocuida.derivaciones.service.DerivacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/derivaciones")
@RequiredArgsConstructor
public class DerivacionController {

    private static final Logger log = LoggerFactory.getLogger(DerivacionController.class);

    private final DerivacionService service;

    @GetMapping
    public ResponseEntity<List<DerivacionResponseDTO>> obtenerTodos() {
        log.info("GET /api/derivaciones");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DerivacionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/derivaciones/{}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<DerivacionResponseDTO>> obtenerPorSolicitud(@PathVariable Long solicitudId) {
        log.info("GET /api/derivaciones/solicitud/{}", solicitudId);
        return ResponseEntity.ok(service.obtenerPorSolicitud(solicitudId));
    }

    @GetMapping("/estado")
    public ResponseEntity<List<DerivacionResponseDTO>> obtenerPorEstado(@RequestParam String valor) {
        log.info("GET /api/derivaciones/estado?valor={}", valor);
        return ResponseEntity.ok(service.obtenerPorEstado(valor));
    }

    @PostMapping
    public ResponseEntity<DerivacionResponseDTO> crear(@Valid @RequestBody DerivacionRequestDTO dto) {
        log.info("POST /api/derivaciones");
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DerivacionResponseDTO> actualizar(@PathVariable Long id,
                                                            @Valid @RequestBody DerivacionRequestDTO dto) {
        log.info("PUT /api/derivaciones/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/derivaciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}