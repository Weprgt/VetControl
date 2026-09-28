-- =============================================================
-- VETCONTROL - DATOS INICIALES OBLIGATORIOS
-- Ejecutar después de 01-esquema.sql
-- Compatible con MySQL 8+
-- =============================================================

USE vetcontrol;

START TRANSACTION;

-- Roles indispensables para el control de permisos.
INSERT IGNORE INTO roles (
    nombre,
    descripcion
)
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
-- CUENTA INICIAL
-- Usuario: admin
-- Contraseña temporal: Admin123*
--
-- La contraseña se almacena como hash PBKDF2 con sal,
-- nunca como texto sin protección.
-- Al utilizar el sistema en un entorno real, se recomienda
-- cambiar esta contraseña inmediatamente.
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

COMMIT;

-- Comprobación de la instalación mínima.
SELECT
    u.nombre_usuario,
    u.nombre_completo,
    r.nombre AS rol,
    u.activo
FROM usuarios u
INNER JOIN roles r
    ON r.id_rol = u.id_rol
WHERE u.nombre_usuario = 'admin';

