package com.duocuida.usuarios.controller;

import com.duocuida.usuarios.dto.UsuarioResponseDTO;
import com.duocuida.usuarios.exception.ResourceNotFoundException;
import com.duocuida.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    private UsuarioResponseDTO crearResponseEjemplo() {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(1L);
        dto.setNombre("Ana");
        dto.setApellido("Gonzalez");
        dto.setEmail("ana.gonzalez@duoc.cl");
        dto.setRol("ESTUDIANTE");
        dto.setActivo(true);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeUsuarios() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("ana.gonzalez@duoc.cl"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Ana",
                                  "apellido": "Gonzalez",
                                  "email": "ana.gonzalez@duoc.cl",
                                  "password": "123456",
                                  "rol": "ESTUDIANTE"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rol").value("ESTUDIANTE"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "",
                                  "apellido": "",
                                  "email": "no-es-un-email",
                                  "password": "",
                                  "rol": "INVALIDO"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Usuario no encontrado con id: 99"));

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
