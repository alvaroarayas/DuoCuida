package com.duocuida.notificaciones.service;

import com.duocuida.notificaciones.dto.NotificacionRequestDTO;
import com.duocuida.notificaciones.exception.RecursoNoEncontradoException;
import com.duocuida.notificaciones.model.Notificacion;
import com.duocuida.notificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService
{

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository)
    {
        this.notificacionRepository = notificacionRepository;
    }

    public List<Notificacion> listar()
    {
        logger.info("Listando notificaciones");
        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Long id)
    {
        logger.info("Buscando notificación con ID {}", id);

        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe notificación con ID: " + id));
    }

    public List<Notificacion> listarPorUsuario(Long usuarioId)
    {
        logger.info("Listando notificaciones del usuario ID {}", usuarioId);
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    public List<Notificacion> listarNoLeidasPorUsuario(Long usuarioId)
    {
        logger.info("Listando notificaciones no leídas del usuario ID {}", usuarioId);
        return notificacionRepository.findByUsuarioIdAndLeida(usuarioId, false);
    }

    public Notificacion crear(NotificacionRequestDTO dto)
    {
        logger.info("Creando notificación para usuario ID {}", dto.getUsuarioId());

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuarioId(dto.getUsuarioId());
        notificacion.setSolicitudId(dto.getSolicitudId());
        notificacion.setTitulo(dto.getTitulo());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setLeida(false);
        notificacion.setFechaCreacion(LocalDateTime.now());

        return notificacionRepository.save(notificacion);
    }

    public Notificacion marcarComoLeida(Long id)
    {
        logger.info("Marcando notificación ID {} como leída", id);

        Notificacion notificacion = buscarPorId(id);
        notificacion.setLeida(true);

        return notificacionRepository.save(notificacion);
    }

    public void eliminar(Long id)
    {
        logger.info("Eliminando notificación con ID {}", id);

        Notificacion notificacion = buscarPorId(id);
        notificacionRepository.delete(notificacion);
    }
}