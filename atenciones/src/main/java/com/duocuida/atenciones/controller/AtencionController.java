package com.duocuida.atenciones.controller;

import com.duocuida.atenciones.dto.AtencionRequestDTO;
import com.duocuida.atenciones.dto.AtencionResponseDTO;
import com.duocuida.atenciones.service.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
public class AtencionController {

    private static final Logger log = LoggerFactory.getLogger(AtencionController.class);

    private final AtencionService service;

    @GetMapping
    public ResponseEntity<List<AtencionResponseDTO>> obtenerTodos() {
        log.info("GET /api/atenciones");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtencionResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/atenciones/{}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/solicitud/{solicitudId}")
    public ResponseEntity<List<AtencionResponseDTO>> obtenerPorSolicitud(@PathVariable Long solicitudId) {
        log.info("GET /api/atenciones/solicitud/{}", solicitudId);
        return ResponseEntity.ok(service.obtenerPorSolicitud(solicitudId));
    }

    @GetMapping("/estado")
    public ResponseEntity<List<AtencionResponseDTO>> obtenerPorEstado(@RequestParam String valor) {
        log.info("GET /api/atenciones/estado?valor={}", valor);
        return ResponseEntity.ok(service.obtenerPorEstado(valor));
    }

    @PostMapping
    public ResponseEntity<AtencionResponseDTO> crear(@Valid @RequestBody AtencionRequestDTO dto) {
        log.info("POST /api/atenciones");
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtencionResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody AtencionRequestDTO dto) {
        log.info("PUT /api/atenciones/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/atenciones/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}