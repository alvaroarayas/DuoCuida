-- Microservicio: notificaciones
-- Ubicación sugerida:
-- notificaciones/src/main/resources/db/migration/V1__create_tables.sql

CREATE TABLE IF NOT EXISTS notificacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    solicitud_id BIGINT NULL,
    titulo VARCHAR(150) NOT NULL,
    mensaje VARCHAR(500) NOT NULL,
    leida BOOLEAN NOT NULL,
    fecha_creacion DATETIME NOT NULL
);
