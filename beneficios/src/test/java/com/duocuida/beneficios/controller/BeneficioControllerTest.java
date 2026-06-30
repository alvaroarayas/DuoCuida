package com.duocuida.beneficios.controller;

import com.duocuida.beneficios.dto.BeneficioResponseDTO;
import com.duocuida.beneficios.exception.ResourceNotFoundException;
import com.duocuida.beneficios.service.BeneficioService;
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

@WebMvcTest(BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BeneficioService service;

    private BeneficioResponseDTO crearResponseEjemplo() {
        BeneficioResponseDTO dto = new BeneficioResponseDTO();
        dto.setId(1L);
        dto.setEstudianteId(1L);
        dto.setTipo("BECA");
        dto.setDescripcion("Beca de alimentacion");
        dto.setEstado("ACTIVO");
        dto.setFechaInicio(LocalDate.of(2025, 6, 1));
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    @Test
    void getAll_deberiaRetornarListaDeBeneficios() throws Exception {
        when(service.findAll()).thenReturn(List.of(crearResponseEjemplo()));

        mockMvc.perform(get("/api/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipo").value("BECA"));
    }

    @Test
    void create_cuandoJsonEsValido_deberiaRetornarCreated() throws Exception {
        when(service.save(any())).thenReturn(crearResponseEjemplo());

        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estudianteId": 1,
                                  "tipo": "BECA",
                                  "descripcion": "Beca de alimentacion",
                                  "estado": "ACTIVO",
                                  "fechaInicio": "2025-06-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("BECA"));
    }

    @Test
    void create_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "estudianteId": null,
                                  "tipo": "INVALIDO",
                                  "estado": "INVALIDO",
                                  "fechaInicio": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).save(any());
    }

    @Test
    void getById_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.findById(99L))
                .thenThrow(new ResourceNotFoundException("Beneficio no encontrado con id: 99"));

        mockMvc.perform(get("/api/beneficios/99"))
                .andExpect(status().isNotFound());
    }
}
