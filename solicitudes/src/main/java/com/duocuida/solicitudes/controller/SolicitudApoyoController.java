package com.duocuida.solicitudes.controller;

import com.duocuida.solicitudes.dto.CambioEstadoRequestDTO;
import com.duocuida.solicitudes.dto.SolicitudRequestDTO;
import com.duocuida.solicitudes.model.EstadoSolicitud;
import com.duocuida.solicitudes.model.HistorialEstadoSolicitud;
import com.duocuida.solicitudes.model.SolicitudApoyo;
import com.duocuida.solicitudes.model.TipoSolicitud;
import com.duocuida.solicitudes.service.SolicitudApoyoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudApoyoController
{

    private final SolicitudApoyoService solicitudService;

    public SolicitudApoyoController(SolicitudApoyoService solicitudService)
    {
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudApoyo>> listar()
    {
        return ResponseEntity.ok(solicitudService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudApoyo> buscarPorId(@PathVariable Long id)
    {
        return ResponseEntity.ok(solicitudService.buscarPorId(id));
    }

    @GetMapping("/perfil/{perfilEstudianteId}")
    public ResponseEntity<List<SolicitudApoyo>> listarPorPerfil(@PathVariable Long perfilEstudianteId)
    {
        return ResponseEntity.ok(solicitudService.listarPorPerfil(perfilEstudianteId));
    }

    @PostMapping
    public ResponseEntity<SolicitudApoyo> crear(@Valid @RequestBody SolicitudRequestDTO dto)
    {
        return ResponseEntity.ok(solicitudService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudApoyo> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudRequestDTO dto)
    {

        return ResponseEntity.ok(solicitudService.actualizar(id, dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudApoyo> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequestDTO dto)
    {

        return ResponseEntity.ok(solicitudService.cambiarEstado(id, dto));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoSolicitud>> listarHistorial(@PathVariable Long id)
    {
        return ResponseEntity.ok(solicitudService.listarHistorial(id));
    }

    @GetMapping("/tipos")
    public ResponseEntity<List<TipoSolicitud>> listarTiposSolicitud()
    {
        return ResponseEntity.ok(solicitudService.listarTiposSolicitud());
    }

    @GetMapping("/estados")
    public ResponseEntity<List<EstadoSolicitud>> listarEstadosSolicitud()
    {
        return ResponseEntity.ok(solicitudService.listarEstadosSolicitud());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}