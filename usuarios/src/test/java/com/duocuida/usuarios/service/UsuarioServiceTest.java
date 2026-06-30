package com.duocuida.usuarios.service;

import com.duocuida.usuarios.dto.UsuarioRequestDTO;
import com.duocuida.usuarios.dto.UsuarioResponseDTO;
import com.duocuida.usuarios.exception.ResourceNotFoundException;
import com.duocuida.usuarios.model.Usuario;
import com.duocuida.usuarios.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    private Usuario crearUsuarioEjemplo(Long id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre("Ana");
        u.setApellido("Gonzalez");
        u.setEmail("ana.gonzalez@duoc.cl");
        u.setPassword("123456");
        u.setRol("ESTUDIANTE");
        u.setActivo(true);
        return u;
    }

    private UsuarioRequestDTO crearRequestEjemplo() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNombre("Ana");
        dto.setApellido("Gonzalez");
        dto.setEmail("ana.gonzalez@duoc.cl");
        dto.setPassword("123456");
        dto.setRol("ESTUDIANTE");
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeUsuarios() {
        when(repository.findAll()).thenReturn(List.of(crearUsuarioEjemplo(1L)));

        List<UsuarioResponseDTO> respuesta = service.obtenerTodos();

        assertThat(respuesta).hasSize(1);
        assertThat(respuesta.get(0).getEmail()).isEqualTo("ana.gonzalez@duoc.cl");
    }

    @Test
    void crear_deberiaGuardarUsuarioYRetornarRespuesta() {
        UsuarioRequestDTO request = crearRequestEjemplo();
        Usuario guardado = crearUsuarioEjemplo(10L);

        when(repository.save(any(Usuario.class))).thenReturn(guardado);

        UsuarioResponseDTO respuesta = service.crear(request);

        assertThat(respuesta.getId()).isEqualTo(10L);
        assertThat(respuesta.getRol()).isEqualTo("ESTUDIANTE");
        assertThat(respuesta.getNombre()).isEqualTo("Ana");
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado con id: 99");
    }
}
