# Especificación General de Casos de Uso — Sistema Condominio

Este documento consolida y unifica los casos de uso definidos por todos los equipos para el sistema de gestión del condominio.

---

## 1. Introducción al Sistema
El Sistema Condominio es una plataforma integral orientada a facilitar la administración y convivencia dentro de un conjunto habitacional o edificio. Está dividido en seis módulos funcionales interconectados:
1. **GRA (Pagos y Finanzas):** Gestión de alícuotas, cobros, diferimiento de deudas y reportes de solvencia.
2. **GRB (Gestión de Usuarios y Administradores):** Control de acceso, perfiles, asignación de roles y permisos.
3. **GRC (Gestión de Inmuebles):** Control del inventario físico (edificios, departamentos, locales), asignación de propietarios y agenda de reservas general.
4. **GRD (Reservas de Espacios Comunes):** Reservación específica de áreas comunes, salones de eventos, control de disponibilidad y cancelaciones por los residentes.
5. **GRE (Check-in / Accesos):** Control y registro de entrada de visitantes, registro de vehículos y parqueaderos, programación de visitas y envío de alertas de seguridad.
6. **GRF (Comunicación):** Difusión de anuncios, envío de mensajería interna global y segmentada a residentes y trabajadores.

---

## 2. Modelo de Actores
A continuación se definen los actores que interactúan con el sistema:

*   **Usuario:** Actor general del sistema. Posee una cuenta y credenciales para autenticarse. Todos los actores físicos heredan los permisos básicos del Usuario (inicio de sesión, actualización de perfil y recuperación de contraseña).
*   **Administrador:** Encargado de la gestión general del condominio, incluyendo las finanzas, usuarios, inmuebles, catálogo, alertas y anuncios de comunicación.
*   **Residente:** Habitante del condominio. Puede consultar y pagar sus deudas, solicitar financiamientos, descargar certificados de solvencia, realizar reservas de áreas comunes, registrar visitas programadas y recibir notificaciones y alertas.
*   **Propietario:** Dueño de uno o varios inmuebles en el condominio. Puede gestionar las relaciones de sus propiedades.
*   **Trabajador:** Conserjes, personal de mantenimiento, etc. Puede recibir comunicados y consultar su historial de mensajes.
*   **Personal de Seguridad:** Encargado de la vigilancia física y del registro de ingresos de visitantes y vehículos en la garita.
*   **Sistema (Automático):** Proceso autónomo encargado de la generación mensual de alícuotas, envíos de recordatorios automáticos de deudas, registro de bitácoras de comunicación y notificaciones push/correo electrónico.

---

## 3. Especificación de Casos de Uso por Módulo

### 3.1. GRA - Módulo de Pagos y Finanzas

#### UC-GRA-01: registrarDeuda
*   **Descripción:** Permite al Administrador registrar una deuda específica o cargo extraordinario a un residente.
*   **Actor Principal:** Administrador
*   **Precondiciones:** El Administrador ha iniciado sesión. El residente destinatario existe.
*   **Flujo Principal:**
    1. El Administrador ingresa el identificador del residente, monto y descripción.
    2. El Sistema valida los datos ingresados.
    3. El Administrador confirma el registro.
    4. El Sistema guarda la deuda con estado "PENDIENTE".
*   **Flujos Alternativos:**
    *   **A1. Datos inválidos:** El Sistema muestra un mensaje de error y no registra la deuda.

#### UC-GRA-02: registrarPagoEfectivoTransferencia (Anderson)
*   **Descripción:** Permite al Administrador registrar manualmente un pago recibido por transferencia bancaria o en efectivo.
*   **Actor Principal:** Administrador
*   **Precondiciones:** El Administrador ha iniciado sesión. La deuda identificada existe en estado "PENDIENTE".
*   **Flujo Principal:**
    1. El Administrador ingresa el ID de la deuda.
    2. El Sistema verifica la existencia y el estado "PENDIENTE" de la deuda.
    3. Si es por transferencia, el Sistema muestra el comprobante cargado.
    4. El Administrador confirma la recepción del pago y cambia el estado a "PAGADA".
    5. El Sistema muestra el mensaje de éxito.
