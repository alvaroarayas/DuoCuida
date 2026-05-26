-- Microservicio: solicitudes
-- Ubicación sugerida:
-- solicitudes/src/main/resources/db/migration/V1__create_tables.sql

CREATE TABLE IF NOT EXISTS tipo_solicitud (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS estado_solicitud (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitud_apoyo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    perfil_estudiante_id BIGINT NOT NULL,
    tipo_solicitud_id BIGINT NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    antecedentes VARCHAR(1000) NOT NULL,
    estado_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    CONSTRAINT fk_solicitud_tipo
        FOREIGN KEY (tipo_solicitud_id) REFERENCES tipo_solicitud(id),
    CONSTRAINT fk_solicitud_estado
        FOREIGN KEY (estado_id) REFERENCES estado_solicitud(id)
);

CREATE TABLE IF NOT EXISTS historial_estado_solicitud (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado_anterior_id BIGINT NULL,
    estado_nuevo_id BIGINT NOT NULL,
    observacion VARCHAR(500) NOT NULL,
    fecha_cambio DATETIME NOT NULL,
    solicitud_id BIGINT NOT NULL,
    CONSTRAINT fk_historial_estado_anterior
        FOREIGN KEY (estado_anterior_id) REFERENCES estado_solicitud(id),
    CONSTRAINT fk_historial_estado_nuevo
        FOREIGN KEY (estado_nuevo_id) REFERENCES estado_solicitud(id),
    CONSTRAINT fk_historial_solicitud
        FOREIGN KEY (solicitud_id) REFERENCES solicitud_apoyo(id)
);
