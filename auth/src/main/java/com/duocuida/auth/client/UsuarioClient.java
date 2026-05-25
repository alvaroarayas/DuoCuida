package com.duocuida.auth.client;

import com.duocuida.auth.dto.UsuarioDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UsuarioClient
{

    private final WebClient webClient;

    public UsuarioClient(WebClient webClient)
    {
        this.webClient = webClient;
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id)
    {
        return webClient.get()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .block();
    }
}