*   **Flujos Alternativos:**
    *   **A1. Deuda no encontrada:** El Sistema emite "No existe una deuda con el identificador proporcionado".
    *   **A2. Deuda ya pagada:** El Sistema emite "Esta deuda ya ha sido pagada".

#### UC-GRA-03: modificarFechaMaximaDePagoDeUnaDeuda (Ricardo)
*   **Descripción:** Permite al Administrador ampliar o modificar la fecha máxima de vencimiento de una deuda pendiente.
*   **Actor Principal:** Administrador
*   **Precondiciones:** La deuda existe en el sistema.
*   **Flujo Principal:**
    1. El Administrador ingresa el número de deuda.
    2. El Sistema recupera los datos de la deuda.
    3. El Administrador ingresa la nueva fecha de pago.
    4. El Sistema valida que la fecha sea válida (futura y posterior a la fecha máxima actual).
    5. El Sistema actualiza la fecha máxima de pago de la deuda.
*   **Flujos Alternativos:**
    *   **A1. Deuda no registrada:** El Sistema emite "Deuda no registrada en el sistema".
    *   **A2. Fecha inválida:** El Sistema emite "La fecha ingresada no es válida...".

#### UC-GRA-04: definirValorMensualDeAlicuotas (Ricardo)
*   **Descripción:** Permite al Administrador establecer el valor estándar mensual a cobrar por concepto de alícuotas ordinarias.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador ingresa el nuevo valor de la alícuota.
    2. El Sistema valida que sea un número mayor que 0 y con hasta 2 decimales.
    3. El Sistema actualiza el valor mensual configurado.
*   **Flujos Alternativos:**
    *   **A1. Valor inválido:** El Sistema emite "El valor de la alícuota ingresado no es válido...".

#### UC-GRA-05: generarReporteDePagosRealizados (Bryan)
*   **Descripción:** Permite generar un reporte en PDF/pantalla de todos los pagos efectuados por un residente.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador ingresa el identificador del residente.
    2. El Sistema verifica la existencia del residente y busca sus pagos asociados.
    3. El Sistema genera y muestra el reporte con detalles de montos, descripción, fechas y métodos de pago.
*   **Flujos Alternativos:**
    *   **A1. Residente no existe:** El Sistema emite "No existe un residente asociado al identificador ingresado".
    *   **A2. Sin pagos registrados:** El Sistema emite "No existen pagos realizados asociados al residente".

#### UC-GRA-06: registrarEntidadBancaria (Bryan)
*   **Descripción:** Permite registrar las cuentas de banco autorizadas del condominio para recibir transferencias.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador ingresa el nombre de la entidad bancaria y el número de cuenta.
    2. El Sistema valida la información y verifica la no duplicidad del número de cuenta.
    3. El Sistema almacena la entidad bancaria.
*   **Flujos Alternativos:**
    *   **A1. Cuenta ya registrada:** El Sistema emite "Ya existe una entidad bancaria registrada con el número de cuenta ingresado".

#### UC-GRA-07: eliminarDeuda (Jorge)
*   **Descripción:** Permite al Administrador anular de forma lógica una deuda registrada por error.
*   **Actor Principal:** Administrador
*   **Precondiciones:** La deuda se encuentra en estado "PENDIENTE" y no está asociada a ningún convenio de cuotas activo.
*   **Flujo Principal:**
    1. El Administrador busca la deuda y selecciona "Eliminar Deuda".
    2. El Sistema solicita confirmación y justificación obligatoria.
    3. El Administrador ingresa la justificación y confirma.
    4. El Sistema ejecuta el borrado lógico ("ELIMINADA"), registra la auditoría y recalcula el saldo del residente.
