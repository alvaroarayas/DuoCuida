package com.duocuida.auth.controller;

import com.duocuida.auth.dto.LoginRequestDTO;
import com.duocuida.auth.dto.LoginResponseDTO;
import com.duocuida.auth.dto.RegistroRequestDTO;
import com.duocuida.auth.model.Credencial;
import com.duocuida.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{

    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Credencial> registrar(@Valid @RequestBody RegistroRequestDTO dto)
    {
        return ResponseEntity.ok(authService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto)
    {
        return ResponseEntity.ok(authService.login(dto));
    }
}