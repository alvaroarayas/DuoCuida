package com.duocuida.solicitudes.client;

import com.duocuida.solicitudes.dto.NotificacionRequestDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NotificacionClient {

    private final WebClient webClient;

    public NotificacionClient(@Qualifier("notificacionWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void crearNotificacion(NotificacionRequestDTO dto) {
        webClient.post()
                .uri("/api/notificaciones")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}