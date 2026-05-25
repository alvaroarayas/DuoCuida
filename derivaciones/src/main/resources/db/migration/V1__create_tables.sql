CREATE TABLE derivacion (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    solicitud_id  BIGINT       NOT NULL,
    gestor_id     BIGINT       NOT NULL,
    unidad_destino VARCHAR(150) NOT NULL,
    motivo        TEXT         NOT NULL,
    estado        VARCHAR(50)  NOT NULL DEFAULT 'PENDIENTE',
    fecha         DATE         NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);