-- =============================================================
-- VETCONTROL - ESQUEMA DE BASE DE DATOS
-- Compatible con MySQL 8+
-- =============================================================

CREATE DATABASE IF NOT EXISTS vetcontrol
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE vetcontrol;

-- =============================================================
-- 1. ROLES
-- =============================================================

CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(150),

    PRIMARY KEY (id_rol),
    UNIQUE KEY uk_roles_nombre (nombre)
) ENGINE = InnoDB;

-- =============================================================
-- 2. USUARIOS
-- =============================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT,
    id_rol INT NOT NULL,
    nombre_completo VARCHAR(120) NOT NULL,
    nombre_usuario VARCHAR(50) NOT NULL,
    contrasena_hash VARCHAR(255) NOT NULL,
    correo VARCHAR(120),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_nombre_usuario (nombre_usuario),
    UNIQUE KEY uk_usuario_correo (correo),

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES roles (id_rol)
) ENGINE = InnoDB;

-- =============================================================
-- 3. VETERINARIOS
-- =============================================================

CREATE TABLE IF NOT EXISTS veterinarios (
    id_veterinario INT AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    especialidad VARCHAR(100),
    telefono_profesional VARCHAR(20),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_veterinario),
    UNIQUE KEY uk_veterinario_usuario (id_usuario),

    CONSTRAINT fk_veterinario_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id_usuario)
) ENGINE = InnoDB;

-- =============================================================
-- 4. CLIENTES
-- =============================================================

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INT AUTO_INCREMENT,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(120),
    direccion VARCHAR(250),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_cliente),
    UNIQUE KEY uk_cliente_correo (correo),
    KEY idx_cliente_nombre (nombres, apellidos),
    KEY idx_cliente_telefono (telefono)
) ENGINE = InnoDB;

-- =============================================================
-- 5. MASCOTAS
-- =============================================================

CREATE TABLE IF NOT EXISTS mascotas (
    id_mascota INT AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    numero_expediente VARCHAR(20) NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    especie VARCHAR(40) NOT NULL,
    raza VARCHAR(60),
    sexo ENUM('MACHO', 'HEMBRA', 'DESCONOCIDO')
        NOT NULL DEFAULT 'DESCONOCIDO',
    fecha_nacimiento DATE,
    color VARCHAR(50),
    observaciones VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_mascota),
    UNIQUE KEY uk_mascota_expediente (numero_expediente),
    KEY idx_mascota_nombre (nombre),

    CONSTRAINT fk_mascota_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes (id_cliente)
) ENGINE = InnoDB;

-- =============================================================
-- 6. CITAS
-- horario_activo solo conserva la fecha cuando la cita sigue
-- programada o confirmada. Así los horarios finalizados pueden
-- volver a utilizarse.
-- =============================================================

CREATE TABLE IF NOT EXISTS citas (
    id_cita INT AUTO_INCREMENT,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    motivo VARCHAR(250) NOT NULL,
    estado ENUM(
        'PROGRAMADA',
        'CONFIRMADA',
        'ATENDIDA',
        'CANCELADA',
        'NO_ASISTIO'
    ) NOT NULL DEFAULT 'PROGRAMADA',
    observaciones VARCHAR(500),
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    horario_activo DATETIME
        GENERATED ALWAYS AS (
            CASE
                WHEN estado IN ('PROGRAMADA', 'CONFIRMADA')
                    THEN fecha_hora
                ELSE NULL
            END
        ) STORED,

    PRIMARY KEY (id_cita),
    UNIQUE KEY uk_veterinario_horario (
        id_veterinario,
        horario_activo
    ),
    UNIQUE KEY uk_mascota_horario (
        id_mascota,
        horario_activo
    ),
    KEY idx_cita_fecha (fecha_hora),

    CONSTRAINT fk_cita_mascota
        FOREIGN KEY (id_mascota)
        REFERENCES mascotas (id_mascota),

    CONSTRAINT fk_cita_veterinario
        FOREIGN KEY (id_veterinario)
        REFERENCES veterinarios (id_veterinario)
) ENGINE = InnoDB;

-- =============================================================
-- 7. HISTORIAL CLÍNICO
-- =============================================================

CREATE TABLE IF NOT EXISTS historial_clinico (
    id_historial INT AUTO_INCREMENT,
    id_mascota INT NOT NULL,
    id_veterinario INT NOT NULL,
    id_cita INT,
    fecha_atencion DATETIME NOT NULL,
    tipo_registro ENUM(
        'CONSULTA',
        'DIAGNOSTICO',
        'TRATAMIENTO',
        'VACUNA',
        'CONTROL',
        'OTRO'
    ) NOT NULL DEFAULT 'CONSULTA',
    motivo_consulta VARCHAR(250),
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    nombre_vacuna VARCHAR(120),
    lote_vacuna VARCHAR(60),
    proxima_dosis DATE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_historial),
    KEY idx_historial_mascota_fecha (
        id_mascota,
        fecha_atencion
    ),

    CONSTRAINT fk_historial_mascota
        FOREIGN KEY (id_mascota)
        REFERENCES mascotas (id_mascota),

    CONSTRAINT fk_historial_veterinario
        FOREIGN KEY (id_veterinario)
        REFERENCES veterinarios (id_veterinario),

    CONSTRAINT fk_historial_cita
        FOREIGN KEY (id_cita)
        REFERENCES citas (id_cita)
) ENGINE = InnoDB;

-- =============================================================
-- 8. PRODUCTOS
-- =============================================================

CREATE TABLE IF NOT EXISTS productos (
    id_producto INT AUTO_INCREMENT,
    codigo VARCHAR(15) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    categoria ENUM(
        'MEDICAMENTO',
        'VACUNA',
        'ALIMENTO',
        'INSUMO',
        'OTRO'
    ) NOT NULL,
    descripcion VARCHAR(500),
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 0,
    precio_compra DECIMAL(10, 2),
    precio_venta DECIMAL(10, 2),
    fecha_vencimiento DATE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_producto),
    UNIQUE KEY uk_producto_codigo (codigo),
    KEY idx_producto_nombre (nombre),
    KEY idx_producto_categoria (categoria),

    CONSTRAINT chk_producto_stock_actual
        CHECK (stock_actual >= 0),

    CONSTRAINT chk_producto_stock_minimo
        CHECK (stock_minimo >= 0),

    CONSTRAINT chk_producto_precio_compra
        CHECK (precio_compra IS NULL OR precio_compra >= 0),

    CONSTRAINT chk_producto_precio_venta
        CHECK (precio_venta IS NULL OR precio_venta >= 0)
) ENGINE = InnoDB;

-- =============================================================
-- 9. MOVIMIENTOS DE INVENTARIO
-- =============================================================

CREATE TABLE IF NOT EXISTS movimientos_inventario (
    id_movimiento INT AUTO_INCREMENT,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo_movimiento ENUM(
        'ENTRADA',
        'SALIDA',
        'AJUSTE_ENTRADA',
        'AJUSTE_SALIDA'
    ) NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(250),
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id_movimiento),
    KEY idx_movimiento_producto_fecha (
        id_producto,
        fecha_movimiento
    ),

    CONSTRAINT chk_movimiento_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT fk_movimiento_producto
        FOREIGN KEY (id_producto)
        REFERENCES productos (id_producto),

    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id_usuario)
) ENGINE = InnoDB;

