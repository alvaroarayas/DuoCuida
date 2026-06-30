package com.duocuida.evaluaciones.service;

import com.duocuida.evaluaciones.dto.EvaluacionRequestDTO;
import com.duocuida.evaluaciones.dto.EvaluacionResponseDTO;
import com.duocuida.evaluaciones.exception.ResourceNotFoundException;
import com.duocuida.evaluaciones.model.Evaluacion;
import com.duocuida.evaluaciones.repository.EvaluacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EvaluacionServiceTest {

    @Mock
    private EvaluacionRepository repository;

    @InjectMocks
    private EvaluacionService service;

    // método ayudante para no repetir la creación de datos
    private Evaluacion crearEvaluacionEjemplo(Long id) {
        Evaluacion e = new Evaluacion();
        e.setId(id);
        e.setSolicitudId(1L);
        e.setGestorId(1L);
        e.setResultado("APROBADO");
        e.setObservacion("Sin observaciones");
        e.setPuntaje(80);
        e.setFecha(LocalDate.of(2025, 6, 1));
        return e;
    }

    private EvaluacionRequestDTO crearRequestEjemplo() {
        EvaluacionRequestDTO dto = new EvaluacionRequestDTO();
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setResultado("APROBADO");
        dto.setObservacion("Sin observaciones");
        dto.setPuntaje(80);
        dto.setFecha(LocalDate.of(2025, 6, 1));
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeEvaluaciones() {
        when(repository.findAll()).thenReturn(List.of(crearEvaluacionEjemplo(1L)));

        List<EvaluacionResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getResultado()).isEqualTo("APROBADO");
    }

    @Test
    void crear_deberiaGuardarEvaluacionYRetornarRespuesta() {
        EvaluacionRequestDTO request = crearRequestEjemplo();
        Evaluacion guardada = crearEvaluacionEjemplo(10L);

        when(repository.save(any(Evaluacion.class))).thenReturn(guardada);

        EvaluacionResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getResultado()).isEqualTo("APROBADO");
        assertThat(respuesta.getPuntaje()).isEqualTo(80);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Evaluacion no encontrada con id: 99");
    }
}
