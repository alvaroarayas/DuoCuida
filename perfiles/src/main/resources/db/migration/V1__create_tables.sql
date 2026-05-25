CREATE TABLE perfil_estudiante (
                                   id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   usuario_id BIGINT       NOT NULL,
                                   nombre     VARCHAR(100) NOT NULL,
                                   apellido   VARCHAR(100) NOT NULL,
                                   email      VARCHAR(150) NOT NULL UNIQUE,
                                   telefono   VARCHAR(20),
                                   carrera    VARCHAR(150) NOT NULL,
                                   sede       VARCHAR(100) NOT NULL,
                                   activo     BOOLEAN      NOT NULL DEFAULT TRUE,
                                   created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);