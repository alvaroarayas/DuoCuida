package com.duocuida.solicitudes.client;

import com.duocuida.solicitudes.dto.PerfilEstudianteDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PerfilEstudianteClient {

    private final WebClient webClient;

    public PerfilEstudianteClient(@Qualifier("perfilWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public PerfilEstudianteDTO obtenerPerfilPorId(Long id) {
        return webClient.get()
                .uri("/api/perfiles/{id}", id)
                .retrieve()
                .bodyToMono(PerfilEstudianteDTO.class)
                .block();
    }
}