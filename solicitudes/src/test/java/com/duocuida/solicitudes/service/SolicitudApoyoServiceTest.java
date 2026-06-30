package com.duocuida.solicitudes.service;

import com.duocuida.solicitudes.client.NotificacionClient;
import com.duocuida.solicitudes.client.PerfilEstudianteClient;
import com.duocuida.solicitudes.dto.PerfilEstudianteDTO;
import com.duocuida.solicitudes.dto.SolicitudRequestDTO;
import com.duocuida.solicitudes.exception.RecursoNoEncontradoException;
import com.duocuida.solicitudes.exception.ReglaNegocioException;
import com.duocuida.solicitudes.model.EstadoSolicitud;
import com.duocuida.solicitudes.model.SolicitudApoyo;
import com.duocuida.solicitudes.model.TipoSolicitud;
import com.duocuida.solicitudes.repository.EstadoSolicitudRepository;
import com.duocuida.solicitudes.repository.HistorialEstadoSolicitudRepository;
import com.duocuida.solicitudes.repository.SolicitudApoyoRepository;
import com.duocuida.solicitudes.repository.TipoSolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitudApoyoServiceTest {

    @Mock private SolicitudApoyoRepository solicitudRepository;
    @Mock private HistorialEstadoSolicitudRepository historialRepository;
    @Mock private TipoSolicitudRepository tipoSolicitudRepository;
    @Mock private EstadoSolicitudRepository estadoSolicitudRepository;
    @Mock private PerfilEstudianteClient perfilClient;
    @Mock private NotificacionClient notificacionClient;

    @InjectMocks
    private SolicitudApoyoService service;

    private PerfilEstudianteDTO crearPerfilActivo() {
        PerfilEstudianteDTO p = new PerfilEstudianteDTO();
        p.setId(1L);
        p.setUsuarioId(1L);
        p.setActivo(true);
        return p;
    }

    private SolicitudRequestDTO crearRequestEjemplo() {
        SolicitudRequestDTO dto = new SolicitudRequestDTO();
        dto.setPerfilEstudianteId(1L);
        dto.setTipoSolicitudId(1L);
        dto.setDescripcion("Solicito apoyo academico");
        dto.setAntecedentes("Tengo dificultades en varias asignaturas");
        return dto;
    }

    private SolicitudApoyo crearSolicitudEjemplo(Long id) {
        SolicitudApoyo s = new SolicitudApoyo();
        s.setId(id);
        s.setPerfilEstudianteId(1L);
        s.setTipoSolicitud(new TipoSolicitud(1L, "APOYO_ACADEMICO", true));
        s.setDescripcion("Solicito apoyo academico");
        s.setAntecedentes("Tengo dificultades en varias asignaturas");
        s.setEstado(new EstadoSolicitud(1L, "REGISTRADA", true));
        s.setFechaCreacion(LocalDateTime.now());
        return s;
    }

    @Test
    void listar_deberiaRetornarListaDeSolicitudes() {
        when(solicitudRepository.findAll()).thenReturn(List.of(crearSolicitudEjemplo(1L)));

        List<SolicitudApoyo> respuesta = service.listar();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontrado() {
        when(solicitudRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe solicitud con ID: 99");
    }

    @Test
    void crear_cuandoPerfilNoExiste_deberiaLanzarReglaNegocio() {
        SolicitudRequestDTO request = crearRequestEjemplo();

        when(perfilClient.obtenerPerfilPorId(1L)).thenReturn(null);

        assertThatThrownBy(() -> service.crear(request))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("el perfil estudiante no existe");
    }

    @Test
    void crear_cuandoDatosValidos_deberiaGuardarSolicitud() {
        SolicitudRequestDTO request = crearRequestEjemplo();

        when(perfilClient.obtenerPerfilPorId(1L)).thenReturn(crearPerfilActivo());
        when(tipoSolicitudRepository.findById(1L))
                .thenReturn(Optional.of(new TipoSolicitud(1L, "APOYO_ACADEMICO", true)));
        when(estadoSolicitudRepository.findByDescripcion("REGISTRADA"))
                .thenReturn(Optional.of(new EstadoSolicitud(1L, "REGISTRADA", true)));
        when(solicitudRepository.save(any(SolicitudApoyo.class))).thenReturn(crearSolicitudEjemplo(3L));

        SolicitudApoyo resultado = service.crear(request);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getDescripcion()).isEqualTo("Solicito apoyo academico");
    }
}