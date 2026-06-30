package com.duocuida.solicitudes.controller;

import com.duocuida.solicitudes.exception.RecursoNoEncontradoException;
import com.duocuida.solicitudes.model.EstadoSolicitud;
import com.duocuida.solicitudes.model.SolicitudApoyo;
import com.duocuida.solicitudes.model.TipoSolicitud;
import com.duocuida.solicitudes.service.SolicitudApoyoService;
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

@WebMvcTest(SolicitudApoyoController.class)
class SolicitudApoyoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SolicitudApoyoService service;

    private SolicitudApoyo crearSolicitudEjemplo() {
        SolicitudApoyo s = new SolicitudApoyo();
        s.setId(1L);
        s.setPerfilEstudianteId(1L);
        s.setTipoSolicitud(new TipoSolicitud(1L, "APOYO_ACADEMICO", true));
        s.setDescripcion("Solicito apoyo academico");
        s.setAntecedentes("Tengo dificultades en varias asignaturas");
        s.setEstado(new EstadoSolicitud(1L, "REGISTRADA", true));
        s.setFechaCreacion(LocalDateTime.now());
        return s;
    }

    @Test
    void listar_deberiaRetornarListaDeSolicitudes() throws Exception {
        when(service.listar()).thenReturn(List.of(crearSolicitudEjemplo()));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("Solicito apoyo academico"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarOk() throws Exception {
        when(service.crear(any())).thenReturn(crearSolicitudEjemplo());

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "perfilEstudianteId": 1,
                                  "tipoSolicitudId": 1,
                                  "descripcion": "Solicito apoyo academico",
                                  "antecedentes": "Tengo dificultades en varias asignaturas"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Solicito apoyo academico"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "perfilEstudianteId": null,
                                  "tipoSolicitudId": null,
                                  "descripcion": "",
                                  "antecedentes": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("No existe solicitud con ID: 99"));

        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound());
    }
}