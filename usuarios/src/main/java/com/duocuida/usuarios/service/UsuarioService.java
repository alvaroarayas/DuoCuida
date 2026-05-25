package com.duocuida.usuarios.service;

import com.duocuida.usuarios.dto.UsuarioRequestDTO;
import com.duocuida.usuarios.dto.UsuarioResponseDTO;
import com.duocuida.usuarios.exception.ResourceNotFoundException;
import com.duocuida.usuarios.model.Usuario;
import com.duocuida.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository repository;

    public List<UsuarioResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public UsuarioResponseDTO obtenerPorId(Long id) {
        log.info("Buscando usuario con id {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return toDTO(usuario);
    }

    public List<UsuarioResponseDTO> obtenerPorRol(String rol) {
        log.info("Buscando usuarios por rol: {}", rol);
        return repository.findByRol(rol).stream().map(this::toDTO).toList();
    }

    public List<UsuarioResponseDTO> obtenerActivos() {
        log.info("Obteniendo usuarios activos");
        return repository.findByActivo(true).stream().map(this::toDTO).toList();
    }

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        log.info("Creando usuario con email {}", dto.getEmail());
        Usuario usuario = toEntity(dto);
        Usuario guardado = repository.save(usuario);
        log.info("Usuario creado con id {}", guardado.getId());
        return toDTO(guardado);
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        log.info("Actualizando usuario con id {}", id);
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        return toDTO(repository.save(usuario));
    }

    public void eliminar(Long id) {
        log.info("Eliminando usuario con id {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        repository.deleteById(id);
        log.info("Usuario con id {} eliminado", id);
    }

    private UsuarioResponseDTO toDTO(Usuario u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }

    private Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellido(dto.getApellido());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        u.setRol(dto.getRol());
        return u;
    }
}