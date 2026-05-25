CREATE TABLE atencion (
                          id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                          solicitud_id BIGINT       NOT NULL,
                          gestor_id    BIGINT       NOT NULL,
                          tipo         VARCHAR(100) NOT NULL,
                          descripcion  TEXT         NOT NULL,
                          estado       VARCHAR(50)  NOT NULL DEFAULT 'AGENDADA',
                          fecha        DATE         NOT NULL,
                          hora         TIME         NOT NULL,
                          created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);