*   **Flujos Alternativos:**
    *   **A1. Vinculada a plan de pagos:** El Sistema bloquea la acción indicando que debe disolver el convenio de pago primero.

#### UC-GRA-08: registrarPago
*   **Descripción:** Permite al Residente notificar la realización de un pago de alícuota adjuntando el comprobante.
*   **Actor Principal:** Residente
*   **Precondiciones:** El Residente ha iniciado sesión y tiene deudas pendientes.
*   **Flujo Principal:**
    1. El Residente selecciona la deuda a pagar.
    2. Selecciona el método de pago (transferencia/depósito).
    3. Adjunta la foto/PDF del comprobante y envía.
    4. El Sistema guarda el pago con estado "PENDIENTE_VERIFICACION" y notifica al Administrador.

#### UC-GRA-09: definirPagoDeAlicuotasDeFormaMensual (Ricardo)
*   **Descripción:** Permite configurar la automatización temporal del pago mensual para proyectar deudas de alícuotas recurrentes.
*   **Actor Principal:** Administrador (y se asigna al cronograma del Residente)
*   **Flujo Principal:**
    1. El Administrador ingresa el número de meses de proyección y el día límite de pago (entre 1 y 28).
    2. El Sistema valida los datos y calcula las fechas límites de pago de cada alícuota proyectada.
    3. El Sistema genera las deudas mensuales proyectadas.

#### UC-GRA-10: solicitarPagoEnCuotas (Jorge)
*   **Descripción:** Permite al Residente diferir el pago de una deuda pendiente (ej. extraordinarias) en cuotas mensuales (de 2 a 12 meses).
*   **Actor Principal:** Residente
*   **Precondiciones:** El Residente tiene deudas pendientes.
*   **Flujo Principal:**
    1. El Residente selecciona la deuda elegible y hace clic en "Solicitar Pago en Cuotas".
    2. El Residente define el número de cuotas deseadas.
    3. El Sistema simula el plan de pagos (número de cuotas, montos redondeados, fechas límites).
    4. El Residente acepta los términos.
    5. El Sistema registra el plan de pagos como "SOLICITADO" y genera una alerta al Administrador.
*   **Flujos Alternativos:**
    *   **A1. Deuda ya diferida:** El Sistema alerta que la obligación ya tiene un plan activo.
    *   **A2. Cancelación voluntaria:** El Residente cancela la simulación y el sistema revierte los cálculos sin alterar la base de datos.

#### UC-GRA-11: eliminarFormaDePagoRecurrente (Jorge)
*   **Descripción:** Permite al Residente desvincular un método de pago guardado para cobros recurrentes de alícuotas (débito automático).
*   **Actor Principal:** Residente
*   **Flujo Principal:**
    1. El Residente ingresa al módulo de métodos de pago y selecciona la tarjeta/cuenta configurada como recurrente.
    2. Selecciona "Eliminar Forma de Pago Recurrente".
    3. El Sistema muestra una advertencia sobre la necesidad de pago manual futuro.
    4. El Residente confirma; el sistema se comunica con la pasarela de pagos, desactiva la recurrencia y envía confirmación al correo.
*   **Flujos Alternativos:**
    *   **A1. Error en pasarela de pagos:** El Sistema revierte la acción local e informa que el débito sigue activo y debe reintentarse.

#### UC-GRA-12: generarCertificadoDeNoDeudor (Jorge)
*   **Descripción:** Genera un certificado de solvencia (No Deudor) en PDF no editable para el residente que se encuentra al día.
*   **Actor Principal:** Residente o Administrador
*   **Flujo Principal:**
    1. El usuario solicita generar el certificado.
    2. El Sistema valida que no existan obligaciones pendientes ni en mora.
    3. El Sistema genera el PDF con los datos del residente, departamento y código de verificación.
    4. Registra la emisión en la bitácora de auditoría y habilita la descarga.
