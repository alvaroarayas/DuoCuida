package com.duocuida.auth.service;

import com.duocuida.auth.client.UsuarioClient;
import com.duocuida.auth.dto.LoginRequestDTO;
import com.duocuida.auth.dto.LoginResponseDTO;
import com.duocuida.auth.dto.RegistroRequestDTO;
import com.duocuida.auth.dto.UsuarioDTO;
import com.duocuida.auth.exception.RecursoNoEncontradoException;
import com.duocuida.auth.exception.ReglaNegocioException;
import com.duocuida.auth.model.Credencial;
import com.duocuida.auth.repository.CredencialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService
{

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final CredencialRepository credencialRepository;
    private final UsuarioClient usuarioClient;

    public AuthService(CredencialRepository credencialRepository, UsuarioClient usuarioClient) {
        this.credencialRepository = credencialRepository;
        this.usuarioClient = usuarioClient;
    }

    public Credencial registrar(RegistroRequestDTO dto) {
        logger.info("Registrando credencial para usuario ID {}", dto.getUsuarioId());

        if (credencialRepository.existsByCorreo(dto.getCorreo())) {
            throw new ReglaNegocioException("Ya existe una credencial con ese correo");
        }

        if (credencialRepository.existsByUsuarioId(dto.getUsuarioId())) {
            throw new ReglaNegocioException("El usuario ya tiene credencial registrada");
        }

        UsuarioDTO usuario = usuarioClient.obtenerUsuarioPorId(dto.getUsuarioId());

        if (usuario == null || usuario.getId() == null) {
            throw new ReglaNegocioException("No se puede registrar credencial porque el usuario no existe");
        }

        if (!usuario.getActivo()) {
            throw new ReglaNegocioException("No se puede registrar credencial para un usuario inactivo");
        }

        if (!usuario.getEmail().equalsIgnoreCase(dto.getCorreo())) {
            throw new ReglaNegocioException("El correo no coincide con el usuario registrado");
        }

        Credencial credencial = new Credencial();
        credencial.setUsuarioId(usuario.getId());
        credencial.setCorreo(dto.getCorreo());
        credencial.setPassword(dto.getPassword());
        credencial.setActivo(true);
        credencial.setFechaCreacion(LocalDateTime.now());

        return credencialRepository.save(credencial);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        logger.info("Intento de login para correo {}", dto.getCorreo());

        Credencial credencial = credencialRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe credencial para ese correo"));

        if (!credencial.getActivo()) {
            throw new ReglaNegocioException("La credencial está inactiva");
        }

        if (!credencial.getPassword().equals(dto.getPassword())) {
            throw new ReglaNegocioException("Correo o contraseña incorrectos");
        }

        UsuarioDTO usuario = usuarioClient.obtenerUsuarioPorId(credencial.getUsuarioId());

        if (usuario == null || usuario.getId() == null) {
            throw new ReglaNegocioException("No se pudo validar el usuario asociado");
        }

        if (!usuario.getActivo()) {
            throw new ReglaNegocioException("El usuario está inactivo");
        }

        String rol = "SIN_ROL";

        if (usuario.getRol() != null) {
            rol = usuario.getRol().getNombre();
        }

        return new LoginResponseDTO(
                "Login correcto",
                usuario.getId(),
                usuario.getEmail(),
                rol
        );
    }
}