-- ============================================================
-- Sistema de Gestión de Condominios
-- Datos de prueba completos para todos los módulos
-- Archivo: seed.sql
-- Compatible con schema.sql
-- ============================================================

PRAGMA foreign_keys = ON;

-- ============================================================
-- GRB - ROLES, PERMISOS, USUARIOS Y CUENTAS
-- ============================================================

INSERT OR IGNORE INTO rol (id_rol, nombre, descripcion, estado) VALUES
(1, 'ADMINISTRADOR', 'Administrador general del sistema', 'ACTIVO'),
(2, 'RESIDENTE', 'Residente del condominio', 'ACTIVO'),
(3, 'PROPIETARIO', 'Propietario de uno o más inmuebles', 'ACTIVO'),
(4, 'PERSONAL_SEGURIDAD', 'Personal encargado del control de accesos', 'ACTIVO'),
(5, 'PRESIDENTE', 'Presidente o representante del condominio', 'ACTIVO');

INSERT OR IGNORE INTO permiso (id_permiso, nombre, recurso, accion, descripcion) VALUES
(1, 'USUARIOS_VER', 'USUARIOS', 'VER', 'Consultar usuarios'),
(2, 'USUARIOS_GESTIONAR', 'USUARIOS', 'GESTIONAR', 'Crear, modificar y desactivar usuarios'),
(3, 'INMUEBLES_VER', 'INMUEBLES', 'VER', 'Consultar inmuebles y espacios comunes'),
(4, 'INMUEBLES_GESTIONAR', 'INMUEBLES', 'GESTIONAR', 'Administrar inmuebles y espacios comunes'),
(5, 'RESERVAS_CREAR', 'RESERVAS', 'CREAR', 'Crear reservas'),
(6, 'RESERVAS_AUDITAR', 'RESERVAS', 'AUDITAR', 'Auditar todas las reservas'),
(7, 'FINANZAS_VER', 'FINANZAS', 'VER', 'Consultar deudas y pagos'),
(8, 'FINANZAS_GESTIONAR', 'FINANZAS', 'GESTIONAR', 'Administrar deudas, pagos y gastos'),
(9, 'CHECKIN_REGISTRAR', 'CHECKIN', 'REGISTRAR', 'Registrar ingresos y salidas'),
(10, 'COMUNICACION_PUBLICAR', 'COMUNICACION', 'PUBLICAR', 'Publicar mensajes, anuncios y alertas');

INSERT OR IGNORE INTO rol_permiso (id_rol, id_permiso) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),
(2,3),(2,5),(2,7),
(3,3),(3,5),(3,7),
(4,3),(4,9),
(5,1),(5,3),(5,6),(5,7),(5,8),(5,10);

-- Permisos de Finanzas por botón/vista (matriz enviada por el módulo de Finanzas)
INSERT OR IGNORE INTO permiso (nombre, recurso, accion, descripcion) VALUES
('FINANZAS_VER_DEUDAS', 'FINANZAS', 'VER', 'Ver deudas'),
('FINANZAS_VER_PAGOS', 'FINANZAS', 'VER', 'Ver pagos'),
('FINANZAS_VER_RENDICION_CUENTAS', 'FINANZAS', 'VER', 'Ver rendición de cuentas'),
('FINANZAS_BTN_REGISTRAR_DEUDA', 'FINANZAS', 'BTN', 'Botón registrar deuda'),
('FINANZAS_BTN_ELIMINAR_DEUDA', 'FINANZAS', 'BTN', 'Botón eliminar deuda'),
('FINANZAS_BTN_MODIFICAR_FECHA_DEUDA', 'FINANZAS', 'BTN', 'Botón modificar fecha de deuda'),
('FINANZAS_BTN_REGISTRAR_PAGO', 'FINANZAS', 'BTN', 'Botón registrar pago'),
('FINANZAS_BTN_DEFINIR_ALICUOTA', 'FINANZAS', 'BTN', 'Botón definir alícuota'),
('FINANZAS_BTN_REGISTRAR_BANCO', 'FINANZAS', 'BTN', 'Botón registrar entidad bancaria'),
('FINANZAS_BTN_REGISTRAR_GASTO', 'FINANZAS', 'BTN', 'Botón registrar gasto'),
('FINANZAS_BTN_GENERAR_REPORTE_PAGOS', 'FINANZAS', 'BTN', 'Botón generar reporte de pagos'),
('FINANZAS_BTN_GENERAR_REPORTE_GASTOS', 'FINANZAS', 'BTN', 'Botón generar reporte de gastos'),
('FINANZAS_BTN_GENERAR_RENDICION_CUENTAS', 'FINANZAS', 'BTN', 'Botón generar rendición de cuentas'),
('FINANZAS_BTN_PAGAR_DEUDA', 'FINANZAS', 'BTN', 'Botón pagar deuda'),
('FINANZAS_BTN_SOLICITAR_CUOTAS', 'FINANZAS', 'BTN', 'Botón solicitar cuotas'),
('FINANZAS_BTN_GENERAR_CERTIFICADO', 'FINANZAS', 'BTN', 'Botón generar certificado de no deudor');

