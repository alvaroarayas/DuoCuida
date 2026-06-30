package com.duocuida.derivaciones.service;

import com.duocuida.derivaciones.dto.DerivacionRequestDTO;
import com.duocuida.derivaciones.dto.DerivacionResponseDTO;
import com.duocuida.derivaciones.exception.ResourceNotFoundException;
import com.duocuida.derivaciones.model.Derivacion;
import com.duocuida.derivaciones.repository.DerivacionRepository;
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
class DerivacionServiceTest {

    @Mock
    private DerivacionRepository repository;

    @InjectMocks
    private DerivacionService service;

    private Derivacion crearDerivacionEjemplo(Long id) {
        Derivacion d = new Derivacion();
        d.setId(id);
        d.setSolicitudId(1L);
        d.setGestorId(1L);
        d.setUnidadDestino("Bienestar Estudiantil");
        d.setMotivo("Requiere apoyo psicologico");
        d.setEstado("PENDIENTE");
        d.setFecha(LocalDate.of(2025, 6, 1));
        return d;
    }

    private DerivacionRequestDTO crearRequestEjemplo() {
        DerivacionRequestDTO dto = new DerivacionRequestDTO();
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setUnidadDestino("Bienestar Estudiantil");
        dto.setMotivo("Requiere apoyo psicologico");
        dto.setEstado("PENDIENTE");
        dto.setFecha(LocalDate.of(2025, 6, 1));
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeDerivaciones() {
        when(repository.findAll()).thenReturn(List.of(crearDerivacionEjemplo(1L)));

        List<DerivacionResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getUnidadDestino()).isEqualTo("Bienestar Estudiantil");
    }

    @Test
    void crear_deberiaGuardarDerivacionYRetornarRespuesta() {
        DerivacionRequestDTO request = crearRequestEjemplo();
        Derivacion guardada = crearDerivacionEjemplo(10L);

        when(repository.save(any(Derivacion.class))).thenReturn(guardada);

        DerivacionResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getEstado()).isEqualTo("PENDIENTE");
        assertThat(respuesta.getMotivo()).isEqualTo("Requiere apoyo psicologico");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Derivacion no encontrada con id: 99");
    }
}
