-- Datos iniciales para microservicio auth
-- IMPORTANTE: Esta versión es simplificada, siguiendo la complejidad de los ejemplos.
-- Las contraseñas están en texto plano solo para pruebas académicas iniciales.
-- Ubicación sugerida:
-- auth/src/main/resources/db/migration/V2__insert_data.sql

INSERT INTO credencial (id, usuario_id, correo, password, activo, fecha_creacion) VALUES
    (1, 1, 'ana.gonzalez@duoc.cl', '123456', TRUE, NOW()),
    (2, 2, 'carlos.ramirez@duoc.cl', '123456', TRUE, NOW()),
    (3, 3, 'sofia.martinez@duoc.cl', '123456', TRUE, NOW())
    ON DUPLICATE KEY UPDATE
                         usuario_id = VALUES(usuario_id),
                         correo = VALUES(correo),
                         password = VALUES(password),
                         activo = VALUES(activo);
