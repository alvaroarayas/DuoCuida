package com.duocuida.solicitudes.service;

import com.duocuida.solicitudes.client.NotificacionClient;
import com.duocuida.solicitudes.client.PerfilEstudianteClient;
import com.duocuida.solicitudes.dto.CambioEstadoRequestDTO;
import com.duocuida.solicitudes.dto.NotificacionRequestDTO;
import com.duocuida.solicitudes.dto.PerfilEstudianteDTO;
import com.duocuida.solicitudes.dto.SolicitudRequestDTO;
import com.duocuida.solicitudes.exception.RecursoNoEncontradoException;
import com.duocuida.solicitudes.exception.ReglaNegocioException;
import com.duocuida.solicitudes.model.EstadoSolicitud;
import com.duocuida.solicitudes.model.HistorialEstadoSolicitud;
import com.duocuida.solicitudes.model.SolicitudApoyo;
import com.duocuida.solicitudes.model.TipoSolicitud;
import com.duocuida.solicitudes.repository.EstadoSolicitudRepository;
import com.duocuida.solicitudes.repository.HistorialEstadoSolicitudRepository;
import com.duocuida.solicitudes.repository.SolicitudApoyoRepository;
import com.duocuida.solicitudes.repository.TipoSolicitudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudApoyoService {

    private static final Logger logger = LoggerFactory.getLogger(SolicitudApoyoService.class);

    private final SolicitudApoyoRepository solicitudRepository;
    private final HistorialEstadoSolicitudRepository historialRepository;
    private final TipoSolicitudRepository tipoSolicitudRepository;
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final PerfilEstudianteClient perfilClient;
    private final NotificacionClient notificacionClient;

    public SolicitudApoyoService(
            SolicitudApoyoRepository solicitudRepository,
            HistorialEstadoSolicitudRepository historialRepository,
            TipoSolicitudRepository tipoSolicitudRepository,
            EstadoSolicitudRepository estadoSolicitudRepository,
            PerfilEstudianteClient perfilClient,
            NotificacionClient notificacionClient) {

        this.solicitudRepository = solicitudRepository;
        this.historialRepository = historialRepository;
        this.tipoSolicitudRepository = tipoSolicitudRepository;
        this.estadoSolicitudRepository = estadoSolicitudRepository;
        this.perfilClient = perfilClient;
        this.notificacionClient = notificacionClient;
    }

    public List<SolicitudApoyo> listar() {
        logger.info("Listando solicitudes de apoyo");
        return solicitudRepository.findAll();
    }

    public SolicitudApoyo buscarPorId(Long id) {
        logger.info("Buscando solicitud con ID {}", id);

        return solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe solicitud con ID: " + id));
    }

    public List<SolicitudApoyo> listarPorPerfil(Long perfilEstudianteId) {
        logger.info("Listando solicitudes del perfil estudiante ID {}", perfilEstudianteId);
        return solicitudRepository.findByPerfilEstudianteId(perfilEstudianteId);
    }

    public SolicitudApoyo crear(SolicitudRequestDTO dto) {
        logger.info("Intentando crear solicitud para perfil ID {}", dto.getPerfilEstudianteId());

        PerfilEstudianteDTO perfil = perfilClient.obtenerPerfilPorId(dto.getPerfilEstudianteId());

        if (perfil == null || perfil.getId() == null) {
            throw new ReglaNegocioException("No se puede crear la solicitud porque el perfil estudiante no existe");
        }

        TipoSolicitud tipoSolicitud = tipoSolicitudRepository.findById(dto.getTipoSolicitudId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe tipo de solicitud con ID: " + dto.getTipoSolicitudId()
                ));

        if (!tipoSolicitud.getActivo()) {
            throw new ReglaNegocioException("No se puede usar un tipo de solicitud inactivo");
        }

        EstadoSolicitud estadoRegistrada = estadoSolicitudRepository.findByDescripcion("REGISTRADA")
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estado REGISTRADA"));

        SolicitudApoyo solicitud = new SolicitudApoyo();
        solicitud.setPerfilEstudianteId(dto.getPerfilEstudianteId());
        solicitud.setTipoSolicitud(tipoSolicitud);
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setAntecedentes(dto.getAntecedentes());
        solicitud.setEstado(estadoRegistrada);
        solicitud.setFechaCreacion(LocalDateTime.now());

        SolicitudApoyo solicitudGuardada = solicitudRepository.save(solicitud);

        registrarHistorial(
                solicitudGuardada,
                null,
                estadoRegistrada,
                "Solicitud registrada inicialmente"
        );

        crearNotificacionSolicitudRegistrada(perfil, solicitudGuardada);

        logger.info("Solicitud creada correctamente con ID {}", solicitudGuardada.getId());

        return solicitudGuardada;
    }

    public SolicitudApoyo actualizar(Long id, SolicitudRequestDTO dto) {
        logger.info("Actualizando solicitud con ID {}", id);

        SolicitudApoyo solicitud = buscarPorId(id);

        if (esEstadoCerrada(solicitud.getEstado())) {
            throw new ReglaNegocioException("No se puede modificar una solicitud cerrada");
        }

        PerfilEstudianteDTO perfil = perfilClient.obtenerPerfilPorId(dto.getPerfilEstudianteId());

        if (perfil == null || perfil.getId() == null) {
            throw new ReglaNegocioException("No se puede actualizar la solicitud porque el perfil estudiante no existe");
        }

        TipoSolicitud tipoSolicitud = tipoSolicitudRepository.findById(dto.getTipoSolicitudId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe tipo de solicitud con ID: " + dto.getTipoSolicitudId()
                ));

        if (!tipoSolicitud.getActivo()) {
            throw new ReglaNegocioException("No se puede usar un tipo de solicitud inactivo");
        }

        solicitud.setPerfilEstudianteId(dto.getPerfilEstudianteId());
        solicitud.setTipoSolicitud(tipoSolicitud);
        solicitud.setDescripcion(dto.getDescripcion());
        solicitud.setAntecedentes(dto.getAntecedentes());

        return solicitudRepository.save(solicitud);
    }

    public SolicitudApoyo cambiarEstado(Long id, CambioEstadoRequestDTO dto) {
        logger.info("Cambiando estado de solicitud ID {}", id);

        SolicitudApoyo solicitud = buscarPorId(id);

        if (esEstadoCerrada(solicitud.getEstado())) {
            throw new ReglaNegocioException("No se puede cambiar el estado de una solicitud cerrada");
        }

        EstadoSolicitud nuevoEstado = estadoSolicitudRepository.findById(dto.getNuevoEstadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe estado con ID: " + dto.getNuevoEstadoId()
                ));

        if (!nuevoEstado.getActivo()) {
            throw new ReglaNegocioException("No se puede cambiar a un estado inactivo");
        }

        EstadoSolicitud estadoAnterior = solicitud.getEstado();
        solicitud.setEstado(nuevoEstado);

        SolicitudApoyo solicitudActualizada = solicitudRepository.save(solicitud);

        registrarHistorial(
                solicitudActualizada,
                estadoAnterior,
                nuevoEstado,
                dto.getObservacion()
        );

        PerfilEstudianteDTO perfil = perfilClient.obtenerPerfilPorId(
                solicitudActualizada.getPerfilEstudianteId()
        );

        crearNotificacionCambioEstado(perfil, solicitudActualizada, nuevoEstado);

        logger.info("Estado de solicitud ID {} cambiado correctamente", id);

        return solicitudActualizada;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando solicitud con ID {}", id);

        SolicitudApoyo solicitud = buscarPorId(id);

        if (esEstadoCerrada(solicitud.getEstado())) {
            throw new ReglaNegocioException("No se puede eliminar una solicitud cerrada");
        }

        solicitudRepository.delete(solicitud);
    }

    public List<HistorialEstadoSolicitud> listarHistorial(Long solicitudId) {
        buscarPorId(solicitudId);
        return historialRepository.findBySolicitudId(solicitudId);
    }

    public List<TipoSolicitud> listarTiposSolicitud() {
        return tipoSolicitudRepository.findAll();
    }

    public List<EstadoSolicitud> listarEstadosSolicitud() {
        return estadoSolicitudRepository.findAll();
    }

    private void registrarHistorial(
            SolicitudApoyo solicitud,
            EstadoSolicitud estadoAnterior,
            EstadoSolicitud estadoNuevo,
            String observacion) {

        HistorialEstadoSolicitud historial = new HistorialEstadoSolicitud();
        historial.setSolicitud(solicitud);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(estadoNuevo);
        historial.setObservacion(observacion);
        historial.setFechaCambio(LocalDateTime.now());

        historialRepository.save(historial);

        logger.info(
                "Historial registrado para solicitud ID {}",
                solicitud.getId()
        );
    }

    private void crearNotificacionSolicitudRegistrada(
            PerfilEstudianteDTO perfil,
            SolicitudApoyo solicitud) {

        try {
            NotificacionRequestDTO notificacion = new NotificacionRequestDTO(
                    perfil.getUsuarioId(),
                    solicitud.getId(),
                    "Solicitud registrada",
                    "Su solicitud de apoyo fue registrada correctamente."
            );

            notificacionClient.crearNotificacion(notificacion);

            logger.info("Notificación creada para usuario ID {}", perfil.getUsuarioId());

        } catch (Exception ex) {
            logger.error(
                    "No se pudo crear la notificación de solicitud registrada: {}",
                    ex.getMessage()
            );
        }
    }

    private void crearNotificacionCambioEstado(
            PerfilEstudianteDTO perfil,
            SolicitudApoyo solicitud,
            EstadoSolicitud nuevoEstado) {

        try {
            NotificacionRequestDTO notificacion = new NotificacionRequestDTO(
                    perfil.getUsuarioId(),
                    solicitud.getId(),
                    "Cambio de estado de solicitud",
                    "Su solicitud cambió al estado: " + nuevoEstado.getDescripcion()
            );

            notificacionClient.crearNotificacion(notificacion);

            logger.info(
                    "Notificación de cambio de estado creada para usuario ID {}",
                    perfil.getUsuarioId()
            );

        } catch (Exception ex) {
            logger.error(
                    "No se pudo crear la notificación de cambio de estado: {}",
                    ex.getMessage()
            );
        }
    }

    private boolean esEstadoCerrada(EstadoSolicitud estado) {
        return estado != null && "CERRADA".equalsIgnoreCase(estado.getDescripcion());
    }
}