*   **Flujos Alternativos:**
    *   **A1. Convenio de pago al día:** Si el residente tiene cuotas diferidas no vencidas, se le permite generar un certificado parcial con la observación de que posee un plan activo.

#### UC-GRA-13: consultarDeuda (Ricardo)
*   **Descripción:** Permite visualizar el estado y detalle de las obligaciones de pago de un residente.
*   **Actor Principal:** Administrador, Residente
*   **Flujo Principal:**
    1. El Administrador (por cédula) o el Residente (de su propia cuenta) solicita consultar deudas.
    2. El Sistema recupera todas las obligaciones financieras asociadas.
    3. Se muestra un desglose de deudas pendientes, pagadas o vencidas.

#### UC-GRA-14: consultarPagosEfectuados (Bryan)
*   **Descripción:** Permite consultar los pagos realizados filtrando por un rango de fechas.
*   **Actor Principal:** Administrador, Residente
*   **Flujo Principal:**
    1. El actor ingresa la fecha de inicio y la fecha de fin.
    2. El Sistema valida el formato y que la fecha de fin sea posterior a la de inicio.
    3. El Sistema muestra los pagos registrados correspondientes.

#### UC-GRA-15: registrarDeudaMensual (Sistema)
*   **Descripción:** Caso de uso automático que se ejecuta el primer día de cada mes para registrar las alícuotas ordinarias a todas las propiedades.
*   **Actor Principal:** Sistema (Automático)
*   **Postcondiciones:** Se crean deudas en estado "PENDIENTE" para cada departamento y suite según el valor configurado.

#### UC-GRA-16: enviarRecordatorioDeDeudaPendiente (Sistema / Bryan)
*   **Descripción:** Envía notificaciones de cobro automáticas al correo de los residentes que tienen deudas pendientes o en mora.
*   **Actor Principal:** Sistema (Automático)
*   **Flujo Principal:**
    1. El Sistema detecta deudas vencidas o próximas a vencer.
    2. Genera un correo electrónico detallando el saldo pendiente y fecha límite.
    3. Envía el correo al destinatario y registra el evento en el log.

---

### 3.2. GRB - Módulo de Gestión de Usuarios y Administradores

#### UC-GRB-01: registrarCuenta
*   **Descripción:** Permite al Administrador registrar un nuevo usuario (residente, guardia, trabajador) en el sistema.
*   **Actor Principal:** Administrador
*   **Precondiciones:** El Administrador ha iniciado sesión y tiene permisos.
*   **Flujo Principal:**
    1. El Administrador proporciona nombres, apellidos y correo electrónico del usuario.
    2. El Sistema valida los campos y comprueba que el correo no esté registrado.
    3. El Administrador define la contraseña inicial.
    4. El Sistema guarda la cuenta y envía un correo con credenciales de acceso.

#### UC-GRB-02: actualizarInformacionDeCuenta
*   **Descripción:** Permite modificar datos básicos de una cuenta existente.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador selecciona el usuario y el sistema muestra los datos actuales.
    2. El Administrador modifica los datos y guarda.
    3. El Sistema valida y aplica los cambios.

#### UC-GRB-03: desactivarCuenta
*   **Descripción:** Permite desactivar el acceso de un usuario al sistema temporal o definitivamente, sin borrar su historial de transacciones.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador selecciona la cuenta activa y solicita desactivar.
    2. El Sistema solicita confirmación.
    3. Al confirmar, el estado de la cuenta cambia a "desactivada" y se revoca su token de sesión.

#### UC-GRB-04: asignarRolAUsuario
*   **Descripción:** Permite cambiar el rol asignado a un usuario (ej. de Residente a Directiva, o agregar rol Conserje).
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador busca al usuario.
    2. Selecciona "Asignar nuevo rol" y elige de la lista de roles del sistema.
    3. Guarda los cambios. Los nuevos permisos se aplican inmediatamente.

