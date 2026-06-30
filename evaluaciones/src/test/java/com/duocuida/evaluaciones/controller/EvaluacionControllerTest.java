package com.duocuida.evaluaciones.controller;

import com.duocuida.evaluaciones.dto.EvaluacionResponseDTO;
import com.duocuida.evaluaciones.exception.ResourceNotFoundException;
import com.duocuida.evaluaciones.service.EvaluacionService;
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

@WebMvcTest(EvaluacionController.class)
class EvaluacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EvaluacionService service;

    private EvaluacionResponseDTO crearResponseEjemplo() {
        EvaluacionResponseDTO dto = new EvaluacionResponseDTO();
        dto.setId(1L);
        dto.setSolicitudId(1L);
        dto.setGestorId(1L);
        dto.setResultado("APROBADO");
        dto.setObservacion("Sin observaciones");
        dto.setPuntaje(80);
        dto.setFecha(LocalDate.of(2025, 6, 1));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeEvaluaciones() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/evaluaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].resultado").value("APROBADO"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": 1,
                                  "gestorId": 1,
                                  "resultado": "APROBADO",
                                  "observacion": "Sin observaciones",
                                  "puntaje": 80,
                                  "fecha": "2025-06-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.resultado").value("APROBADO"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/evaluaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "solicitudId": null,
                                  "gestorId": null,
                                  "resultado": "INVALIDO",
                                  "puntaje": 200,
                                  "fecha": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Evaluacion no encontrada con id: 99"));

        mockMvc.perform(get("/api/evaluaciones/99"))
                .andExpect(status().isNotFound());
    }
}
