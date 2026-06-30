package com.duocuida.notificaciones.controller;

import com.duocuida.notificaciones.exception.RecursoNoEncontradoException;
import com.duocuida.notificaciones.model.Notificacion;
import com.duocuida.notificaciones.service.NotificacionService;
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

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService service;

    private Notificacion crearNotificacionEjemplo() {
        Notificacion n = new Notificacion();
        n.setId(1L);
        n.setUsuarioId(1L);
        n.setSolicitudId(1L);
        n.setTitulo("Solicitud registrada");
        n.setMensaje("Su solicitud fue registrada correctamente");
        n.setLeida(false);
        n.setFechaCreacion(LocalDateTime.now());
        return n;
    }

    @Test
    void listar_deberiaRetornarListaDeNotificaciones() throws Exception {
        when(service.listar()).thenReturn(List.of(crearNotificacionEjemplo()));

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Solicitud registrada"));
    }

    @Test
    void crear_cuandoJsonEsValido_deberiaRetornarOk() throws Exception {
        when(service.crear(any())).thenReturn(crearNotificacionEjemplo());

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": 1,
                                  "solicitudId": 1,
                                  "titulo": "Solicitud registrada",
                                  "mensaje": "Su solicitud fue registrada correctamente"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Solicitud registrada"));
    }

    @Test
    void crear_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": null,
                                  "titulo": "",
                                  "mensaje": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).crear(any());
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.buscarPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("No existe notificación con ID: 99"));

        mockMvc.perform(get("/api/notificaciones/99"))
                .andExpect(status().isNotFound());
    }
}