package com.duocuida.atenciones.service;

import com.duocuida.atenciones.client.SolicitudClient;
import com.duocuida.atenciones.dto.AtencionRequestDTO;
import com.duocuida.atenciones.dto.AtencionResponseDTO;
import com.duocuida.atenciones.exception.ResourceNotFoundException;
import com.duocuida.atenciones.model.Atencion;
import com.duocuida.atenciones.repository.AtencionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtencionServiceTest {

    @Mock
    private AtencionRepository repository;

    @Mock
    private SolicitudClient solicitudClient;

    @InjectMocks
    private AtencionService service;

    private Atencion crearAtencionEjemplo(Long id) {
        Atencion a = new Atencion();
        a.setId(id);
        a.setSolicitudId(1L);
        a.setGestorId(1L);
        a.setTipo("Presencial");
        a.setDescripcion("Primera sesion de apoyo");
        a.setEstado("AGENDADA");
        a.setFecha(LocalDate.of(2025, 6, 10));
        a.setHora(LocalTime.of(10, 0));
        return a;
    }

    private AtencionRequestDTO crearRequestEjemplo() {
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setTipo("Presencial");
        dto.setDescripcion("Primera sesion de apoyo");
        dto.setEstado("AGENDADA");
        dto.setFecha(LocalDate.of(2025, 6, 10));
        dto.setHora(LocalTime.of(10, 0));
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeAtenciones() {
        when(repository.findAll()).thenReturn(List.of(crearAtencionEjemplo(1L)));

        List<AtencionResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getTipo()).isEqualTo("Presencial");
    }

    @Test
    void crear_cuandoSolicitudExiste_deberiaGuardarAtencionYRetornarRespuesta() {
        AtencionRequestDTO request = crearRequestEjemplo();
        Atencion guardada = crearAtencionEjemplo(10L);

        when(solicitudClient.existeSolicitud(1L)).thenReturn(true);
        when(repository.save(any(Atencion.class))).thenReturn(guardada);

        AtencionResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getEstado()).isEqualTo("AGENDADA");
    }

    @Test
    void crear_cuandoSolicitudNoExiste_deberiaLanzarExcepcion() {
        AtencionRequestDTO request = crearRequestEjemplo();
        request.setSolicitudId(99L);

        when(solicitudClient.existeSolicitud(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.crear(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Solicitud no encontrada con id: 99");

        verify(repository, never()).save(any());
    }
}
