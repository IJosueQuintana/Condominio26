# Documentación de Casos de Uso — Sistema de Gestión de Inmuebles

---

## UC1: administrarInmuebles

**Descripción**
Permite al Administrador gestionar los inmuebles registrados en el condominio, incluyendo edificios, departamentos, suites, estudios y locales comerciales.

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El administrador ha iniciado sesión.
- El sistema se encuentra operativo.

**Postcondiciones**
- La información del inmueble queda registrada o actualizada.
- Los cambios quedan almacenados en la base de datos.

**Flujo Principal**
1. El Administrador selecciona la opción "administrarInmuebles".
2. El sistema muestra el listado de inmuebles registrados.
3. El Administrador selecciona una acción (crear, editar, consultar o eliminar).
4. El sistema solicita los datos correspondientes.
5. El Administrador ingresa o modifica la información.
6. El sistema valida los datos.
7. El sistema guarda los cambios.
8. El sistema confirma la operación.

**Flujos Alternativos**
- **A1. Datos inválidos:** El sistema detecta información incorrecta o incompleta → muestra mensaje de error → el Administrador corrige los datos.
- **A2. Inmueble inexistente:** El Administrador intenta modificar un inmueble no registrado → el sistema informa que el inmueble no existe.

**Reglas de Negocio**
- Cada inmueble debe tener un identificador único.
- No se pueden registrar inmuebles duplicados.
- Solo los administradores pueden eliminar inmuebles.

---

## UC2: registrarEdificios

**Descripción**
Permite al Administrador registrar nuevos edificios dentro del condominio como parte de la gestión de inmuebles.

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El Administrador ha iniciado sesión.
- El Administrador está ejecutando el caso de uso "administrarInmuebles".

**Postcondiciones**
- El edificio queda registrado en el catálogo del condominio.

**Flujo Principal**
1. El Administrador indica que desea registrar un edificio.
2. El sistema solicita los datos del edificio (nombre, dirección, número de pisos, etc.).
3. El Administrador ingresa la información.
4. El sistema valida los datos.
5. El sistema guarda el edificio.
6. El sistema confirma el registro.

**Flujos Alternativos**
- **A1. Datos inválidos:** El sistema detecta error → muestra mensaje → el Administrador corrige.

**Reglas de Negocio**
- El nombre del edificio debe ser único dentro del condominio.
- Cada edificio debe pertenecer a un condominio existente.

---

## UC3: registrarDepartamentosSuitesEstudiosYLocales

**Descripción**
Permite al Administrador registrar unidades específicas (departamentos, suites, estudios, locales) dentro de los edificios del condominio.

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El edificio padre ya está registrado.
- El Administrador está ejecutando "administrarInmuebles".

**Postcondiciones**
- La unidad queda registrada y asociada a su edificio correspondiente.

**Flujo Principal**
1. El Administrador selecciona un edificio existente.
2. El sistema solicita el tipo de unidad y sus datos.
3. El Administrador ingresa la información.
4. El sistema valida y guarda.
5. El sistema confirma el registro.

**Flujos Alternativos**
- **A1. Edificio no existe:** El sistema informa y solicita registrar primero el edificio.

**Reglas de Negocio**
- Cada unidad debe tener un número único dentro del edificio.
- El tipo de unidad define ciertos atributos obligatorios.

---

## UC4: mantenerCatalogoDelCondominio

**Descripción**
Permite al Administrador mantener actualizado el catálogo general del condominio, incluyendo servicios, áreas comunes y reglamentos.

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El Administrador ha iniciado sesión.

**Postcondiciones**
- El catálogo queda actualizado con la nueva información.

**Flujo Principal**
1. El Administrador accede a "mantenerCatalogoDelCondominio".
2. El sistema muestra el catálogo actual.
3. El Administrador agrega, edita o elimina elementos.
4. El sistema valida y guarda.
5. El sistema confirma la operación.

**Flujos Alternativos**
- **A1. Datos duplicados:** El sistema impide agregar elementos ya existentes.

**Reglas de Negocio**
- El catálogo es consultable por todos los actores, pero solo modificable por el Administrador.

---

## UC5: gestionarCaracteristicasDelCondominio

**Descripción**
Permite al Administrador definir y modificar las características generales del condominio (nombre, reglas, horarios, políticas, etc.).

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El Administrador está en "mantenerCatalogoDelCondominio".

**Postcondiciones**
- Las características del condominio se actualizan.

**Flujo Principal**
1. El Administrador selecciona "gestionarCaracteristicasDelCondominio".
2. El sistema muestra las características actuales.
3. El Administrador modifica los valores.
4. El sistema valida y guarda.
5. El sistema confirma la actualización.

**Flujos Alternativos**
- **A1. Formato inválido:** El sistema rechaza datos mal formateados.

**Reglas de Negocio**
- Algunas características pueden tener efectos en otros módulos (ej: horarios de reserva).

---

## UC6: gestionarPropiedades

