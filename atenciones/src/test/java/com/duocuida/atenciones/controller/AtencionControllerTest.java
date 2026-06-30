package com.duocuida.atenciones.controller;

import com.duocuida.atenciones.dto.AtencionResponseDTO;
import com.duocuida.atenciones.exception.ResourceNotFoundException;
import com.duocuida.atenciones.service.AtencionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AtencionController.class)
class AtencionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AtencionService service;

    private AtencionResponseDTO crearResponseEjemplo() {
        AtencionResponseDTO dto = new AtencionResponseDTO();
        dto.setId(1L);
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setTipo("Presencial");
        dto.setDescripcion("Primera sesion de apoyo");
        dto.setEstado("AGENDADA");
        dto.setFecha(LocalDate.of(2025, 6, 10));
        dto.setHora(LocalTime.of(10, 0));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeAtenciones() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/atenciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipo").value("Presencial"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/atenciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": 1,
                                  "gestorId": 1,
                                  "tipo": "Presencial",
                                  "descripcion": "Primera sesion de apoyo",
                                  "estado": "AGENDADA",
                                  "fecha": "2025-06-10",
                                  "hora": "10:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("AGENDADA"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/atenciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": null,
                                  "gestorId": null,
                                  "tipo": "",
                                  "descripcion": "",
                                  "estado": "INVALIDO",
                                  "fecha": null,
                                  "hora": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Atencion no encontrada con id: 99"));

        mockMvc.perform(get("/api/atenciones/99"))
                .andExpect(status().isNotFound());
    }
}
