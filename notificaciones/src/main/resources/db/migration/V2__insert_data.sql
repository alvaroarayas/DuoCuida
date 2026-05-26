-- Datos iniciales para microservicio notificaciones
-- usuario_id y solicitud_id son IDs externos de otros microservicios.
-- No se crean FK físicas entre bases de datos distintas.
-- Ubicación sugerida:
-- notificaciones/src/main/resources/db/migration/V2__insert_data.sql

INSERT INTO notificacion (
    id,
    usuario_id,
    solicitud_id,
    titulo,
    mensaje,
    leida,
    fecha_creacion
) VALUES
(
    1,
    1,
    1,
    'Solicitud registrada',
    'Su solicitud de apoyo fue registrada correctamente.',
    FALSE,
    NOW()
)
ON DUPLICATE KEY UPDATE
usuario_id = VALUES(usuario_id),
solicitud_id = VALUES(solicitud_id),
titulo = VALUES(titulo),
mensaje = VALUES(mensaje),
leida = VALUES(leida);
