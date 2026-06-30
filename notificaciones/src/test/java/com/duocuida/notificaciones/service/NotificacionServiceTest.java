package com.duocuida.notificaciones.service;

import com.duocuida.notificaciones.dto.NotificacionRequestDTO;
import com.duocuida.notificaciones.exception.RecursoNoEncontradoException;
import com.duocuida.notificaciones.model.Notificacion;
import com.duocuida.notificaciones.repository.NotificacionRepository;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService service;

    private Notificacion crearNotificacionEjemplo(Long id) {
        Notificacion n = new Notificacion();
        n.setId(id);
        n.setUsuarioId(1L);
        n.setSolicitudId(1L);
        n.setTitulo("Solicitud registrada");
        n.setMensaje("Su solicitud fue registrada correctamente");
        n.setLeida(false);
        n.setFechaCreacion(LocalDateTime.now());
        return n;
    }

    private NotificacionRequestDTO crearRequestEjemplo() {
        NotificacionRequestDTO dto = new NotificacionRequestDTO();
        dto.setUsuarioId(1L);
        dto.setSolicitudId(1L);
        dto.setTitulo("Solicitud registrada");
        dto.setMensaje("Su solicitud fue registrada correctamente");
        return dto;
    }

    @Test
    void listar_deberiaRetornarListaDeNotificaciones() {
        when(notificacionRepository.findAll()).thenReturn(List.of(crearNotificacionEjemplo(1L)));

        List<Notificacion> respuesta = service.listar();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getTitulo()).isEqualTo("Solicitud registrada");
    }

    @Test
    void crear_deberiaGuardarNotificacionYRetornarla() {
        NotificacionRequestDTO request = crearRequestEjemplo();

        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(crearNotificacionEjemplo(10L));

        Notificacion resultado = service.crear(request);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getLeida()).isFalse();
        assertThat(resultado.getTitulo()).isEqualTo("Solicitud registrada");
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe notificación con ID: 99");
    }
}