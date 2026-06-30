package com.duocuida.planes.controller;

import com.duocuida.planes.dto.PlanResponseDTO;
import com.duocuida.planes.exception.ResourceNotFoundException;
import com.duocuida.planes.service.PlanService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlanController.class)
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlanService service;

    private PlanResponseDTO crearResponseEjemplo() {
        PlanResponseDTO dto = new PlanResponseDTO();
        dto.setId(1L);
        dto.setEvaluacionId(1L);
        dto.setGestorId(1L);
        dto.setDescripcion("Plan de apoyo academico");
        dto.setObjetivo("Mejorar rendimiento del estudiante");
        dto.setEstado("ACTIVO");
        dto.setFechaInicio(LocalDate.of(2025, 6, 1));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDePlanes() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/planes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Plan de apoyo academico"))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarPlan() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(crearResponseEjemplo());

        mockMvc.perform(get("/api/planes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.objetivo").value("Mejorar rendimiento del estudiante"));
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.obtenerPorId(99L))
                .thenThrow(new ResourceNotFoundException("Plan no encontrado con id: 99"));

        mockMvc.perform(get("/api/planes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.crear(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/planes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "evaluacionId": 1,
                                  "gestorId": 1,
                                  "descripcion": "Plan de apoyo academico",
                                  "objetivo": "Mejorar rendimiento del estudiante",
                                  "estado": "ACTIVO",
                                  "fechaInicio": "2025-06-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Plan de apoyo academico"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/planes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "evaluacionId": null,
                                  "gestorId": null,
                                  "descripcion": "",
                                  "objetivo": "",
                                  "estado": "INVALIDO",
                                  "fechaInicio": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void eliminar_deberiaRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/api/planes/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }
}
