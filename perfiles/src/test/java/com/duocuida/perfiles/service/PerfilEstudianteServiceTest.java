package com.duocuida.perfiles.service;

import com.duocuida.perfiles.dto.PerfilRequestDTO;
import com.duocuida.perfiles.dto.PerfilResponseDTO;
import com.duocuida.perfiles.exception.ResourceNotFoundException;
import com.duocuida.perfiles.model.PerfilEstudiante;
import com.duocuida.perfiles.repository.PerfilEstudianteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilEstudianteServiceTest {

    @Mock
    private PerfilEstudianteRepository repository;

    @InjectMocks
    private PerfilEstudianteService service;

    private PerfilEstudiante crearPerfilEjemplo(Long id) {
        PerfilEstudiante p = new PerfilEstudiante();
        p.setId(id);
        p.setUsuarioId(1L);
        p.setNombre("Ana");
        p.setApellido("Gonzalez");
        p.setEmail("ana.gonzalez@duoc.cl");
        p.setTelefono("912345678");
        p.setCarrera("Ingenieria Informatica");
        p.setSede("Valparaiso");
        p.setActivo(true);
        return p;
    }

    private PerfilRequestDTO crearRequestEjemplo() {
        PerfilRequestDTO dto = new PerfilRequestDTO();
        dto.setUsuarioId(1L);
        dto.setNombre("Ana");
        dto.setApellido("Gonzalez");
        dto.setEmail("ana.gonzalez@duoc.cl");
        dto.setTelefono("912345678");
        dto.setCarrera("Ingenieria Informatica");
        dto.setSede("Valparaiso");
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDePerfiles() {
        when(repository.findAll()).thenReturn(List.of(crearPerfilEjemplo(1L)));

        List<PerfilResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getEmail()).isEqualTo("ana.gonzalez@duoc.cl");
    }

    @Test
    void crear_deberiaGuardarPerfilYRetornarRespuesta() {
        PerfilRequestDTO request = crearRequestEjemplo();
        PerfilEstudiante guardado = crearPerfilEjemplo(10L);

        when(repository.save(any(PerfilEstudiante.class))).thenReturn(guardado);

        PerfilResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getCarrera()).isEqualTo("Ingenieria Informatica");
        assertThat(respuesta.getNombre()).isEqualTo("Ana");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Perfil no encontrado con id: 99");
    }
}