CREATE TABLE beneficio (
                           id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                           estudiante_id BIGINT NOT NULL,
                           tipo          VARCHAR(50)  NOT NULL,
                           descripcion   TEXT,
                           estado        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO',
                           fecha_inicio  DATE         NOT NULL,
                           fecha_fin     DATE,
                           created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);