package com.duocuida.auth.repository;

import com.duocuida.auth.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredencialRepository extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByUsuarioId(Long usuarioId);
}