-- ADMINISTRADOR: acceso total, todos los permisos de Finanzas
INSERT OR IGNORE INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'ADMINISTRADOR'), id_permiso
FROM permiso WHERE nombre IN (
    'FINANZAS_VER_DEUDAS','FINANZAS_VER_PAGOS','FINANZAS_VER_RENDICION_CUENTAS',
    'FINANZAS_BTN_REGISTRAR_DEUDA','FINANZAS_BTN_ELIMINAR_DEUDA','FINANZAS_BTN_MODIFICAR_FECHA_DEUDA',
    'FINANZAS_BTN_REGISTRAR_PAGO','FINANZAS_BTN_DEFINIR_ALICUOTA','FINANZAS_BTN_REGISTRAR_BANCO',
    'FINANZAS_BTN_REGISTRAR_GASTO','FINANZAS_BTN_GENERAR_REPORTE_PAGOS','FINANZAS_BTN_GENERAR_REPORTE_GASTOS',
    'FINANZAS_BTN_GENERAR_RENDICION_CUENTAS','FINANZAS_BTN_PAGAR_DEUDA','FINANZAS_BTN_SOLICITAR_CUOTAS',
    'FINANZAS_BTN_GENERAR_CERTIFICADO'
);

-- PRESIDENTE: gestiona finanzas generales, pagos externos y reportes
INSERT OR IGNORE INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'PRESIDENTE'), id_permiso
FROM permiso WHERE nombre IN (
    'FINANZAS_VER_DEUDAS','FINANZAS_VER_PAGOS','FINANZAS_VER_RENDICION_CUENTAS',
    'FINANZAS_BTN_REGISTRAR_DEUDA','FINANZAS_BTN_ELIMINAR_DEUDA','FINANZAS_BTN_MODIFICAR_FECHA_DEUDA',
    'FINANZAS_BTN_REGISTRAR_PAGO','FINANZAS_BTN_DEFINIR_ALICUOTA','FINANZAS_BTN_REGISTRAR_BANCO',
    'FINANZAS_BTN_REGISTRAR_GASTO','FINANZAS_BTN_GENERAR_REPORTE_PAGOS','FINANZAS_BTN_GENERAR_REPORTE_GASTOS',
    'FINANZAS_BTN_GENERAR_RENDICION_CUENTAS'
);

-- RESIDENTE: solo sus propias deudas y consultas
INSERT OR IGNORE INTO rol_permiso (id_rol, id_permiso)
SELECT (SELECT id_rol FROM rol WHERE nombre = 'RESIDENTE'), id_permiso
FROM permiso WHERE nombre IN (
    'FINANZAS_VER_DEUDAS','FINANZAS_VER_PAGOS','FINANZAS_VER_RENDICION_CUENTAS',
    'FINANZAS_BTN_PAGAR_DEUDA','FINANZAS_BTN_SOLICITAR_CUOTAS','FINANZAS_BTN_GENERAR_CERTIFICADO'
);

