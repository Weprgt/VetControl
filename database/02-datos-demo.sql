-- =============================================================
-- VETCONTROL - DATOS DE DEMOSTRACIÓN
-- Ejecutar después de 01-esquema.sql
-- Compatible con MySQL 8+
-- =============================================================

USE vetcontrol;

START TRANSACTION;

-- =============================================================
-- ROLES
-- =============================================================

INSERT IGNORE INTO roles (nombre, descripcion)
VALUES
    (
        'ADMINISTRADOR',
        'Administra usuarios, inventario y reportes'
    ),
    (
        'VETERINARIO',
        'Gestiona citas e historiales clínicos'
    ),
    (
        'RECEPCIONISTA',
        'Gestiona clientes, mascotas y citas'
    );

-- =============================================================
-- USUARIOS
-- Contraseñas de demostración:
-- admin             -> Admin123*
-- ana.recepcion     -> Recepcion123*
-- daniel.lopez      -> Veterinario123*
-- laura.morales     -> Veterinario123*
-- Los valores almacenados son hashes PBKDF2, nunca texto plano.
-- =============================================================

INSERT INTO usuarios (
    id_rol,
    nombre_completo,
    nombre_usuario,
    contrasena_hash,
    correo,
    activo
)
SELECT
    r.id_rol,
    'Administrador VetControl',
    'admin',
    'pbkdf2$210000$MG3MX30IYpAAHeW2ppX+Zw==$7gCZosuvBHvc1zu9BZmuAmQevXVcSHEpl8ZCCoap8xU=',
    'admin@vetcontrol.test',
    TRUE
FROM roles r
WHERE r.nombre = 'ADMINISTRADOR'
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios u
      WHERE u.nombre_usuario = 'admin'
  );

INSERT INTO usuarios (
    id_rol,
    nombre_completo,
    nombre_usuario,
    contrasena_hash,
    correo,
    activo
)
SELECT
    r.id_rol,
    'Ana Martínez',
    'ana.recepcion',
    'pbkdf2$210000$WFRwi9OLkrmZNM+FfV7JIA==$GVVSuCCRSjdSCK/YS4U42Sk/wntYtAB/DuGDE59FGaA=',
    'ana.recepcion@vetcontrol.test',
    TRUE
FROM roles r
WHERE r.nombre = 'RECEPCIONISTA'
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios u
      WHERE u.nombre_usuario = 'ana.recepcion'
  );

INSERT INTO usuarios (
    id_rol,
    nombre_completo,
    nombre_usuario,
    contrasena_hash,
    correo,
    activo
)
SELECT
    r.id_rol,
    'Dr. Daniel López',
    'daniel.lopez',
    'pbkdf2$210000$JQWsezLX0CZtWjz1Kn7jJA==$sPkAkHX4DRLssUe56hn7WNYC2yNMIm/d6mKQOosK0ww=',
    'daniel.lopez@vetcontrol.test',
    TRUE
FROM roles r
WHERE r.nombre = 'VETERINARIO'
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios u
      WHERE u.nombre_usuario = 'daniel.lopez'
  );

INSERT INTO usuarios (
    id_rol,
    nombre_completo,
    nombre_usuario,
    contrasena_hash,
    correo,
    activo
)
SELECT
    r.id_rol,
    'Dra. Laura Morales',
    'laura.morales',
    'pbkdf2$210000$0c0mF0NeVUyq5tDF65dxTw==$9JhfBlxIgyuCMzYRD3MP3qtI8qWXKyaTwhoyxa2r35Q=',
    'laura.morales@vetcontrol.test',
    TRUE
FROM roles r
WHERE r.nombre = 'VETERINARIO'
  AND NOT EXISTS (
      SELECT 1
      FROM usuarios u
      WHERE u.nombre_usuario = 'laura.morales'
  );

-- =============================================================
-- DATOS PROFESIONALES DE VETERINARIOS
-- =============================================================

INSERT INTO veterinarios (
    id_usuario,
    especialidad,
    telefono_profesional,
    activo
)
SELECT
    u.id_usuario,
    'Cirugía veterinaria',
    '5552-2002',
    TRUE
FROM usuarios u
WHERE u.nombre_usuario = 'daniel.lopez'
  AND NOT EXISTS (
      SELECT 1
      FROM veterinarios v
      WHERE v.id_usuario = u.id_usuario
  );

INSERT INTO veterinarios (
    id_usuario,
    especialidad,
    telefono_profesional,
    activo
)
SELECT
    u.id_usuario,
    'Medicina general',
    '5552-2001',
    TRUE
