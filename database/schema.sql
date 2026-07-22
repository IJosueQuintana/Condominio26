-- ============================================================
-- Sistema de Gestión de Condominios
-- Base de datos SQLite - Esquema completo
-- Archivo: schema.sql
-- ============================================================

PRAGMA foreign_keys = ON;


-- ============================================================
-- GRB - USUARIOS, CUENTAS, ROLES Y PERMISOS
-- ============================================================

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario              INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_documento        TEXT NOT NULL UNIQUE,
    nombres                 TEXT NOT NULL,
    apellidos               TEXT NOT NULL,
    correo                  TEXT NOT NULL UNIQUE,
    telefono                TEXT,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'INACTIVO', 'BLOQUEADO')),
    fecha_registro          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion     TEXT
);

CREATE TABLE IF NOT EXISTS cuenta (
    id_cuenta               INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario              INTEGER NOT NULL UNIQUE,
    nombre_usuario          TEXT NOT NULL UNIQUE,
    hash_contrasena         TEXT NOT NULL,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVA'
                            CHECK (estado IN ('ACTIVA', 'DESACTIVADA', 'BLOQUEADA')),
    intentos_fallidos       INTEGER NOT NULL DEFAULT 0
                            CHECK (intentos_fallidos >= 0),
    ultimo_acceso           TEXT,
    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion      TEXT,
    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rol (
    id_rol                  INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre                  TEXT NOT NULL UNIQUE,
    descripcion             TEXT,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS permiso (
    id_permiso              INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre                  TEXT NOT NULL UNIQUE,
    recurso                 TEXT NOT NULL,
    accion                  TEXT,
    descripcion             TEXT
);

CREATE TABLE IF NOT EXISTS usuario_rol (
    id_usuario              INTEGER NOT NULL,
    id_rol                  INTEGER NOT NULL,
    fecha_asignacion        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS rol_permiso (
    id_rol                  INTEGER NOT NULL,
    id_permiso              INTEGER NOT NULL,
    PRIMARY KEY (id_rol, id_permiso),
    FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_permiso)
        REFERENCES permiso(id_permiso)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS token_restablecimiento (
    id_token                INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cuenta               INTEGER NOT NULL,
    codigo                  TEXT NOT NULL UNIQUE,
    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion        TEXT NOT NULL,
    intentos                INTEGER NOT NULL DEFAULT 0
                            CHECK (intentos >= 0),
    utilizado               INTEGER NOT NULL DEFAULT 0
                            CHECK (utilizado IN (0, 1)),
    FOREIGN KEY (id_cuenta)
        REFERENCES cuenta(id_cuenta)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- ============================================================
-- GRC - CONDOMINIOS, EDIFICIOS, INMUEBLES Y ESPACIOS COMUNES
-- ============================================================

CREATE TABLE IF NOT EXISTS condominio (
    id_condominio           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre                  TEXT NOT NULL,
    ruc                     TEXT UNIQUE,
    direccion               TEXT NOT NULL,
    telefono                TEXT,
    correo                  TEXT,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    fecha_registro          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS edificio (
    id_edificio             INTEGER PRIMARY KEY AUTOINCREMENT,
    id_condominio           INTEGER NOT NULL,
    nombre                  TEXT NOT NULL,
    codigo                  TEXT NOT NULL,
    numero_pisos            INTEGER NOT NULL DEFAULT 1
                            CHECK (numero_pisos > 0),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'INACTIVO', 'MANTENIMIENTO')),
    UNIQUE (id_condominio, codigo),
    FOREIGN KEY (id_condominio)
        REFERENCES condominio(id_condominio)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS tipo_inmueble (
    id_tipo_inmueble        INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo                  TEXT NOT NULL UNIQUE,
    nombre                  TEXT NOT NULL UNIQUE,
    descripcion             TEXT,
    es_espacio_comun        INTEGER NOT NULL DEFAULT 0
                            CHECK (es_espacio_comun IN (0, 1)),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS inmueble (
    id_inmueble             INTEGER PRIMARY KEY AUTOINCREMENT,
    id_edificio             INTEGER,
    id_tipo_inmueble        INTEGER NOT NULL,
    codigo                  TEXT NOT NULL UNIQUE,
    piso                    INTEGER,
    numero                  TEXT,
    area_m2                 REAL CHECK (area_m2 IS NULL OR area_m2 > 0),
    numero_habitaciones     INTEGER CHECK (numero_habitaciones IS NULL OR numero_habitaciones >= 0),
    numero_banos            INTEGER CHECK (numero_banos IS NULL OR numero_banos >= 0),
    descripcion             TEXT,
    disponible_alquiler     INTEGER NOT NULL DEFAULT 0
                            CHECK (disponible_alquiler IN (0, 1)),
    disponible_venta        INTEGER NOT NULL DEFAULT 0
                            CHECK (disponible_venta IN (0, 1)),
    estado                  TEXT NOT NULL DEFAULT 'DISPONIBLE'
                            CHECK (estado IN ('DISPONIBLE','OCUPADO','EN_MANTENIMIENTO','EN_REMODELACION','INACTIVO')),
    fecha_registro          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_edificio)
        REFERENCES edificio(id_edificio)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (id_tipo_inmueble)
        REFERENCES tipo_inmueble(id_tipo_inmueble)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS usuario_inmueble (
    id_usuario_inmueble     INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario              INTEGER NOT NULL,
    id_inmueble             INTEGER NOT NULL,
    tipo_relacion           TEXT NOT NULL
                            CHECK (tipo_relacion IN ('PROPIETARIO','RESIDENTE','ARRENDATARIO','REPRESENTANTE')),
    fecha_inicio            TEXT NOT NULL DEFAULT CURRENT_DATE,
    fecha_fin               TEXT,
    es_principal            INTEGER NOT NULL DEFAULT 0 CHECK (es_principal IN (0, 1)),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO')),
    UNIQUE (id_usuario, id_inmueble, tipo_relacion, fecha_inicio),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS espacio_comun (
    id_espacio_comun        INTEGER PRIMARY KEY AUTOINCREMENT,
    id_inmueble             INTEGER NOT NULL UNIQUE,
    nombre                  TEXT NOT NULL,
    capacidad_maxima        INTEGER NOT NULL CHECK (capacidad_maxima > 0),
    hora_apertura           TEXT NOT NULL,
    hora_cierre             TEXT NOT NULL,
    costo_reserva_centavos  INTEGER NOT NULL DEFAULT 0 CHECK (costo_reserva_centavos >= 0),
    requiere_aprobacion     INTEGER NOT NULL DEFAULT 0 CHECK (requiere_aprobacion IN (0, 1)),
    reglamento_uso          TEXT,
    estado                  TEXT NOT NULL DEFAULT 'DISPONIBLE'
                            CHECK (estado IN ('DISPONIBLE','NO_DISPONIBLE','EN_MANTENIMIENTO','INACTIVO')),
    CHECK (hora_apertura < hora_cierre),
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parqueadero (
    id_parqueadero          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_inmueble             INTEGER NOT NULL UNIQUE,
    numero                  TEXT NOT NULL UNIQUE,
    tipo                    TEXT NOT NULL DEFAULT 'RESIDENTE'
                            CHECK (tipo IN ('RESIDENTE','VISITA','DISCAPACIDAD','SERVICIO')),
    estado                  TEXT NOT NULL DEFAULT 'DISPONIBLE'
                            CHECK (estado IN ('DISPONIBLE','OCUPADO','RESERVADO','INACTIVO')),
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS caso_fortuito (
    id_caso                 INTEGER PRIMARY KEY AUTOINCREMENT,
    id_inmueble             INTEGER NOT NULL,
    descripcion             TEXT NOT NULL,
    fecha                   TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado                  TEXT NOT NULL DEFAULT 'REGISTRADO'
                            CHECK (estado IN ('REGISTRADO','EN_REVISION','RESUELTO','CANCELADO')),
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reporte_cambio_inmueble (
    id_reporte              INTEGER PRIMARY KEY AUTOINCREMENT,
    id_inmueble             INTEGER NOT NULL,
    id_usuario_registra     INTEGER,
    fecha_generacion        TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    descripcion_cambio      TEXT NOT NULL,
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_usuario_registra) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL
);

-- ============================================================
-- GRD - RESERVAS
-- ============================================================

CREATE TABLE IF NOT EXISTS reserva (
    id_reserva              INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario              INTEGER NOT NULL,
    id_espacio_comun        INTEGER NOT NULL,
    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_reserva           TEXT NOT NULL,
    hora_inicio             TEXT NOT NULL,
    hora_fin                TEXT NOT NULL,
    costo_aplicado_centavos INTEGER NOT NULL DEFAULT 0 CHECK (costo_aplicado_centavos >= 0),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVA'
                            CHECK (estado IN ('ACTIVA','CANCELADA','FINALIZADA')),
    motivo_cancelacion      TEXT,
    fecha_cancelacion       TEXT,
    CHECK (hora_inicio < hora_fin),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_espacio_comun) REFERENCES espacio_comun(id_espacio_comun) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS observacion_reserva (
    id_observacion          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_reserva              INTEGER NOT NULL,
    id_usuario              INTEGER NOT NULL,
    texto                   TEXT NOT NULL,
    fecha_hora              TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- GRA - FINANZAS, DEUDAS, PAGOS Y GASTOS
-- ============================================================

CREATE TABLE IF NOT EXISTS tipo_deuda (
    id_tipo_deuda           INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo                  TEXT NOT NULL UNIQUE,
    nombre                  TEXT NOT NULL UNIQUE,
    descripcion             TEXT,
    genera_mora             INTEGER NOT NULL DEFAULT 0 CHECK (genera_mora IN (0, 1)),
    porcentaje_mora         REAL NOT NULL DEFAULT 0 CHECK (porcentaje_mora >= 0),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS configuracion_alicuota (
    id_configuracion        INTEGER PRIMARY KEY AUTOINCREMENT,
    id_condominio           INTEGER NOT NULL,
    valor_m2_centavos       INTEGER NOT NULL DEFAULT 0 CHECK (valor_m2_centavos >= 0),
    valor_fijo_centavos     INTEGER NOT NULL DEFAULT 0 CHECK (valor_fijo_centavos >= 0),
    fecha_inicio            TEXT NOT NULL,
    fecha_fin               TEXT,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'INACTIVA')),
    FOREIGN KEY (id_condominio) REFERENCES condominio(id_condominio) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS deuda (
    id_deuda                INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario              INTEGER NOT NULL,
    id_inmueble             INTEGER,
    id_tipo_deuda           INTEGER NOT NULL,
    id_reserva              INTEGER,
    descripcion             TEXT NOT NULL,
    valor_base_centavos     INTEGER NOT NULL CHECK (valor_base_centavos >= 0),
    mora_centavos           INTEGER NOT NULL DEFAULT 0 CHECK (mora_centavos >= 0),
    total_centavos          INTEGER NOT NULL CHECK (total_centavos >= 0),
    saldo_centavos          INTEGER NOT NULL CHECK (saldo_centavos >= 0),
    fecha_emision           TEXT NOT NULL DEFAULT CURRENT_DATE,
    fecha_vencimiento       TEXT NOT NULL,
    estado                  TEXT NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE','EN_PROCESO','PAGADA','EN_MORA','ANULADA')),
    observaciones           TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_inmueble) REFERENCES inmueble(id_inmueble) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (id_tipo_deuda) REFERENCES tipo_deuda(id_tipo_deuda) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_reserva) REFERENCES reserva(id_reserva) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS metodo_pago (
    id_metodo_pago          INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo                  TEXT NOT NULL UNIQUE,
    nombre                  TEXT NOT NULL UNIQUE,
    requiere_comprobante    INTEGER NOT NULL DEFAULT 0 CHECK (requiere_comprobante IN (0, 1)),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'INACTIVO'))
);

CREATE TABLE IF NOT EXISTS entidad_bancaria (
    id_entidad_bancaria     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre                  TEXT NOT NULL,
    numero_cuenta           TEXT NOT NULL,
    identificacion_titular  TEXT NOT NULL,
    nombre_titular          TEXT,
    tipo_cuenta             TEXT NOT NULL CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE')),
    estado                  TEXT NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'INACTIVA')),
    UNIQUE (nombre, numero_cuenta)
);

CREATE TABLE IF NOT EXISTS pago (
    id_pago                 INTEGER PRIMARY KEY AUTOINCREMENT,
    id_deuda                INTEGER NOT NULL,
    id_metodo_pago          INTEGER NOT NULL,
    id_entidad_bancaria     INTEGER,
    id_usuario_registra     INTEGER,
    valor_pagado_centavos   INTEGER NOT NULL CHECK (valor_pagado_centavos > 0),
    fecha_pago              TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    numero_transaccion      TEXT,
    comprobante             TEXT,
    estado                  TEXT NOT NULL DEFAULT 'REGISTRADO'
                            CHECK (estado IN ('REGISTRADO','VALIDADO','RECHAZADO','ANULADO')),
    observaciones           TEXT,
    FOREIGN KEY (id_deuda) REFERENCES deuda(id_deuda) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_entidad_bancaria) REFERENCES entidad_bancaria(id_entidad_bancaria) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (id_usuario_registra) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS plan_cuotas (
    id_plan_cuotas          INTEGER PRIMARY KEY AUTOINCREMENT,
    id_deuda                INTEGER NOT NULL UNIQUE,
    numero_cuotas           INTEGER NOT NULL CHECK (numero_cuotas > 0),
    valor_total_centavos    INTEGER NOT NULL CHECK (valor_total_centavos > 0),
    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO','COMPLETADO','CANCELADO')),
    FOREIGN KEY (id_deuda) REFERENCES deuda(id_deuda) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cuota (
    id_cuota                INTEGER PRIMARY KEY AUTOINCREMENT,
    id_plan_cuotas          INTEGER NOT NULL,
    numero_cuota            INTEGER NOT NULL CHECK (numero_cuota > 0),
    valor_centavos          INTEGER NOT NULL CHECK (valor_centavos > 0),
    fecha_vencimiento       TEXT NOT NULL,
    fecha_pago              TEXT,
    estado                  TEXT NOT NULL DEFAULT 'PENDIENTE'
                            CHECK (estado IN ('PENDIENTE','PAGADA','VENCIDA','ANULADA')),
    UNIQUE (id_plan_cuotas, numero_cuota),
    FOREIGN KEY (id_plan_cuotas) REFERENCES plan_cuotas(id_plan_cuotas) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pago_recurrente (
    id_pago_recurrente      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario              INTEGER NOT NULL,
    id_metodo_pago          INTEGER NOT NULL,
    id_entidad_bancaria     INTEGER,
    concepto                TEXT NOT NULL,
    monto_centavos          INTEGER NOT NULL CHECK (monto_centavos > 0),
    dia_ejecucion           INTEGER NOT NULL CHECK (dia_ejecucion BETWEEN 1 AND 28),
    fecha_inicio            TEXT NOT NULL,
    fecha_fin               TEXT,
    estado                  TEXT NOT NULL DEFAULT 'ACTIVO'
                            CHECK (estado IN ('ACTIVO','PAUSADO','FINALIZADO','CANCELADO')),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_entidad_bancaria) REFERENCES entidad_bancaria(id_entidad_bancaria) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS gasto (
    id_gasto                INTEGER PRIMARY KEY AUTOINCREMENT,
    id_condominio           INTEGER NOT NULL,
    id_usuario_registra     INTEGER NOT NULL,
    tipo_gasto              TEXT NOT NULL CHECK (tipo_gasto IN ('SERVICIO_BASICO','SUELDO','OTRO')),
    descripcion             TEXT NOT NULL,
    valor_centavos          INTEGER NOT NULL CHECK (valor_centavos > 0),
    fecha_gasto             TEXT NOT NULL DEFAULT CURRENT_DATE,
    comprobante             TEXT,
    detalle_adicional       TEXT,
    estado                  TEXT NOT NULL DEFAULT 'REGISTRADO'
                            CHECK (estado IN ('REGISTRADO','APROBADO','ANULADO')),
    FOREIGN KEY (id_condominio) REFERENCES condominio(id_condominio) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY (id_usuario_registra) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- GRE - VISITAS, CHECK-IN Y SEGURIDAD
-- ============================================================

CREATE TABLE IF NOT EXISTS visitas_programadas (
   id_visita INTEGER PRIMARY KEY AUTOINCREMENT,
   id_residente INTEGER NOT NULL,
   -- Datos del visitante "aplanados" en la visita
   nombres_visita TEXT NOT NULL,
   apellidos_visita TEXT NOT NULL,
   cedula_visita TEXT NOT NULL,
   telefono_visita TEXT NOT NULL,
   -- Datos propios de la planificación
   fecha_programada TEXT NOT NULL, -- Formato: 'YYYY-MM-DD'
   hora_programada TEXT NOT NULL, -- Formato: 'HH:MM:SS'
   placa_vehiculo TEXT NOT NULL DEFAULT 'N/A',
   estado TEXT NOT NULL DEFAULT 'PROGRAMADA',
   motivo_visita TEXT NOT NULL,
   
   -- Restricciones para simular los Enums del diagrama
   CONSTRAINT chk_estado_visita CHECK (estado IN ('PROGRAMADA', 'REALIZADA', 'CANCELADA'))
);

CREATE TABLE IF NOT EXISTS registro_entrada (
   id_entrada INTEGER PRIMARY KEY AUTOINCREMENT,
   -- Relación opcional con la visita programada
   id_visita INTEGER NULL,
   -- Datos de la persona que ingresa (sea residente, visitante o externo)
   nombres TEXT NOT NULL,
   apellidos TEXT NOT NULL,
   cedula TEXT NOT NULL,
   -- Atributos generales del Check-In
   fecha_llegada TEXT NOT NULL, -- Formato: 'YYYY-MM-DD'
   hora_llegada TEXT NOT NULL,  -- Formato: 'HH:MM:SS'
   informacion_adicional TEXT,  -- informacion importante sobre la visita
   observaciones TEXT,          -- incidencias, novedades sobre la visita
   tipo_entrada TEXT NOT NULL,
   placa_vehiculo TEXT,
   
   -- Llave foránea hacia visitas programadas
   FOREIGN KEY (id_visita) REFERENCES visitas_programadas(id_visita) ON DELETE SET NULL,
   -- Restricciones de integridad y formatos
   CONSTRAINT chk_tipo_entrada CHECK (tipo_entrada IN ('RESIDENTE', 'VISITANTE', 'EXTERNA'))
);

CREATE TABLE IF NOT EXISTS vehiculo_visita (
    id_vehiculo_visita      INTEGER PRIMARY KEY AUTOINCREMENT,
    id_visita               INTEGER NOT NULL,
    placa                   TEXT NOT NULL,
    marca                   TEXT,
    modelo                  TEXT,
    color                   TEXT,
    FOREIGN KEY (id_visita) REFERENCES visitas_programadas(id_visita) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ingreso_parqueadero (
    id_ingreso_parqueadero  INTEGER PRIMARY KEY AUTOINCREMENT,
    id_registro_entrada     INTEGER NOT NULL,
    id_parqueadero          INTEGER NOT NULL,
    fecha_hora_ingreso      TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_hora_salida       TEXT,
    estado                  TEXT NOT NULL DEFAULT 'OCUPADO'
                            CHECK (estado IN ('OCUPADO','LIBERADO','CANCELADO')),
    FOREIGN KEY (id_registro_entrada) REFERENCES registro_entrada(id_entrada) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id_parqueadero) REFERENCES parqueadero(id_parqueadero) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS alerta_seguridad (
    id_alerta               INTEGER PRIMARY KEY AUTOINCREMENT,
    id_registro_entrada     INTEGER,
    id_usuario_reporta      INTEGER NOT NULL,
    tipo                    TEXT NOT NULL,
    descripcion             TEXT NOT NULL,
    nivel                   TEXT NOT NULL DEFAULT 'MEDIA' CHECK (nivel IN ('BAJA','MEDIA','ALTA','CRITICA')),
    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado                  TEXT NOT NULL DEFAULT 'ABIERTA'
                            CHECK (estado IN ('ABIERTA','EN_ATENCION','RESUELTA','CERRADA')),
    FOREIGN KEY (id_registro_entrada) REFERENCES registro_entrada(id_entrada) ON UPDATE CASCADE ON DELETE SET NULL,
    FOREIGN KEY (id_usuario_reporta) REFERENCES usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

-- ============================================================
-- GRF - COMUNICACIONES Y NOTIFICACIONES
-- ============================================================
CREATE TABLE IF NOT EXISTS mensaje (
                                       id_mensaje              INTEGER PRIMARY KEY AUTOINCREMENT,
                                       id_emisor               INTEGER NOT NULL,

                                       asunto                  TEXT NOT NULL,
                                       contenido               TEXT NOT NULL,

                                       tipo                    TEXT NOT NULL DEFAULT 'MENSAJE_GLOBAL'
                                       CHECK (
                                       tipo IN (
                                       'MENSAJE_RESIDENTES',
                                       'COMUNICADO_TRABAJADORES',
                                       'MENSAJE_GLOBAL',
                                       'MENSAJE_URGENTE',
                                       'ALERTA_EMERGENCIA',
                                       'BOLETIN_INFORMATIVO'
)
    ),

    prioridad               TEXT NOT NULL DEFAULT 'NORMAL'
    CHECK (
              prioridad IN (
              'BAJA',
              'NORMAL',
              'ALTA',
              'URGENTE'
                           )
    ),

    fecha_creacion          TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_envio             TEXT,

    estado                  TEXT NOT NULL DEFAULT 'BORRADOR'
    CHECK (
              estado IN (
              'BORRADOR',
              'ENVIADO',
              'CANCELADO'
                        )
    ),

    FOREIGN KEY (id_emisor)
    REFERENCES usuario(id_usuario)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
    );

-- ============================================================
-- ÍNDICES
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_mensaje_tipo
    ON mensaje(tipo);

CREATE INDEX IF NOT EXISTS idx_mensaje_estado
    ON mensaje(estado);

CREATE INDEX IF NOT EXISTS idx_mensaje_fecha_envio
    ON mensaje(fecha_envio);
CREATE INDEX IF NOT EXISTS idx_mensaje_destinatario_usuario
    ON mensaje_destinatario(id_usuario);
CREATE INDEX IF NOT EXISTS idx_usuario_estado ON usuario(estado);
CREATE INDEX IF NOT EXISTS idx_usuario_rol_usuario ON usuario_rol(id_usuario);
CREATE INDEX IF NOT EXISTS idx_usuario_rol_rol ON usuario_rol(id_rol);
CREATE INDEX IF NOT EXISTS idx_inmueble_edificio ON inmueble(id_edificio);
CREATE INDEX IF NOT EXISTS idx_inmueble_tipo ON inmueble(id_tipo_inmueble);
CREATE INDEX IF NOT EXISTS idx_usuario_inmueble_usuario ON usuario_inmueble(id_usuario);
CREATE INDEX IF NOT EXISTS idx_usuario_inmueble_inmueble ON usuario_inmueble(id_inmueble);
CREATE INDEX IF NOT EXISTS idx_reserva_usuario ON reserva(id_usuario);
CREATE INDEX IF NOT EXISTS idx_reserva_espacio_fecha ON reserva(id_espacio_comun, fecha_reserva);
CREATE INDEX IF NOT EXISTS idx_reserva_estado ON reserva(estado);
CREATE INDEX IF NOT EXISTS idx_deuda_usuario ON deuda(id_usuario);
CREATE INDEX IF NOT EXISTS idx_deuda_estado ON deuda(estado);
CREATE INDEX IF NOT EXISTS idx_deuda_vencimiento ON deuda(fecha_vencimiento);
CREATE INDEX IF NOT EXISTS idx_pago_deuda ON pago(id_deuda);
CREATE INDEX IF NOT EXISTS idx_visita_residente ON visitas_programadas(id_residente);
CREATE INDEX IF NOT EXISTS idx_visita_fecha ON visitas_programadas(fecha_programada);
CREATE INDEX IF NOT EXISTS idx_registro_entrada_visita ON registro_entrada(id_visita);
CREATE INDEX IF NOT EXISTS idx_registro_entrada_fecha ON registro_entrada(fecha_llegada);
CREATE INDEX IF NOT EXISTS idx_mensaje_emisor ON mensaje(id_emisor);
CREATE INDEX IF NOT EXISTS idx_notificacion_usuario_leida ON notificacion(id_usuario, leida);

-- ============================================================
-- TRIGGERS BÁSICOS
-- ============================================================

CREATE TRIGGER IF NOT EXISTS trg_usuario_fecha_actualizacion
AFTER UPDATE ON usuario
FOR EACH ROW
WHEN NEW.fecha_actualizacion IS OLD.fecha_actualizacion
BEGIN
    UPDATE usuario
    SET fecha_actualizacion = CURRENT_TIMESTAMP
    WHERE id_usuario = NEW.id_usuario;
END;

CREATE TRIGGER IF NOT EXISTS trg_cuenta_fecha_modificacion
AFTER UPDATE ON cuenta
FOR EACH ROW
WHEN NEW.fecha_modificacion IS OLD.fecha_modificacion
BEGIN
    UPDATE cuenta
    SET fecha_modificacion = CURRENT_TIMESTAMP
    WHERE id_cuenta = NEW.id_cuenta;
END;

CREATE TRIGGER IF NOT EXISTS trg_reserva_evitar_solapamiento_insert
BEFORE INSERT ON reserva
FOR EACH ROW
WHEN NEW.estado = 'ACTIVA'
BEGIN
    SELECT CASE
        WHEN EXISTS (
            SELECT 1
            FROM reserva r
            WHERE r.id_espacio_comun = NEW.id_espacio_comun
              AND r.fecha_reserva = NEW.fecha_reserva
              AND r.estado = 'ACTIVA'
              AND NEW.hora_inicio < r.hora_fin
              AND NEW.hora_fin > r.hora_inicio
        )
        THEN RAISE(ABORT, 'Existe una reserva activa que se superpone en el mismo espacio y horario')
    END;
END;

CREATE TRIGGER IF NOT EXISTS trg_reserva_evitar_solapamiento_update
BEFORE UPDATE OF id_espacio_comun, fecha_reserva, hora_inicio, hora_fin, estado
ON reserva
FOR EACH ROW
WHEN NEW.estado = 'ACTIVA'
BEGIN
    SELECT CASE
        WHEN EXISTS (
            SELECT 1
            FROM reserva r
            WHERE r.id_reserva <> NEW.id_reserva
              AND r.id_espacio_comun = NEW.id_espacio_comun
              AND r.fecha_reserva = NEW.fecha_reserva
              AND r.estado = 'ACTIVA'
              AND NEW.hora_inicio < r.hora_fin
              AND NEW.hora_fin > r.hora_inicio
        )
        THEN RAISE(ABORT, 'Existe una reserva activa que se superpone en el mismo espacio y horario')
    END;
END;

CREATE TRIGGER IF NOT EXISTS trg_pago_no_superar_saldo
BEFORE INSERT ON pago
FOR EACH ROW
WHEN NEW.estado IN ('REGISTRADO', 'VALIDADO')
BEGIN
    SELECT CASE
        WHEN NEW.valor_pagado_centavos >
             (SELECT saldo_centavos FROM deuda WHERE id_deuda = NEW.id_deuda)
        THEN RAISE(ABORT, 'El pago no puede superar el saldo pendiente de la deuda')
    END;
END;

CREATE TRIGGER IF NOT EXISTS trg_pago_actualizar_deuda
AFTER INSERT ON pago
FOR EACH ROW
WHEN NEW.estado IN ('REGISTRADO', 'VALIDADO')
BEGIN
    UPDATE deuda
    SET saldo_centavos = MAX(0, saldo_centavos - NEW.valor_pagado_centavos),
        estado = CASE
                    WHEN saldo_centavos - NEW.valor_pagado_centavos <= 0 THEN 'PAGADA'
                    ELSE 'EN_PROCESO'
                 END
    WHERE id_deuda = NEW.id_deuda;
END;

CREATE TRIGGER IF NOT EXISTS trg_registro_salida_actualizar_estado
AFTER UPDATE OF fecha_salida, hora_salida ON registro_entrada
FOR EACH ROW
WHEN NEW.fecha_salida IS NOT NULL
 AND NEW.hora_salida IS NOT NULL
 AND NEW.estado <> 'SALIO'
BEGIN
    UPDATE registro_entrada
    SET estado = 'SALIO'
    WHERE id_registro_entrada = NEW.id_registro_entrada;
END;

