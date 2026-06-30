package com.duocuida.planes.service;

import com.duocuida.planes.client.EvaluacionClient;
import com.duocuida.planes.dto.PlanRequestDTO;
import com.duocuida.planes.dto.PlanResponseDTO;
import com.duocuida.planes.exception.ResourceNotFoundException;
import com.duocuida.planes.model.Plan;
import com.duocuida.planes.repository.PlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository repository;

    @Mock
    private EvaluacionClient evaluacionClient;

    @InjectMocks
    private PlanService service;

    private Plan crearPlanEjemplo(Long id) {
        Plan plan = new Plan();
        plan.setId(id);
        plan.setEvaluacionId(1L);
        plan.setGestorId(1L);
        plan.setDescripcion("Plan de apoyo academico");
        plan.setObjetivo("Mejorar rendimiento del estudiante");
        plan.setEstado("ACTIVO");
        plan.setFechaInicio(LocalDate.of(2025, 6, 1));
        return plan;
    }

    private PlanRequestDTO crearRequestEjemplo() {
        PlanRequestDTO dto = new PlanRequestDTO();
        dto.setEvaluacionId(1L);
        dto.setGestorId(1L);
        dto.setDescripcion("Plan de apoyo academico");
        dto.setObjetivo("Mejorar rendimiento del estudiante");
        dto.setEstado("ACTIVO");
        dto.setFechaInicio(LocalDate.of(2025, 6, 1));
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDePlanes() {
        when(repository.findAll()).thenReturn(List.of(crearPlanEjemplo(1L)));

        List<PlanResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getId()).isEqualTo(1L);
        assertThat(respuesta.get(0).getDescripcion()).isEqualTo("Plan de apoyo academico");
        verify(repository).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarPlan() {
        when(repository.findById(1L)).thenReturn(Optional.of(crearPlanEjemplo(1L)));

        PlanResponseDTO respuesta = service.obtenerPorId(1L);

        assertThat(respuesta.getId()).isEqualTo(1L);
        assertThat(respuesta.getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Plan no encontrado con id: 99");
    }

    @Test
    void obtenerPorEvaluacion_deberiaRetornarPlanesDeLaEvaluacion() {
        when(repository.findByEvaluacionId(1L)).thenReturn(List.of(crearPlanEjemplo(1L)));

        List<PlanResponseDTO> respuesta = service.obtenerPorEvaluacion(1L);

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getEvaluacionId()).isEqualTo(1L);
    }

    @Test
    void obtenerPorEstado_deberiaRetornarPlanesConEseEstado() {
        when(repository.findByEstado("ACTIVO")).thenReturn(List.of(crearPlanEjemplo(1L)));

        List<PlanResponseDTO> respuesta = service.obtenerPorEstado("ACTIVO");

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    void crear_cuandoEvaluacionExiste_deberiaGuardarPlanYRetornarRespuesta() {
        PlanRequestDTO request = crearRequestEjemplo();
        Plan planGuardado = crearPlanEjemplo(10L);

        when(evaluacionClient.existeEvaluacion(1L)).thenReturn(true);
        when(repository.save(any(Plan.class))).thenReturn(planGuardado);

        PlanResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getDescripcion()).isEqualTo("Plan de apoyo academico");

        ArgumentCaptor<Plan> captor = ArgumentCaptor.forClass(Plan.class);
        verify(repository).save(captor.capture());

        Plan planEnviadoAGuardar = captor.getValue();
        assertThat(planEnviadoAGuardar.getEvaluacionId()).isEqualTo(1L);
        assertThat(planEnviadoAGuardar.getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    void crear_cuandoEvaluacionNoExiste_deberiaLanzarExcepcion() {
        PlanRequestDTO request = crearRequestEjemplo();
        request.setEvaluacionId(99L);

        when(evaluacionClient.existeEvaluacion(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.crear(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Evaluacion no encontrada con id: 99");

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_cuandoExiste_deberiaActualizarYRetornarPlan() {
        PlanRequestDTO request = crearRequestEjemplo();
        request.setDescripcion("Descripcion actualizada");
        Plan existente = crearPlanEjemplo(5L);

        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Plan.class))).thenReturn(existente);

        PlanResponseDTO respuesta = service.actualizar(5L, request);

        assertThat(respuesta.getId()).isEqualTo(5L);
        verify(repository).save(any(Plan.class));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaLanzarExcepcion() {
        PlanRequestDTO request = crearRequestEjemplo();

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.actualizar(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Plan no encontrado con id: 99");

        verify(repository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_deberiaEliminarPlan() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Plan no encontrado con id: 99");

        verify(repository, never()).deleteById(any());
    }
}