#### UC-GRB-05: definirPermisosDeRol
*   **Descripción:** Permite configurar las acciones autorizadas para cada rol en el sistema.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador ingresa el nombre del rol y selecciona los permisos.
    2. El Sistema guarda o actualiza el mapeo de permisos del rol.

#### UC-GRB-06: iniciarSesion
*   **Descripción:** Permite la autenticación de usuarios y administradores en la plataforma.
*   **Actor Principal:** Usuario
*   **Flujo Principal:**
    1. El Usuario ingresa su correo electrónico y contraseña.
    2. El Sistema valida las credenciales y el estado activo de la cuenta.
    3. Si es correcto, otorga acceso y redirige según el rol.
*   **Flujos Alternativos:**
    *   **A1. Credenciales incorrectas:** Se muestra "Credenciales incorrectas" y se niega el ingreso.
    *   **A2. Cuenta desactivada:** Se emite "La cuenta se encuentra desactivada".

#### UC-GRB-07: actualizarPerfil
*   **Descripción:** Permite a cualquier usuario logueado cambiar su información personal (foto, teléfono, contraseña).
*   **Actor Principal:** Usuario
*   **Flujo Principal:**
    1. El Usuario ingresa a su perfil.
    2. Modifica la información deseada.
    3. El Sistema valida y almacena los cambios.

#### UC-GRB-08: recuperarContrasena
*   **Descripción:** Permite a un usuario restablecer su contraseña olvidada mediante un correo electrónico.
*   **Actor Principal:** Usuario
*   **Flujo Principal:**
    1. El Usuario ingresa su correo en la página de inicio.
    2. El Sistema valida que la cuenta exista.
    3. El Sistema ejecuta el caso de uso "enviarTokenDeRestablecimiento".
    4. El Usuario ingresa el token recibido y su nueva contraseña.
    5. El Sistema valida la contraseña, actualiza la cuenta y confirma.

#### UC-GRB-09: enviarTokenDeRestablecimiento
*   **Descripción:** Caso de uso automático que genera un token temporal único para recuperar contraseñas.
*   **Actor Principal:** Sistema (Automático)
*   **Postcondiciones:** Se asocia el token de seguridad a la cuenta y se envía al correo del usuario.

---

### 3.3. GRC - Módulo de Gestión de Inmuebles

#### UC-GRC-01: administrarInmuebles
*   **Descripción:** Permite al Administrador gestionar los elementos físicos del condominio.
*   **Actor Principal:** Administrador
*   **Flujo Principal:**
    1. El Administrador selecciona "administrarInmuebles".
    2. El Sistema despliega la lista de inmuebles.
    3. El Administrador puede agregar, editar o eliminar registros de unidades.
*   **Relaciones:** Incluye `registrarEdificios` y `registrarDepartamentosSuitesEstudiosYLocales`.

#### UC-GRC-02: registrarEdificios
*   **Descripción:** Permite ingresar nuevos edificios/bloques dentro de la infraestructura del condominio.
*   **Actor Principal:** Administrador (incluido en administrarInmuebles)

#### UC-GRC-03: registrarDepartamentosSuitesEstudiosYLocales
*   **Descripción:** Permite registrar unidades habitacionales específicas asociadas a un edificio padre.
*   **Actor Principal:** Administrador (incluido en administrarInmuebles)

#### UC-GRC-04: mantenerCatalogoDelCondominio
*   **Descripción:** Permite actualizar el listado global de áreas comunes, servicios y reglamentos del condominio.
*   **Actor Principal:** Administrador
*   **Relaciones:** Incluye `gestionarCaracteristicasDelCondominio`.

#### UC-GRC-05: gestionarCaracteristicasDelCondominio
*   **Descripción:** Configura variables del condominio (horarios generales, políticas, reglas de convivencia).
*   **Actor Principal:** Administrador (incluido en mantenerCatalogoDelCondominio)

