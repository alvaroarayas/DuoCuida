package com.duocuida.derivaciones.controller;

import com.duocuida.derivaciones.dto.DerivacionResponseDTO;
import com.duocuida.derivaciones.exception.ResourceNotFoundException;
import com.duocuida.derivaciones.service.DerivacionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(DerivacionController.class)
class DerivacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DerivacionService service;

    private DerivacionResponseDTO crearResponseEjemplo() {
        DerivacionResponseDTO dto = new DerivacionResponseDTO();
        dto.setId(1L);
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setUnidadDestino("Bienestar Estudiantil");
        dto.setMotivo("Requiere apoyo psicologico");
        dto.setEstado("PENDIENTE");
        dto.setFecha(LocalDate.of(2025, 6, 1));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeDerivaciones() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/derivaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].unidadDestino").value("Bienestar Estudiantil"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/derivaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": 1,
                                  "gestorId": 1,
                                  "unidadDestino": "Bienestar Estudiantil",
                                  "motivo": "Requiere apoyo psicologico",
                                  "estado": "PENDIENTE",
                                  "fecha": "2025-06-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/derivaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": null,
                                  "gestorId": null,
                                  "unidadDestino": "",
                                  "motivo": "",
                                  "estado": "INVALIDO",
                                  "fecha": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Derivacion no encontrada con id: 99"));

        mockMvc.perform(get("/api/derivaciones/99"))
                .andExpect(status().isNotFound());
    }
}