CREATE TABLE evaluacion (
                            id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                            solicitud_id BIGINT       NOT NULL,
                            gestor_id    BIGINT       NOT NULL,
                            resultado    VARCHAR(50)  NOT NULL,
                            observacion  TEXT,
                            puntaje      INT,
                            fecha        DATE         NOT NULL,
                            created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);