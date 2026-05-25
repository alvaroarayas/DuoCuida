CREATE TABLE usuario (
                         id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre     VARCHAR(100) NOT NULL,
                         apellido   VARCHAR(100) NOT NULL,
                         email      VARCHAR(150) NOT NULL UNIQUE,
                         password   VARCHAR(255) NOT NULL,
                         rol        VARCHAR(50)  NOT NULL DEFAULT 'ESTUDIANTE',
                         activo     BOOLEAN      NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);