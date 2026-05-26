-- Datos iniciales para microservicio solicitudes
-- perfil_estudiante_id es un ID externo que pertenece al microservicio perfiles.
-- No se crea FK hacia perfiles porque perfiles está en otra base de datos.
-- Ubicación sugerida:
-- solicitudes/src/main/resources/db/migration/V2__insert_data.sql

INSERT INTO tipo_solicitud (id, descripcion, activo) VALUES
(1, 'APOYO_ACADEMICO', TRUE),
(2, 'APOYO_SOCIAL', TRUE),
(3, 'CUIDADO_FAMILIAR', TRUE),
(4, 'SALUD_MENTAL', TRUE),
(5, 'OTRO', TRUE)
ON DUPLICATE KEY UPDATE
descripcion = VALUES(descripcion),
activo = VALUES(activo);

INSERT INTO estado_solicitud (id, descripcion, activo) VALUES
(1, 'REGISTRADA', TRUE),
(2, 'EN_REVISION', TRUE),
(3, 'EVALUADA', TRUE),
(4, 'EN_PLAN', TRUE),
(5, 'CERRADA', TRUE),
(6, 'RECHAZADA', TRUE)
ON DUPLICATE KEY UPDATE
descripcion = VALUES(descripcion),
activo = VALUES(activo);

INSERT INTO solicitud_apoyo (
    id,
    perfil_estudiante_id,
    tipo_solicitud_id,
    descripcion,
    antecedentes,
    estado_id,
    fecha_creacion
) VALUES
(
    1,
    1,
    3,
    'Solicita apoyo por responsabilidades de cuidado familiar.',
    'La estudiante cuida a su abuela tres veces por semana y requiere flexibilidad académica.',
    1,
    NOW()
)
ON DUPLICATE KEY UPDATE
perfil_estudiante_id = VALUES(perfil_estudiante_id),
tipo_solicitud_id = VALUES(tipo_solicitud_id),
descripcion = VALUES(descripcion),
antecedentes = VALUES(antecedentes),
estado_id = VALUES(estado_id);

INSERT INTO historial_estado_solicitud (
    id,
    estado_anterior_id,
    estado_nuevo_id,
    observacion,
    fecha_cambio,
    solicitud_id
) VALUES
(
    1,
    NULL,
    1,
    'Solicitud registrada inicialmente',
    NOW(),
    1
)
ON DUPLICATE KEY UPDATE
estado_anterior_id = VALUES(estado_anterior_id),
estado_nuevo_id = VALUES(estado_nuevo_id),
observacion = VALUES(observacion),
solicitud_id = VALUES(solicitud_id);