FROM usuarios u
WHERE u.nombre_usuario = 'laura.morales'
  AND NOT EXISTS (
      SELECT 1
      FROM veterinarios v
      WHERE v.id_usuario = u.id_usuario
  );

-- =============================================================
-- CLIENTES
-- =============================================================

INSERT IGNORE INTO clientes (
    nombres,
    apellidos,
    telefono,
    correo,
    direccion
)
VALUES
    (
        'Tomás',
        'García',
        '5551-1001',
        'tomas.garcia@example.com',
        'Zona 1, Ciudad de Guatemala'
    ),
    (
        'Ana',
        'Morales',
        '5551-1002',
        'ana.morales@example.com',
        'Zona 7, Ciudad de Guatemala'
    ),
    (
        'Carlos',
        'López',
        '5551-1003',
        'carlos.lopez@example.com',
        'Mixco, Guatemala'
    ),
    (
        'Sofía',
        'Ramírez',
        '5551-1004',
        'sofia.ramirez@example.com',
        'Villa Nueva, Guatemala'
    );

-- =============================================================
-- MASCOTAS
-- =============================================================

INSERT INTO mascotas (
    id_cliente,
    numero_expediente,
    nombre,
    especie,
    raza,
    sexo,
    fecha_nacimiento,
    color,
    observaciones
)
SELECT
    c.id_cliente,
    'EXP-001',
    'Luna',
    'Perro',
    'Labrador',
    'HEMBRA',
    '2021-04-12',
    'Dorado',
    'Paciente tranquila y sociable'
FROM clientes c
WHERE c.correo = 'tomas.garcia@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM mascotas m
      WHERE m.numero_expediente = 'EXP-001'
  );

INSERT INTO mascotas (
    id_cliente, numero_expediente, nombre, especie, raza,
    sexo, fecha_nacimiento, color, observaciones
)
SELECT
    c.id_cliente, 'EXP-002', 'Rocky', 'Perro',
    'Pastor alemán', 'MACHO', '2020-08-20',
    'Negro y café',
    'Presenta sensibilidad en la pata trasera derecha'
FROM clientes c
WHERE c.correo = 'tomas.garcia@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM mascotas m
      WHERE m.numero_expediente = 'EXP-002'
  );

INSERT INTO mascotas (
    id_cliente, numero_expediente, nombre, especie, raza,
    sexo, fecha_nacimiento, color, observaciones
)
SELECT
    c.id_cliente, 'EXP-003', 'Milo', 'Gato',
    'Siamés', 'MACHO', '2022-02-15',
    'Crema', 'Vacunación al día'
FROM clientes c
WHERE c.correo = 'ana.morales@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM mascotas m
      WHERE m.numero_expediente = 'EXP-003'
  );

INSERT INTO mascotas (
    id_cliente, numero_expediente, nombre, especie, raza,
    sexo, fecha_nacimiento, color, observaciones
)
SELECT
    c.id_cliente, 'EXP-004', 'Nala', 'Gato',
    'Común europeo', 'HEMBRA', '2023-06-08',
    'Gris', NULL
FROM clientes c
WHERE c.correo = 'carlos.lopez@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM mascotas m
      WHERE m.numero_expediente = 'EXP-004'
  );

INSERT INTO mascotas (
    id_cliente, numero_expediente, nombre, especie, raza,
    sexo, fecha_nacimiento, color, observaciones
)
SELECT
    c.id_cliente, 'EXP-005', 'Coco', 'Ave',
    'Periquito australiano', 'DESCONOCIDO', '2024-01-10',
    'Verde y amarillo', 'Revisión general'
FROM clientes c
WHERE c.correo = 'sofia.ramirez@example.com'
  AND NOT EXISTS (
      SELECT 1 FROM mascotas m
      WHERE m.numero_expediente = 'EXP-005'
  );

-- =============================================================
-- CITAS FUTURAS
-- Se calculan desde la fecha de instalación para que siempre
-- aparezcan en el panel de próximas citas.
-- =============================================================

