package com.duocuida.perfiles.controller;

import com.duocuida.perfiles.dto.PerfilRequestDTO;
import com.duocuida.perfiles.dto.PerfilResponseDTO;
import com.duocuida.perfiles.service.PerfilEstudianteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilEstudianteController {

    private static final Logger log = LoggerFactory.getLogger(PerfilEstudianteController.class);

    private final PerfilEstudianteService service;

    @GetMapping
    public ResponseEntity<List<PerfilResponseDTO>> obtenerTodos() {
        log.info("GET /api/perfiles");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/perfiles/{}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/carrera")
    public ResponseEntity<List<PerfilResponseDTO>> obtenerPorCarrera(@RequestParam String nombre) {
        log.info("GET /api/perfiles/carrera?nombre={}", nombre);
        return ResponseEntity.ok(service.obtenerPorCarrera(nombre));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PerfilResponseDTO>> obtenerActivos() {
        log.info("GET /api/perfiles/activos");
        return ResponseEntity.ok(service.obtenerActivos());
    }

    @PostMapping
    public ResponseEntity<PerfilResponseDTO> crear(@Valid @RequestBody PerfilRequestDTO dto) {
        log.info("POST /api/perfiles");
        return ResponseEntity.status(201).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerfilResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody PerfilRequestDTO dto) {
        log.info("PUT /api/perfiles/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/perfiles/{}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}