#### UC-GRC-06: gestionarPropiedades
*   **Descripción:** Permite definir qué propietario es dueño de qué unidades específicas (departamentos, locales, estacionamientos).
*   **Actor Principal:** Administrador, Propietario
*   **Flujo Principal:**
    1. El actor selecciona la propiedad.
    2. Asigna o modifica la relación entre la unidad y el propietario.
    3. El Administrador puede asignar a cualquier cuenta; el Propietario solo visualiza y gestiona sus relaciones asignadas.

#### UC-GRC-07: realizarReservaOAgenda
*   **Descripción:** Permite solicitar el uso de una instalación del condominio en una fecha y hora determinadas.
*   **Actor Principal:** Usuario (Residente)
*   **Relaciones:** Incluye `gestionarNotificacionesYRecordatorios` (UC8).

#### UC-GRC-08: gestionarNotificacionesYRecordatorios
*   **Descripción:** Permite configurar y disparar alertas relacionadas con las reservaciones.
*   **Actor Principal:** Usuario
*   **Postcondiciones:** Se envían confirmaciones y alertas periódicas por los canales configurados (SMS, correo, app).

#### UC-GRC-09: actualizarEstadoDelInmueble
*   **Descripción:** Permite cambiar la operatividad de un inmueble (disponible, mantenimiento, inhabilitado).
*   **Actor Principal:** Administrador
*   **Relaciones:** Incluye `generarReportesDeCambios`.

#### UC-GRC-10: generarReportesDeCambios
*   **Descripción:** Muestra un histórico detallado de los cambios de estado y modificaciones del inmueble.
*   **Actor Principal:** Administrador

---

### 3.4. GRD - Módulo de Reservas de Espacios Comunes

#### UC-GRD-01: reservarAreaComun
*   **Descripción:** Permite al Residente agendar un espacio del condominio.
*   **Actor Principal:** Residente
*   **Precondiciones:** El espacio se encuentra disponible en la fecha seleccionada.
*   **Flujo Principal:**
    1. El Residente selecciona el área común.
    2. Visualiza la disponibilidad de horarios (incluido).
    3. Confirma la reserva de fecha y hora.
*   **Relaciones:**
    *   Especializaciones: `reservarSalonDeEventos` (UC2) y `reservarAreaRecreacional` (UC3).
    *   Incluye: `visualizarDisponibilidadDeEspacios` (UC4).

#### UC-GRD-02: reservarSalonDeEventos
*   **Descripción:** Reserva específica del salón comunal para eventos privados (hereda de reservarAreaComun).

#### UC-GRD-03: reservarAreaRecreacional
*   **Descripción:** Reserva específica de canchas, piscinas o parrillas (hereda de reservarAreaComun).

#### UC-GRD-04: visualizarDisponibilidadDeEspacios
*   **Descripción:** Permite consultar los horarios libres de cada área común en tiempo real (incluido en reservarAreaComun).

#### UC-GRD-05: cancelarReserva
*   **Descripción:** Permite al Residente dar de baja una reserva programada con anticipación.
*   **Actor Principal:** Residente
*   **Flujo Principal:**
    1. El Residente selecciona su reserva activa.
    2. Solicita la cancelación.
    3. El Sistema libera el espacio y envía confirmación.

---

### 3.5. GRE - Módulo Check-in

#### UC-GRE-01: enviarAlerta
*   **Descripción:** Envía una alerta de seguridad (emergencia, incendio, etc.) a todos los residentes de forma inmediata.
*   **Actor Principal:** Administrador, Personal de Seguridad
*   **Destinatario:** Residente
*   **Flujo Principal:**
    1. El actor selecciona enviar alerta de seguridad.
    2. Detalla el tipo de emergencia y mensaje de alerta.
    3. Confirma el envío.
    4. Los residentes reciben la alerta en tiempo real en sus dispositivos.

