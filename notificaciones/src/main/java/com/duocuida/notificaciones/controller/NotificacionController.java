package com.duocuida.notificaciones.controller;

import com.duocuida.notificaciones.dto.NotificacionRequestDTO;
import com.duocuida.notificaciones.model.Notificacion;
import com.duocuida.notificaciones.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController
{

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService)
    {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar()
    {
        return ResponseEntity.ok(notificacionService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Long id)
    {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> listarPorUsuario(@PathVariable Long usuarioId)
    {
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> listarNoLeidasPorUsuario(@PathVariable Long usuarioId)
    {
        return ResponseEntity.ok(notificacionService.listarNoLeidasPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionRequestDTO dto)
    {
        return ResponseEntity.ok(notificacionService.crear(dto));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id)
    {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id)
    {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}