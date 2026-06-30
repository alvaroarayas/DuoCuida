package com.duocuida.auth.controller;

import com.duocuida.auth.dto.LoginResponseDTO;
import com.duocuida.auth.exception.RecursoNoEncontradoException;
import com.duocuida.auth.model.Credencial;
import com.duocuida.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService service;

    @Test
    void login_cuandoCredencialesValidas_deberiaRetornarOk() throws Exception {
        LoginResponseDTO respuesta = new LoginResponseDTO(
                "Login correcto", 1L, "ana.gonzalez@duoc.cl", "ESTUDIANTE");
        when(service.login(any())).thenReturn(respuesta);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "correo": "ana.gonzalez@duoc.cl",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.rol").value("ESTUDIANTE"));
    }

    @Test
    void login_cuandoJsonEsInvalido_deberiaRetornarBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "correo": "no-es-un-email",
                                  "password": ""
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(service, never()).login(any());
    }

    @Test
    void registro_cuandoJsonEsValido_deberiaRetornarOk() throws Exception {
        Credencial credencial = new Credencial(
                1L, 1L, "ana.gonzalez@duoc.cl", "123456", true, LocalDateTime.now());
        when(service.registrar(any())).thenReturn(credencial);

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "usuarioId": 1,
                                  "correo": "ana.gonzalez@duoc.cl",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.correo").value("ana.gonzalez@duoc.cl"));
    }

    @Test
    void login_cuandoCorreoNoExiste_deberiaRetornarNotFound() throws Exception {
        when(service.login(any()))
                .thenThrow(new RecursoNoEncontradoException("No existe credencial para ese correo"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "correo": "noexiste@duoc.cl",
                                  "password": "123456"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}