INSERT INTO citas (
    id_mascota,
    id_veterinario,
    fecha_hora,
    motivo,
    estado,
    observaciones
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00'),
    'Consulta general',
    'CONFIRMADA',
    'Primera evaluación del paciente'
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'laura.morales'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-001'
  AND NOT EXISTS (
      SELECT 1
      FROM citas c
      WHERE c.id_mascota = m.id_mascota
        AND c.motivo = 'Consulta general'
        AND c.estado IN ('PROGRAMADA', 'CONFIRMADA')
  );

INSERT INTO citas (
    id_mascota, id_veterinario, fecha_hora,
    motivo, estado, observaciones
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:30:00'),
    'Control de tratamiento',
    'PROGRAMADA',
    'Revisar evolución de la pata trasera'
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'daniel.lopez'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-002'
  AND NOT EXISTS (
      SELECT 1
      FROM citas c
      WHERE c.id_mascota = m.id_mascota
        AND c.motivo = 'Control de tratamiento'
        AND c.estado IN ('PROGRAMADA', 'CONFIRMADA')
  );

INSERT INTO citas (
    id_mascota, id_veterinario, fecha_hora,
    motivo, estado, observaciones
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '14:00:00'),
    'Vacunación anual',
    'PROGRAMADA',
    'Llevar cartilla de vacunación'
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'laura.morales'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-003'
  AND NOT EXISTS (
      SELECT 1
      FROM citas c
      WHERE c.id_mascota = m.id_mascota
        AND c.motivo = 'Vacunación anual'
        AND c.estado IN ('PROGRAMADA', 'CONFIRMADA')
  );

-- =============================================================
-- HISTORIAL CLÍNICO
-- =============================================================

INSERT INTO historial_clinico (
    id_mascota,
    id_veterinario,
    id_cita,
    fecha_atencion,
    tipo_registro,
    motivo_consulta,
    diagnostico,
    tratamiento,
    observaciones,
    nombre_vacuna,
    lote_vacuna,
    proxima_dosis
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    NULL,
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 20 DAY), '09:30:00'),
    'CONSULTA',
    'Irritación y comezón en la piel',
    'Dermatitis alérgica leve',
    'Baño medicado y antihistamínico durante cinco días',
    'Evitar productos perfumados durante el tratamiento',
    NULL,
    NULL,
    NULL
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'laura.morales'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-001'
  AND NOT EXISTS (
      SELECT 1
      FROM historial_clinico h
      WHERE h.id_mascota = m.id_mascota
        AND h.tipo_registro = 'CONSULTA'
        AND h.motivo_consulta =
            'Irritación y comezón en la piel'
  );

INSERT INTO historial_clinico (
    id_mascota, id_veterinario, id_cita, fecha_atencion,
    tipo_registro, motivo_consulta, diagnostico, tratamiento,
    observaciones, nombre_vacuna, lote_vacuna, proxima_dosis
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    NULL,
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 12 DAY), '10:45:00'),
    'VACUNA',
    'Aplicación de vacuna anual',
    NULL,
    'Aplicación de vacuna antirrábica',
    'La mascota no presentó reacciones inmediatas',
    'Vacuna antirrábica',
    'LAR-DEMO-084',
    DATE_ADD(CURDATE(), INTERVAL 353 DAY)
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'daniel.lopez'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-002'
  AND NOT EXISTS (
      SELECT 1
      FROM historial_clinico h
      WHERE h.id_mascota = m.id_mascota
        AND h.tipo_registro = 'VACUNA'
        AND h.lote_vacuna = 'LAR-DEMO-084'
  );

INSERT INTO historial_clinico (
    id_mascota, id_veterinario, id_cita, fecha_atencion,
    tipo_registro, motivo_consulta, diagnostico, tratamiento,
    observaciones, nombre_vacuna, lote_vacuna, proxima_dosis
)
SELECT
    m.id_mascota,
    v.id_veterinario,
    NULL,
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 7 DAY), '14:00:00'),
    'DIAGNOSTICO',
    'Sacude la cabeza y se rasca la oreja derecha',
    'Otitis externa',
    'Limpieza ótica y gotas antibióticas durante siete días',
    'Regresar si aparece secreción o pérdida de equilibrio',
    NULL,
    NULL,
    NULL
FROM mascotas m
INNER JOIN usuarios u
    ON u.nombre_usuario = 'laura.morales'
INNER JOIN veterinarios v
    ON v.id_usuario = u.id_usuario
WHERE m.numero_expediente = 'EXP-003'
  AND NOT EXISTS (
      SELECT 1
      FROM historial_clinico h
      WHERE h.id_mascota = m.id_mascota
        AND h.tipo_registro = 'DIAGNOSTICO'
        AND h.diagnostico = 'Otitis externa'
  );

-- =============================================================
-- PRODUCTOS
-- =============================================================

