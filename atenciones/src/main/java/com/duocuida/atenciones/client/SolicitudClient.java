package com.duocuida.atenciones.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SolicitudClient {

    private static final Logger log = LoggerFactory.getLogger(SolicitudClient.class);

    private final WebClient webClient;

    public SolicitudClient(@Value("${solicitudes.service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public boolean existeSolicitud(Long solicitudId) {
        try {
            log.info("Verificando existencia de solicitud con id {}", solicitudId);
            webClient.get()
                    .uri("/api/solicitudes/{id}", solicitudId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Solicitud con id {} no encontrada: {}", solicitudId, e.getMessage());
            return false;
        }
    }
}