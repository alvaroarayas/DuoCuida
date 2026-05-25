package com.duocuida.solicitudes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "perfilWebClient")
    public WebClient perfilWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean(name = "notificacionWebClient")
    public WebClient notificacionWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8090")
                .build();
    }
}