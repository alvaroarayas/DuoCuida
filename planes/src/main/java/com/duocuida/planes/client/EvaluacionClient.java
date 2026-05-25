package com.duocuida.planes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EvaluacionClient {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionClient.class);

    private final WebClient webClient;

    public EvaluacionClient(@Value("${evaluaciones.service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public boolean existeEvaluacion(Long evaluacionId) {
        try {
            log.info("Verificando existencia de evaluacion con id {}", evaluacionId);
            webClient.get()
                    .uri("/api/evaluaciones/{id}", evaluacionId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Evaluacion con id {} no encontrada: {}", evaluacionId, e.getMessage());
            return false;
        }
    }
}