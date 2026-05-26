-- Microservicio: auth
-- Ubicación sugerida:
-- auth/src/main/resources/db/migration/V1__create_tables.sql

CREATE TABLE IF NOT EXISTS credencial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    activo BOOLEAN NOT NULL,
    fecha_creacion DATETIME NOT NULL
);