**Descripción**
Permite al Administrador y al Propietario gestionar la relación entre propietarios y las unidades que poseen dentro del condominio.

**Actor Principal**
Administrador, Propietario

**Actores Secundarios**
Ninguno

**Precondiciones**
- La unidad inmueble ya existe.
- El propietario está registrado en el sistema.

**Postcondiciones**
- La propiedad queda asignada o actualizada correctamente.

**Flujo Principal**
1. El actor selecciona "gestionarPropiedades".
2. El sistema muestra las propiedades existentes.
3. El actor asigna o modifica la relación propietario-unidad.
4. El sistema valida que el propietario y la unidad existan.
5. El sistema guarda los cambios.
6. El sistema confirma la operación.

**Flujos Alternativos**
- **A1. Propietario o unidad no existe:** El sistema informa y no permite la asignación.

**Reglas de Negocio**
- Una unidad puede tener uno o más propietarios.
- Un propietario puede tener múltiples unidades.
- El Administrador puede gestionar todas las propiedades; el Propietario solo las suyas.

---

## UC7: realizarReservaOAgenda

**Descripción**
Permite al Usuario (residente o inquilino) realizar reservas de áreas comunes o agendar eventos en el condominio.

**Actor Principal**
Usuario

**Actores Secundarios**
Ninguno

**Precondiciones**
- El Usuario ha iniciado sesión.
- El área común o recurso está disponible.

**Postcondiciones**
- La reserva queda registrada en el sistema.
- Se generan notificaciones automáticas.

**Flujo Principal**
1. El Usuario selecciona "realizarReservaOAgenda".
2. El sistema muestra los recursos disponibles.
3. El Usuario elige fecha, hora y recurso.
4. El sistema verifica disponibilidad.
5. El sistema confirma la reserva.
6. El sistema envía notificación al Usuario.

**Flujos Alternativos**
- **A1. Recurso no disponible:** El sistema sugiere fechas u horarios alternativos.

**Reglas de Negocio**
- No se pueden solapar reservas en el mismo recurso y horario.
- El Usuario no puede reservar más de X horas por semana (según política del condominio).

---

## UC8: gestionarNotificacionesYRecordatorios

**Descripción**
Permite gestionar el envío de notificaciones y recordatorios relacionados con reservas, pagos o eventos del condominio.

**Actor Principal**
Usuario

**Actores Secundarios**
Administrador, Propietario

**Precondiciones**
- Existe una reserva o evento programado.

**Postcondiciones**
- Las notificaciones se envían según la configuración.

**Flujo Principal**
1. El sistema detecta un evento que requiere notificación.
2. El sistema genera la notificación o recordatorio.
3. El sistema envía al medio configurado (correo, app, SMS).
4. El actor recibe y puede configurar preferencias.

**Flujos Alternativos**
- **A1. Fallo en envío:** El sistema reintenta o registra error en log.

**Reglas de Negocio**
- Los recordatorios se envían con 24h, 1h y 15min de anticipación.
- El Usuario puede desactivar notificaciones no críticas.

---

## UC9: actualizarEstadoDelInmueble

**Descripción**
Permite al Administrador actualizar el estado operativo de un inmueble (disponible, en mantenimiento, inhabilitado, etc.).

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- El inmueble existe en el sistema.

**Postcondiciones**
- El estado del inmueble queda actualizado.
- Se registra el cambio en el historial.

**Flujo Principal**
1. El Administrador selecciona "actualizarEstadoDelInmueble".
2. El sistema muestra el listado de inmuebles.
3. El Administrador selecciona uno y el nuevo estado.
4. El sistema valida la transición de estado.
5. El sistema guarda el cambio.
6. El sistema confirma la operación.

**Flujos Alternativos**
- **A1. Transición inválida:** El sistema rechaza el cambio (ej: inhabilitado a disponible sin inspección).

**Reglas de Negocio**
- Todo cambio de estado debe quedar registrado en el historial.
- Ciertos estados bloquean reservas o asociación a propietarios.

---

## UC10: generarReportesDeCambios

**Descripción**
Permite al Administrador generar reportes históricos de los cambios realizados sobre inmuebles, estados o asignaciones.

**Actor Principal**
Administrador

**Actores Secundarios**
Ninguno

**Precondiciones**
- Existe al menos un cambio registrado en el sistema.

**Postcondiciones**
- Se genera un reporte con los cambios solicitados.

**Flujo Principal**
1. El Administrador selecciona "generarReportesDeCambios".
2. El sistema solicita filtros (fecha, tipo de cambio, inmueble).
3. El Administrador ingresa los filtros.
4. El sistema consulta el historial.
5. El sistema genera el reporte (PDF, CSV, pantalla).
6. El Administrador visualiza o descarga el reporte.

**Flujos Alternativos**
- **A1. Sin datos:** El sistema informa que no hay cambios para los filtros seleccionados.

**Reglas de Negocio**
- El historial de cambios es inmutable.
- Solo el Administrador puede acceder a reportes de todos los inmuebles.