#### UC-GRE-02: programarVisita
*   **Descripción:** Permite a un residente registrar previamente una visita para facilitar el paso por garita.
*   **Actor Principal:** Residente
*   **Flujo Principal:**
    1. El Residente ingresa los datos del visitante (nombre, CI, fecha, hora, motivo).
    2. El Sistema registra la visita programada.
    3. El Personal de Seguridad puede ver la lista de visitas programadas en garita.

#### UC-GRE-03: registrarEntrada
*   **Descripción:** Registra el ingreso físico de una persona al condominio.
*   **Actor Principal:** Personal de Seguridad
*   **Precondiciones:** El visitante está en la garita.
*   **Flujo Principal:**
    1. El guardia ingresa los datos del visitante y del departamento visitado.
    2. El Sistema notifica al residente de la llegada (incluido).
    3. Se almacena el ingreso en la bitácora histórica (incluido).
*   **Relaciones:**
    *   Incluye: `notificarEntrada` (UC4) y `registrarHistorial` (UC5).
    *   Extiende: `registrarIngresoParqueadero` (UC6) si el visitante ingresa en vehículo.

#### UC-GRE-04: notificarEntrada
*   **Descripción:** Informa inmediatamente al residente que su visita ha cruzado el control (incluido en registrarEntrada).

#### UC-GRE-05: registrarHistorial
*   **Descripción:** Almacena de forma inmutable la fecha, hora e información del visitante en la base de datos (incluido en registrarEntrada).

#### UC-GRE-06: registrarIngresoParqueadero
*   **Descripción:** Asigna un cajón de estacionamiento de visitas libre si la persona ingresa con vehículo.
*   **Actor Principal:** Personal de Seguridad (extiende registrarEntrada)

---

### 3.6. GRF - Módulo de Comunicación

#### UC-GRF-01: Enviar Mensaje a Residentes
*   **Descripción:** Permite al Administrador enviar una comunicación masiva o dirigida únicamente a las cuentas de residentes.
*   **Actor Principal:** Administrador

#### UC-GRF-02: Enviar Comunicado a Trabajadores
*   **Descripción:** Permite al Administrador enviar directrices o mensajes al personal del condominio (conserjes, guardias).
*   **Actor Principal:** Administrador

#### UC-GRF-03: Enviar Mensaje Global
*   **Descripción:** Envía un comunicado simultáneo a residentes y trabajadores.
*   **Actor Principal:** Administrador

#### UC-GRF-04: Crear Anuncio General
*   **Descripción:** Publica un aviso en la cartelera digital del condominio para que sea visible por todos.
*   **Actor Principal:** Administrador

#### UC-GRF-05: Modificar Anuncio General
*   **Descripción:** Permite editar un anuncio publicado anteriormente.
*   **Actor Principal:** Administrador

#### UC-GRF-06: Eliminar Anuncio General
*   **Descripción:** Da de baja un anuncio de la cartelera digital del condominio.
*   **Actor Principal:** Administrador

#### UC-GRF-07: Crear Reporte de Comunicación
*   **Descripción:** Genera estadísticas del porcentaje de lectura y alcance de los comunicados enviados.
*   **Actor Principal:** Administrador

#### UC-GRF-08: Consultar Historial de Mensajes
*   **Descripción:** Permite revisar los mensajes recibidos y enviados.
*   **Actor Principal:** Administrador, Residente, Trabajador (los residentes y trabajadores solo ven sus correspondientes mensajes).

#### UC-GRF-09: Registrar Historial de Comunicaciones (Sistema)
*   **Descripción:** Guarda automáticamente los registros de todas las transmisiones de mensajes en la base de datos para auditorías.
*   **Actor Principal:** Sistema (Automático)

#### UC-GRF-10: Notificar Mensaje Recibido (Sistema)
*   **Descripción:** Dispara alertas push o correos automáticos avisando que se ha publicado un nuevo comunicado.
*   **Actor Principal:** Sistema (Automático)