INSERT OR IGNORE INTO usuario
(id_usuario, numero_documento, nombres, apellidos, correo, telefono, estado, fecha_registro)
VALUES
(1, '1710000001', 'Andrea', 'Administradora', 'admin@condominio.local', '0991000001', 'ACTIVO', CURRENT_TIMESTAMP),
(2, '1710000002', 'Carlos', 'Residente', 'carlos.residente@condominio.local', '0991000002', 'ACTIVO', CURRENT_TIMESTAMP),
(3, '1710000003', 'María', 'Propietaria', 'maria.propietaria@condominio.local', '0991000003', 'ACTIVO', CURRENT_TIMESTAMP),
(4, '1710000004', 'Luis', 'Guardia', 'luis.guardia@condominio.local', '0991000004', 'ACTIVO', CURRENT_TIMESTAMP),
(5, '1710000005', 'Patricia', 'Presidenta', 'patricia.presidenta@condominio.local', '0991000005', 'ACTIVO', CURRENT_TIMESTAMP),
(6, '1710000006', 'Jorge', 'Residente', 'jorge.residente@condominio.local', '0991000006', 'ACTIVO', CURRENT_TIMESTAMP),
(7, '1710000007', 'Sofía', 'Residente', 'sofia.residente@condominio.local', '0991000007', 'ACTIVO', CURRENT_TIMESTAMP);