INSERT IGNORE INTO productos (
    codigo,
    nombre,
    categoria,
    descripcion,
    stock_actual,
    stock_minimo,
    precio_compra,
    precio_venta,
    fecha_vencimiento,
    activo
)
VALUES
    (
        'MED-001', 'Amoxicilina 250 mg', 'MEDICAMENTO',
        'Antibiótico veterinario en cápsulas',
        42, 10, 28.50, 42.00, DATE_ADD(CURDATE(), INTERVAL 180 DAY), TRUE
    ),
    (
        'VAC-001', 'Vacuna antirrábica', 'VACUNA',
        'Vacuna anual contra la rabia',
        6, 10, 35.00, 55.00, DATE_ADD(CURDATE(), INTERVAL 90 DAY), TRUE
    ),
    (
        'ALI-001', 'Alimento para cachorro 2 kg', 'ALIMENTO',
        'Alimento balanceado para perros cachorros',
        18, 5, 72.00, 105.00, NULL, TRUE
    ),
    (
        'MED-002', 'Desparasitante oral', 'MEDICAMENTO',
        'Tabletas para desparasitación interna',
        3, 8, 18.00, 30.00, DATE_ADD(CURDATE(), INTERVAL 120 DAY), TRUE
    ),
    (
        'INS-001', 'Guantes desechables', 'INSUMO',
        'Caja de guantes de nitrilo',
        25, 10, 48.00, 65.00, NULL, TRUE
    ),
    (
        'INS-002', 'Jeringas de 5 ml', 'INSUMO',
        'Paquete de jeringas estériles',
        4, 12, 22.00, 35.00, NULL, TRUE
    ),
    (
        'VAC-002', 'Vacuna quíntuple canina', 'VACUNA',
        'Vacuna múltiple para perros',
        9, 5, 45.00, 68.00, DATE_ADD(CURDATE(), INTERVAL 240 DAY), TRUE
    ),
    (
        'OTR-001', 'Collar isabelino mediano', 'OTRO',
        'Collar protector para recuperación',
        7, 3, 32.00, 50.00, NULL, TRUE
    );

-- =============================================================
-- MOVIMIENTOS DE INVENTARIO
-- Son registros históricos de demostración. El stock actual ya
-- refleja las existencias mostradas en la tabla productos.
-- =============================================================

INSERT INTO movimientos_inventario (
    id_producto,
    id_usuario,
    tipo_movimiento,
    cantidad,
    motivo,
    fecha_movimiento
)
SELECT
    p.id_producto,
    u.id_usuario,
    'ENTRADA',
    20,
    'Compra inicial de inventario',
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 15 DAY), '08:00:00')
FROM productos p
INNER JOIN usuarios u
    ON u.nombre_usuario = 'admin'
WHERE p.codigo = 'MED-001'
  AND NOT EXISTS (
      SELECT 1
      FROM movimientos_inventario mi
      WHERE mi.id_producto = p.id_producto
        AND mi.motivo = 'Compra inicial de inventario'
  );

INSERT INTO movimientos_inventario (
    id_producto, id_usuario, tipo_movimiento,
    cantidad, motivo, fecha_movimiento
)
SELECT
    p.id_producto,
    u.id_usuario,
    'SALIDA',
    2,
    'Aplicación en consultas veterinarias',
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 5 DAY), '11:30:00')
FROM productos p
INNER JOIN usuarios u
    ON u.nombre_usuario = 'admin'
WHERE p.codigo = 'VAC-001'
  AND NOT EXISTS (
      SELECT 1
      FROM movimientos_inventario mi
      WHERE mi.id_producto = p.id_producto
        AND mi.motivo = 'Aplicación en consultas veterinarias'
  );

INSERT INTO movimientos_inventario (
    id_producto, id_usuario, tipo_movimiento,
    cantidad, motivo, fecha_movimiento
)
SELECT
    p.id_producto,
    u.id_usuario,
    'AJUSTE_SALIDA',
    1,
    'Ajuste por producto dañado',
    TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '16:15:00')
FROM productos p
INNER JOIN usuarios u
    ON u.nombre_usuario = 'admin'
WHERE p.codigo = 'INS-002'
  AND NOT EXISTS (
      SELECT 1
      FROM movimientos_inventario mi
      WHERE mi.id_producto = p.id_producto
        AND mi.motivo = 'Ajuste por producto dañado'
  );

COMMIT;

-- Comprobación rápida de los datos insertados.
SELECT 'roles' AS tabla, COUNT(*) AS registros FROM roles
UNION ALL
SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'veterinarios', COUNT(*) FROM veterinarios
UNION ALL
SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL
SELECT 'mascotas', COUNT(*) FROM mascotas
UNION ALL
SELECT 'citas', COUNT(*) FROM citas
UNION ALL
SELECT 'historial_clinico', COUNT(*) FROM historial_clinico
UNION ALL
SELECT 'productos', COUNT(*) FROM productos
UNION ALL
SELECT 'movimientos_inventario', COUNT(*) FROM movimientos_inventario;

