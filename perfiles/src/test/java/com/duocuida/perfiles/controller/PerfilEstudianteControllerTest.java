package com.duocuida.perfiles.controller;

import com.duocuida.perfiles.dto.PerfilResponseDTO;
import com.duocuida.perfiles.exception.ResourceNotFoundException;
import com.duocuida.perfiles.service.PerfilEstudianteService;
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

@WebMvcTest(PerfilEstudianteController.class)
class PerfilEstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PerfilEstudianteService service;

    private PerfilResponseDTO crearResponseEjemplo() {
        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(1L);
        dto.setUsuarioId(1L);
        dto.setNombre("Ana");
        dto.setApellido("Gonzalez");
        dto.setEmail("ana.gonzalez@duoc.cl");
        dto.setTelefono("912345678");
        dto.setCarrera("Ingenieria Informatica");
        dto.setSede("Valparaiso");
        dto.setActivo(true);
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDePerfiles() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].carrera").value("Ingenieria Informatica"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/perfiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": 1,
                                  "nombre": "Ana",
                                  "apellido": "Gonzalez",
                                  "email": "ana.gonzalez@duoc.cl",
                                  "telefono": "912345678",
                                  "carrera": "Ingenieria Informatica",
                                  "sede": "Valparaiso"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/perfiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": null,
                                  "nombre": "",
                                  "apellido": "",
                                  "email": "no-es-un-email",
                                  "carrera": "",
                                  "sede": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Perfil no encontrado con id: 99"));

        mockMvc.perform(get("/api/perfiles/99"))
                .andExpect(status().isNotFound());
    }
}