INSERT OR IGNORE INTO cuenta
(id_cuenta, id_usuario, nombre_usuario, hash_contrasena, estado, intentos_fallidos, fecha_creacion)
VALUES
(1, 1, 'admin', '$2a$10$0gVnFsGi7I7bwYolUNx87ucpC2rTyGkdecy2v2owmeHn0vwDHgDD.', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(2, 2, 'carlos', '$2a$10$uFfz111cpUIFWm8aw6oth.njWnXTgTA6vWb0ZuNBmTvECwA0GCymK', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(3, 3, 'maria', '$2a$10$DND7xAw/rvaYoG..Pl0FpOqKoV0pLJhwLxPZuL/xsXoSvLmsYHBMi', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(4, 4, 'guardia', '$2a$10$2vJ1zJ62esys9NVk3Jdgs.ZfRrcTqgL2Cg4ljZGEDBDg5nvh.nI8q', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(5, 5, 'presidenta', '$2a$10$T73cIdHQ.xD4WVHZFXvnzuEr4Hhw5mZdQ8mhycJgkVCYYi78TH0NS', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(6, 6, 'jorge', '$2a$10$FI6VITZKb8LojQt9LKXoDOz7TLmZKVhR2c3fbFMy17L4dgGUluyO2', 'ACTIVA', 0, CURRENT_TIMESTAMP),
(7, 7, 'sofia', '$2a$10$PVaRJ7EOhAtARYumAM2n2uyC7nr7gyn1ZRVowL5G4uf6I.T5bjlqS', 'ACTIVA', 0, CURRENT_TIMESTAMP);

INSERT OR IGNORE INTO usuario_rol (id_usuario, id_rol) VALUES
(1,1),
(2,2),
(3,2),(3,3),
(4,4),
(5,5),(5,3),
(6,2),
(7,2);

-- ============================================================
-- GRC - CONDOMINIO, EDIFICIOS E INMUEBLES
-- ============================================================

INSERT OR IGNORE INTO condominio
(id_condominio, nombre, ruc, direccion, telefono, correo, estado)
VALUES
(1, 'Condominio Jardines del Norte', '1799999999001',
 'Av. Principal N35-120 y Los Pinos, Quito',
 '022555555', 'administracion@jardines.local', 'ACTIVO');

INSERT OR IGNORE INTO edificio
(id_edificio, id_condominio, nombre, codigo, numero_pisos, estado)
VALUES
(1, 1, 'Torre A', 'TA', 8, 'ACTIVO'),
(2, 1, 'Torre B', 'TB', 8, 'ACTIVO');

INSERT OR IGNORE INTO tipo_inmueble
(id_tipo_inmueble, codigo, nombre, descripcion, es_espacio_comun, estado)
VALUES
(1, 'DEPARTAMENTO', 'Departamento', 'Unidad habitacional familiar', 0, 'ACTIVO'),
(2, 'SUITE', 'Suite', 'Unidad habitacional tipo suite', 0, 'ACTIVO'),
(3, 'PARQUEADERO', 'Parqueadero', 'Espacio para estacionamiento', 0, 'ACTIVO'),
(4, 'SALON_EVENTOS', 'Salón de Eventos', 'Espacio común para eventos', 1, 'ACTIVO'),
(5, 'PISCINA', 'Piscina', 'Piscina comunal', 1, 'ACTIVO'),
(6, 'AREA_BBQ', 'Área BBQ', 'Zona comunal para parrilladas', 1, 'ACTIVO'),
(7, 'AREA_RECREACIONAL', 'Área Recreacional', 'Cancha y zona recreativa', 1, 'ACTIVO');

INSERT OR IGNORE INTO inmueble
(id_inmueble, id_edificio, id_tipo_inmueble, codigo, piso, numero, area_m2,
 numero_habitaciones, numero_banos, descripcion, disponible_alquiler,
 disponible_venta, estado)
VALUES
(1, 1, 1, 'TA-101', 1, '101', 92.50, 3, 2, 'Departamento Torre A 101', 0, 0, 'OCUPADO'),
(2, 1, 1, 'TA-202', 2, '202', 88.00, 3, 2, 'Departamento Torre A 202', 1, 0, 'OCUPADO'),
(3, 2, 2, 'TB-305', 3, '305', 55.00, 1, 1, 'Suite Torre B 305', 0, 1, 'OCUPADO'),
(4, 1, 3, 'PA-01', NULL, 'P01', 12.50, NULL, NULL, 'Parqueadero propietario 1', 0, 0, 'DISPONIBLE'),
(5, 1, 3, 'PV-01', NULL, 'V01', 12.50, NULL, NULL, 'Parqueadero visitas 1', 0, 0, 'DISPONIBLE'),
(6, 1, 4, 'EC-SALON', 1, 'S01', 150.00, NULL, 2, 'Salón de eventos comunal', 0, 0, 'DISPONIBLE'),
(7, 1, 5, 'EC-PISCINA', 1, 'PISCINA', 200.00, NULL, 2, 'Piscina comunal', 0, 0, 'DISPONIBLE'),
(8, 1, 6, 'EC-BBQ', 1, 'BBQ', 80.00, NULL, 1, 'Área BBQ comunal', 0, 0, 'DISPONIBLE'),
(9, 2, 7, 'EC-RECREA', 1, 'CANCHA', 250.00, NULL, 1, 'Área recreacional y cancha', 0, 0, 'DISPONIBLE');

INSERT OR IGNORE INTO usuario_inmueble
(id_usuario_inmueble, id_usuario, id_inmueble, tipo_relacion, fecha_inicio, es_principal, estado)
VALUES
(1, 2, 1, 'RESIDENTE', '2026-01-01', 1, 'ACTIVO'),
(2, 3, 1, 'PROPIETARIO', '2025-01-01', 1, 'ACTIVO'),
(3, 6, 2, 'RESIDENTE', '2026-02-01', 1, 'ACTIVO'),
(4, 5, 2, 'PROPIETARIO', '2024-01-01', 1, 'ACTIVO'),
(5, 7, 3, 'RESIDENTE', '2026-03-01', 1, 'ACTIVO'),
(6, 3, 3, 'PROPIETARIO', '2025-06-01', 1, 'ACTIVO');

INSERT OR IGNORE INTO espacio_comun
(id_espacio_comun, id_inmueble, nombre, capacidad_maxima, hora_apertura,
 hora_cierre, costo_reserva_centavos, requiere_aprobacion, reglamento_uso, estado)
VALUES
(1, 6, 'Salón de Eventos', 80, '08:00', '23:00', 5000, 1,
 'Máximo 80 personas. Entregar limpio y respetar horarios.', 'DISPONIBLE'),
(2, 7, 'Piscina', 40, '07:00', '20:00', 0, 0,
 'Uso obligatorio de traje de baño. Menores acompañados.', 'DISPONIBLE'),
(3, 8, 'Área BBQ', 25, '09:00', '22:00', 2500, 0,
 'Limpiar parrilla y retirar residuos al finalizar.', 'DISPONIBLE'),
(4, 9, 'Área Recreacional', 50, '06:00', '21:00', 0, 0,
 'Respetar turnos y cuidar el equipamiento.', 'DISPONIBLE');

INSERT OR IGNORE INTO parqueadero
(id_parqueadero, id_inmueble, numero, tipo, estado)
VALUES
(1, 4, 'P01', 'RESIDENTE', 'DISPONIBLE'),
(2, 5, 'V01', 'VISITA', 'DISPONIBLE');

INSERT OR IGNORE INTO caso_fortuito
(id_caso, id_inmueble, descripcion, fecha, estado)
VALUES
(1, 7, 'Mantenimiento preventivo del sistema de filtrado de la piscina',
 '2026-07-10 08:00:00', 'RESUELTO');

INSERT OR IGNORE INTO reporte_cambio_inmueble
(id_reporte, id_inmueble, id_usuario_registra, fecha_generacion, descripcion_cambio)
VALUES
(1, 8, 1, '2026-07-12 10:30:00', 'Se actualizó el reglamento de uso del Área BBQ');

-- ============================================================
-- GRD - RESERVAS
-- ============================================================

INSERT OR IGNORE INTO reserva
(id_reserva, id_usuario, id_espacio_comun, fecha_creacion, fecha_reserva,
 hora_inicio, hora_fin, costo_aplicado_centavos, estado)
VALUES
(1, 2, 1, '2026-07-15 09:00:00', '2026-07-25', '18:00', '21:00', 5000, 'ACTIVA'),
(2, 6, 3, '2026-07-05 10:00:00', '2026-07-12', '12:00', '16:00', 2500, 'FINALIZADA'),
(3, 7, 2, '2026-07-01 08:00:00', '2026-07-08', '10:00', '12:00', 0, 'CANCELADA'),
(4, 3, 4, '2026-07-16 15:00:00', '2026-07-27', '09:00', '11:00', 0, 'ACTIVA');

INSERT OR IGNORE INTO observacion_reserva
(id_observacion, id_reserva, id_usuario, texto, fecha_hora)
VALUES
(1, 1, 1, 'Reserva pendiente de revisión final del administrador.', '2026-07-15 09:30:00'),
(2, 2, 6, 'El espacio fue entregado limpio y sin novedades.', '2026-07-12 16:30:00'),
(3, 3, 7, 'Cancelada por motivos personales.', '2026-07-03 11:00:00');

-- ============================================================
-- GRA - FINANZAS
-- ============================================================

INSERT OR IGNORE INTO tipo_deuda
(id_tipo_deuda, codigo, nombre, descripcion, genera_mora, porcentaje_mora, estado)
VALUES
(1, 'ALICUOTA', 'Alícuota', 'Pago mensual de alícuota', 1, 5.0, 'ACTIVO'),
(2, 'RESERVA', 'Reserva', 'Deuda generada por reserva de espacio común', 0, 0.0, 'ACTIVO'),
(3, 'MULTA', 'Multa', 'Multa por incumplimiento del reglamento', 1, 10.0, 'ACTIVO');

INSERT OR IGNORE INTO configuracion_alicuota
(id_configuracion, id_condominio, valor_m2_centavos, valor_fijo_centavos,
 fecha_inicio, estado)
VALUES
(1, 1, 35, 1500, '2026-01-01', 'ACTIVA');

INSERT OR IGNORE INTO metodo_pago
(id_metodo_pago, codigo, nombre, requiere_comprobante, estado)
VALUES
(1, 'EFECTIVO', 'Efectivo', 0, 'ACTIVO'),
(2, 'TRANSFERENCIA', 'Transferencia bancaria', 1, 'ACTIVO'),
(3, 'TARJETA', 'Tarjeta', 1, 'ACTIVO');

INSERT OR IGNORE INTO entidad_bancaria
(id_entidad_bancaria, nombre, numero_cuenta, identificacion_titular,
 nombre_titular, tipo_cuenta, estado)
VALUES
(1, 'Banco Pichincha', '2200123456', '1799999999001',
 'Condominio Jardines del Norte', 'CORRIENTE', 'ACTIVA'),
(2, 'Banco del Pacífico', '1012345678', '1710000002',
 'Carlos Residente', 'AHORROS', 'ACTIVA');

INSERT OR IGNORE INTO deuda
(id_deuda, id_usuario, id_inmueble, id_tipo_deuda, id_reserva, descripcion,
 valor_base_centavos, mora_centavos, total_centavos, saldo_centavos,
 fecha_emision, fecha_vencimiento, estado, observaciones)
VALUES
(1, 2, 1, 1, NULL, 'Alícuota julio 2026',
 5000, 0, 5000, 5000, '2026-07-01', '2026-07-15', 'PENDIENTE', NULL),
(2, 2, 1, 2, 1, 'Reserva del Salón de Eventos',
 5000, 0, 5000, 5000, '2026-07-15', '2026-07-24', 'PENDIENTE', NULL),
(3, 6, 2, 3, NULL, 'Multa por ruido fuera de horario',
 3000, 300, 3300, 3300, '2026-06-20', '2026-07-05', 'EN_MORA',
 'Notificación entregada al residente'),
(4, 7, 3, 1, NULL, 'Alícuota junio 2026',
 4200, 0, 4200, 4200, '2026-06-01', '2026-06-15', 'PENDIENTE', NULL),
(5, 3, 3, 1, NULL, 'Alícuota mayo 2026',
 4000, 0, 4000, 4000, '2026-05-01', '2026-05-15', 'PENDIENTE', NULL);

-- Los triggers actualizarán automáticamente saldos y estados.
INSERT OR IGNORE INTO pago
(id_pago, id_deuda, id_metodo_pago, id_entidad_bancaria, id_usuario_registra,
 valor_pagado_centavos, fecha_pago, numero_transaccion, comprobante, estado, observaciones)
VALUES
(1, 4, 2, 1, 1, 2000, '2026-07-10 11:00:00',
 'TRX-20260710-001', 'comprobantes/pago_001.pdf', 'VALIDADO', 'Pago parcial'),
(2, 5, 1, NULL, 1, 4000, '2026-05-12 09:30:00',
 NULL, NULL, 'VALIDADO', 'Pago completo en administración');

INSERT OR IGNORE INTO plan_cuotas
(id_plan_cuotas, id_deuda, numero_cuotas, valor_total_centavos,
 fecha_creacion, estado)
VALUES
(1, 3, 3, 3300, '2026-07-01 12:00:00', 'ACTIVO');

INSERT OR IGNORE INTO cuota
(id_cuota, id_plan_cuotas, numero_cuota, valor_centavos,
 fecha_vencimiento, estado)
VALUES
(1, 1, 1, 1100, '2026-07-15', 'PENDIENTE'),
(2, 1, 2, 1100, '2026-08-15', 'PENDIENTE'),
(3, 1, 3, 1100, '2026-09-15', 'PENDIENTE');

INSERT OR IGNORE INTO pago_recurrente
(id_pago_recurrente, id_usuario, id_metodo_pago, id_entidad_bancaria,
 concepto, monto_centavos, dia_ejecucion, fecha_inicio, estado)
VALUES
(1, 2, 2, 2, 'Pago mensual de alícuota', 5000, 10, '2026-08-01', 'ACTIVO');

INSERT OR IGNORE INTO gasto
(id_gasto, id_condominio, id_usuario_registra, tipo_gasto, descripcion,
 valor_centavos, fecha_gasto, comprobante, detalle_adicional, estado)
VALUES
(1, 1, 5, 'SERVICIO_BASICO', 'Pago mensual de energía eléctrica',
 18500, '2026-07-05', 'comprobantes/luz_julio.pdf', 'Áreas comunales', 'APROBADO'),
(2, 1, 5, 'SUELDO', 'Pago mensual personal de seguridad',
 65000, '2026-07-01', NULL, 'Nómina julio 2026', 'APROBADO'),
(3, 1, 1, 'OTRO', 'Compra de implementos de limpieza',
 7800, '2026-07-08', 'comprobantes/limpieza.pdf', NULL, 'REGISTRADO');

-- ============================================================
-- GRE - VISITAS Y CHECK-IN
-- ============================================================

INSERT OR IGNORE INTO visitas_programadas
(id_visita, id_residente, nombres_visita, apellidos_visita, cedula_visita,
 telefono_visita, fecha_programada, hora_programada, placa_vehiculo, estado, motivo_visita)
VALUES
(1, 7, 'María Fernanda', 'Paredes López', '1723456789', '0998456123', '2026-07-22', '10:30:00', 'PCD-4821', 'PROGRAMADA', 'Reunión familiar'),
(2, 2, 'Carlos Andrés', 'Mendoza Ruiz', '0918765432', '0987123456', '2026-07-22', '15:00:00', 'PBC-2145', 'PROGRAMADA', 'Entrega de documentos'),
(3, 3, 'Daniela Sofía', 'Villacrés Mora', '1109876543', '0968453210', '2026-07-20', '09:15:00', 'N/A', 'REALIZADA', 'Visita personal'),
(4, 6, 'Jorge Luis', 'Salazar Ortiz', '1712345678', '0976321458', '2026-07-19', '18:45:00', 'PBL-7710', 'CANCELADA', 'Cena familiar'),
(5, 5, 'Valentina', 'Guerrero Hidalgo', '0923145678', '0957412369', '2026-07-23', '14:00:00', 'PSO-9934', 'PROGRAMADA', 'Celebración de cumpleaños');

INSERT OR IGNORE INTO registro_entrada
(id_entrada, id_visita, nombres, apellidos, cedula, fecha_llegada, hora_llegada,
 informacion_adicional, observaciones, tipo_entrada, placa_vehiculo)
VALUES
(1, 3, 'Daniela Sofía', 'Villacrés Mora', '1109876543', '2026-07-20', '09:12:00',
 'Visita previamente autorizada.', 'Ingreso sin novedades.', 'VISITANTE', NULL),
(2, NULL, 'Luis Fernando', 'Carrión Vega', '1719988776', '2026-07-20', '07:40:00',
 'Residente del condominio.', 'Acceso mediante tarjeta RFID.', 'RESIDENTE', 'PBC-5562'),
(3, NULL, 'Pedro Javier', 'Guamán Chicaiza', '1722233344', '2026-07-20', '11:05:00',
 'Técnico de internet contratado.', 'Se verificó orden de trabajo.', 'EXTERNA', 'TMA-8820'),
(4, 2, 'Carlos Andrés', 'Mendoza Ruiz', '0918765432', '2026-07-22', '14:57:00',
 'Entrega de documentación solicitada.', 'Esperó dos minutos en garita.', 'VISITANTE', 'PBC-2145'),
(5, NULL, 'Andrea Belén', 'Benítez Torres', '0921456789', '2026-07-20', '17:35:00',
 'Residente ingresando con acompañante.', 'Sin novedades.', 'RESIDENTE', 'PDD-1098');

INSERT OR IGNORE INTO vehiculo_visita
(id_vehiculo_visita, id_visita, placa, marca, modelo, color)
VALUES
(1, 1, 'PCD-4821', 'Kia', 'Rio', 'Gris'),
(2, 2, 'PBC-2145', 'Chevrolet', 'Sail', 'Azul');

INSERT OR IGNORE INTO ingreso_parqueadero
(id_ingreso_parqueadero, id_registro_entrada, id_parqueadero,
 fecha_hora_ingreso, fecha_hora_salida, estado)
VALUES
(1, 1, 2, '2026-07-20 09:12:00', '2026-07-20 10:45:00', 'LIBERADO');

INSERT OR IGNORE INTO alerta_seguridad
(id_alerta, id_registro_entrada, id_usuario_reporta, tipo,
 descripcion, nivel, fecha_creacion, estado)
VALUES
(1, 3, 4, 'VERIFICACION',
 'Se verificó identidad del técnico externo antes del ingreso',
 'BAJA', '2026-07-20 11:10:00', 'CERRADA');
-- ============================================================
-- GRF - COMUNICACIONES Y NOTIFICACIONES
-- ============================================================

INSERT OR IGNORE INTO mensaje
(id_mensaje,id_emisor,asunto,contenido,tipo,prioridad,fecha_creacion,fecha_envio,estado)
VALUES
(1,1,'Mantenimiento de piscina',
 'La piscina permanecerá cerrada el sábado de 08:00 a 12:00 por mantenimiento.',
 'MENSAJE_RESIDENTES','NORMAL',
 '2026-07-17 08:00:00','2026-07-17 08:05:00','ENVIADO'),
(2,5,'Reunión general de copropietarios',
 'La reunión general se realizará el próximo viernes a las 19:00.',
 'MENSAJE_GLOBAL','ALTA',
 '2026-07-16 17:00:00','2026-07-16 17:10:00','ENVIADO'),
(3,4,'Alerta de seguridad',
 'Se solicita verificar que las puertas de acceso permanezcan cerradas.',
 'MENSAJE_URGENTE','URGENTE',
 '2026-07-18 06:30:00','2026-07-18 06:35:00','ENVIADO');

INSERT OR IGNORE INTO mensaje_destinatario
(id_mensaje,id_usuario,leido,fecha_lectura)
VALUES
(1,2,1,'2026-07-17 09:00:00'),
(1,3,0,NULL),
(1,6,1,'2026-07-17 10:15:00'),
(1,7,0,NULL),
(2,2,1,'2026-07-16 18:00:00'),
(2,3,1,'2026-07-16 18:20:00'),
(2,5,1,'2026-07-16 17:15:00'),
(2,6,0,NULL),
(2,7,0,NULL),
(3,1,1,'2026-07-18 06:40:00'),
(3,4,1,'2026-07-18 06:36:00');

INSERT OR IGNORE INTO anuncio
(id_anuncio,id_autor,titulo,contenido,tipo,fecha_publicacion,fecha_expiracion,prioridad,estado)
VALUES
(1,5,'Asamblea general',
 'Se convoca a todos los propietarios a la asamblea general.',
 'ANUNCIO_GENERAL','2026-07-16 17:00:00',
 '2026-07-30 23:59:59','ALTA','PUBLICADO'),
(2,1,'Uso responsable de espacios comunes',
 'Recordamos respetar los horarios y reglamentos de cada espacio.',
 'ANUNCIO_GENERAL','2026-07-15 09:00:00',
 '2026-08-15 23:59:59','NORMAL','PUBLICADO');

INSERT OR IGNORE INTO notificacion
(id_notificacion,id_usuario,id_mensaje,id_anuncio,tipo,titulo,contenido,
 fecha_creacion,fecha_envio,leida,fecha_lectura,estado)
VALUES
(1,2,1,NULL,'MENSAJE','Mantenimiento de piscina',
 'La piscina tendrá mantenimiento programado.',
 '2026-07-17 08:05:00','2026-07-17 08:05:00',
 1,'2026-07-17 09:00:00','LEIDA'),
(2,3,NULL,1,'ANUNCIO','Asamblea general',
 'Revisa la convocatoria a la asamblea general.',
 '2026-07-16 17:05:00','2026-07-16 17:05:00',
 0,NULL,'ENVIADA'),
(3,6,NULL,NULL,'RESERVA','Reserva finalizada',
 'Tu reserva del Área BBQ fue marcada como finalizada.',
 '2026-07-12 16:35:00','2026-07-12 16:35:00',
 1,'2026-07-12 17:00:00','LEIDA'),
(4,2,NULL,NULL,'DEUDA','Nueva deuda generada',
 'Se generó una deuda por la reserva del Salón de Eventos.',
 '2026-07-15 09:10:00','2026-07-15 09:10:00',
 0,NULL,'ENVIADA'),
(5,1,3,NULL,'ALERTA','Alerta de seguridad',
 'Existe una alerta de seguridad activa.',
 '2026-07-18 06:35:00','2026-07-18 06:35:00',
 1,'2026-07-18 06:40:00','LEIDA');

INSERT OR IGNORE INTO historial_comunicacion
(id_historial,entidad_tipo,id_entidad,id_usuario,accion,asunto,tipo,
 prioridad,estado,detalle,fecha_registro)
VALUES
(1,'MENSAJE',1,1,'ENVIO','Mantenimiento de piscina',
 'MENSAJE_RESIDENTES','NORMAL','ENVIADO',
 'Mensaje enviado a residentes.','2026-07-17 08:05:00'),
(2,'ANUNCIO',1,5,'CREACION','Asamblea general',
 'ANUNCIO_GENERAL','ALTA','PUBLICADO',
 'Anuncio publicado para usuarios activos.','2026-07-16 17:00:00'),
(3,'NOTIFICACION',1,2,'NOTIFICACION','Mantenimiento de piscina',
 'MENSAJE','NORMAL','LEIDA',
 'Notificación leída por el destinatario.','2026-07-17 09:00:00');

-- ============================================================
-- VALIDACIONES RÁPIDAS
-- ============================================================

SELECT 'usuarios' AS tabla, COUNT(*) AS total FROM usuario
UNION ALL
SELECT 'roles', COUNT(*) FROM rol
UNION ALL
SELECT 'inmuebles', COUNT(*) FROM inmueble
UNION ALL
SELECT 'espacios_comunes', COUNT(*) FROM espacio_comun
UNION ALL
SELECT 'reservas', COUNT(*) FROM reserva
UNION ALL
SELECT 'deudas', COUNT(*) FROM deuda
UNION ALL
SELECT 'pagos', COUNT(*) FROM pago
UNION ALL
SELECT 'visitas_programadas', COUNT(*) FROM visitas_programadas
UNION ALL
SELECT 'registros_entrada', COUNT(*) FROM registro_entrada
UNION ALL
SELECT 'mensajes', COUNT(*) FROM mensaje
UNION ALL
SELECT 'notificaciones', COUNT(*) FROM notificacion;

PRAGMA foreign_key_check;
