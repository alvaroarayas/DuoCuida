CREATE TABLE plan (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluacion_id  BIGINT       NOT NULL,
    gestor_id      BIGINT       NOT NULL,
    descripcion    TEXT         NOT NULL,
    objetivo       VARCHAR(255) NOT NULL,
    estado         VARCHAR(50)  NOT NULL DEFAULT 'ACTIVO',
    fecha_inicio   DATE         NOT NULL,
    fecha_fin      DATE,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);