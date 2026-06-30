package com.duocuida.auth.service;

import com.duocuida.auth.client.UsuarioClient;
import com.duocuida.auth.dto.LoginRequestDTO;
import com.duocuida.auth.dto.LoginResponseDTO;
import com.duocuida.auth.dto.RegistroRequestDTO;
import com.duocuida.auth.dto.UsuarioDTO;
import com.duocuida.auth.exception.RecursoNoEncontradoException;
import com.duocuida.auth.exception.ReglaNegocioException;
import com.duocuida.auth.model.Credencial;
import com.duocuida.auth.repository.CredencialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CredencialRepository credencialRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AuthService service;

    private Credencial crearCredencialEjemplo() {
        Credencial c = new Credencial();
        c.setId(1L);
        c.setUsuarioId(1L);
        c.setCorreo("ana.gonzalez@duoc.cl");
        c.setPassword("123456");
        c.setActivo(true);
        c.setFechaCreacion(LocalDateTime.now());
        return c;
    }

    private UsuarioDTO crearUsuarioActivo() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setEmail("ana.gonzalez@duoc.cl");
        u.setRol("ESTUDIANTE");
        u.setActivo(true);
        return u;
    }

    @Test
    void login_cuandoCredencialesCorrectas_deberiaRetornarLoginResponse() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setCorreo("ana.gonzalez@duoc.cl");
        dto.setPassword("123456");

        when(credencialRepository.findByCorreo("ana.gonzalez@duoc.cl"))
                .thenReturn(Optional.of(crearCredencialEjemplo()));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(crearUsuarioActivo());

        LoginResponseDTO respuesta = service.login(dto);

        assertThat(respuesta.getMensaje()).isEqualTo("Login correcto");
        assertThat(respuesta.getUsuarioId()).isEqualTo(1L);
        assertThat(respuesta.getRol()).isEqualTo("ESTUDIANTE");
    }

    @Test
    void login_cuandoCorreoNoExiste_deberiaLanzarRecursoNoEncontrado() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setCorreo("noexiste@duoc.cl");
        dto.setPassword("123456");

        when(credencialRepository.findByCorreo("noexiste@duoc.cl")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.login(dto))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("No existe credencial para ese correo");
    }

    @Test
    void login_cuandoPasswordIncorrecto_deberiaLanzarReglaNegocio() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setCorreo("ana.gonzalez@duoc.cl");
        dto.setPassword("clave-incorrecta");

        when(credencialRepository.findByCorreo("ana.gonzalez@duoc.cl"))
                .thenReturn(Optional.of(crearCredencialEjemplo()));

        assertThatThrownBy(() -> service.login(dto))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Correo o contraseña incorrectos");
    }

    @Test
    void registrar_cuandoDatosValidos_deberiaGuardarCredencial() {
        RegistroRequestDTO dto = new RegistroRequestDTO();
        dto.setUsuarioId(1L);
        dto.setCorreo("ana.gonzalez@duoc.cl");
        dto.setPassword("123456");

        when(credencialRepository.existsByCorreo("ana.gonzalez@duoc.cl")).thenReturn(false);
        when(credencialRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(crearUsuarioActivo());
        when(credencialRepository.save(any(Credencial.class))).thenReturn(crearCredencialEjemplo());

        Credencial resultado = service.registrar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getCorreo()).isEqualTo("ana.gonzalez@duoc.cl");
        assertThat(resultado.getActivo()).isTrue();
    }
}
