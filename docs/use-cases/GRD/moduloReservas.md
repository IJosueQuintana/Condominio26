# Módulo GRD - Reservas

## Índice

- [CU-01 Reservar Espacio Común](#cu-01-reservar-espacio-común)
- [CU-02 Consultar Disponibilidad de Espacios](#cu-02-consultar-disponibilidad-de-espacios)
- [CU-03 Ver Mis Reservas](#cu-03-ver-mis-reservas)
- [CU-04 Cancelar Reserva](#cu-04-cancelar-reserva)
- [CU-05 Agregar Observación de Reserva](#cu-05-agregar-observación-de-reserva)
- [CU-06 Auditar Reservas](#cu-06-auditar-reservas)
- [CU-07 Solicitar Multa por Infracción](#cu-07-solicitar-multa-por-infracción)
- [CU-08 Finalizar Reservas Vencidas](#cu-08-finalizar-reservas-vencidas)
- [CU-09 Verificar Mora del Residente](#cu-09-verificar-mora-del-residente)
- [CU-10 Registrar Deuda por Reserva](#cu-10-registrar-deuda-por-reserva)

---

# CU-01 Reservar Espacio Común

## Descripción

Permite al Residente reservar cualquier espacio común disponible (áreas comunes, salón de eventos, áreas recreacionales, etc.), seleccionando el espacio, la fecha y el horario deseado. No existen flujos separados por tipo de espacio: todo espacio reservable se gestiona de forma genérica.

## Actor Principal

Residente

## Precondiciones

- El Residente debe haber iniciado sesión en el sistema.
- Debe existir al menos un espacio reservable registrado (módulo GRC - Inmuebles).
- El módulo de reservas debe encontrarse disponible.

## Flujo Principal

1. El Residente ingresa al módulo de reservas y selecciona el tipo de espacio que desea reservar.
2. El sistema ejecuta **CU-02 Consultar Disponibilidad de Espacios** para mostrar la grilla semanal de horarios del espacio seleccionado.
3. El Residente selecciona, dentro de la grilla, el bloque de fecha y horario que desea reservar.
4. El sistema ejecuta **CU-09 Verificar Mora del Residente**.
5. El sistema valida que el horario seleccionado no se superponga con una reserva activa existente para ese espacio.
6. El sistema registra la reserva en estado ACTIVA, junto con la tarifa vigente del espacio.
7. El sistema ejecuta **CU-10 Registrar Deuda por Reserva**.
8. El sistema confirma la reserva al Residente y actualiza la disponibilidad mostrada.

## Flujos Alternos

- **A1 - Residente en mora:** si CU-09 determina que el Residente tiene deudas en mora, el sistema rechaza la reserva e informa que debe saldar su deuda para continuar.
- **A2 - Horario no disponible:** si el horario seleccionado se superpone con otra reserva activa, el sistema rechaza la reserva e informa que el horario no está disponible.

## Postcondiciones

- La reserva queda registrada en estado ACTIVA.
- El espacio queda no disponible en la fecha y horario reservados.
- Si el espacio tiene costo asociado, queda registrada la deuda correspondiente en Finanzas (GRA).

---

# CU-02 Consultar Disponibilidad de Espacios

## Descripción

Muestra la disponibilidad real de un espacio común para una semana determinada, marcando los bloques ya ocupados por reservas activas.

## Actor Principal

Sistema (caso de uso incluido desde CU-01; también se ejecuta al cambiar de espacio o de semana dentro del módulo de reservas)

## Precondiciones

- Debe existir al menos un espacio reservable registrado.
- El módulo de reservas debe encontrarse disponible.

## Flujo Principal

1. El sistema recibe el espacio y la semana a consultar (semana actual o siguiente).
2. El sistema consulta, día por día, las reservas activas registradas para ese espacio.
3. El sistema marca como "Reservado" los bloques horarios ocupados y deja el resto visibles como disponibles.
4. El sistema muestra al Residente la grilla semanal actualizada, permitiendo navegar entre la semana actual y la siguiente.

## Postcondiciones

- La disponibilidad mostrada refleja fielmente las reservas activas registradas en el sistema.

---

# CU-03 Ver Mis Reservas

## Descripción

Permite al Residente consultar el listado de sus propias reservas, con su estado actualizado (Activa, Finalizada o Cancelada).

## Actor Principal

Residente

## Precondiciones

- El Residente debe haber iniciado sesión en el sistema.
- El módulo de reservas debe encontrarse disponible.

## Flujo Principal

1. El Residente ingresa a la pantalla "Mis Reservas".
2. El sistema identifica al Residente a partir de su sesión activa.
3. El sistema obtiene las reservas del Residente y evalúa si alguna reserva activa ya venció, actualizando su estado a Finalizada de ser el caso.
4. El sistema muestra el espacio, fecha, horario y estado de cada reserva.
5. Para cada reserva, el sistema habilita la opción "Cancelar" si está activa, o "Añadir Observación" si está finalizada y el Residente aún no ha comentado esa reserva.

## Postcondiciones

- El Residente visualiza el estado real y actualizado de todas sus reservas.

---

# CU-04 Cancelar Reserva

## Descripción

Permite cancelar una reserva activa, ya sea por iniciativa del propio Residente o por un Administrador durante la auditoría de reservas.

## Actor Principal

Residente, Administrador

## Precondiciones

- El usuario debe haber iniciado sesión en el sistema.
- Debe existir una reserva activa: propia (si es Residente) o cualquiera (si es Administrador).
- El módulo de reservas debe encontrarse disponible.

## Flujo Principal

1. El usuario selecciona una reserva activa desde "Mis Reservas" (Residente) o desde "Auditar Reservas" (Administrador).
2. El usuario confirma la cancelación, indicando opcionalmente un motivo.
3. El sistema cambia el estado de la reserva a CANCELADA y libera el horario correspondiente.
4. El sistema notifica al Residente titular de la reserva la cancelación y su motivo.

## Postcondiciones

- La reserva queda cancelada en el sistema.
- El espacio liberado queda nuevamente disponible para ese horario.
- El Residente titular recibe una notificación de la cancelación.

---

# CU-05 Agregar Observación de Reserva

## Descripción

Permite registrar observaciones o comentarios sobre una reserva ya finalizada, tanto por el Residente titular como por un Administrador. Cada reserva admite como máximo dos observaciones, y un mismo usuario no puede comentar dos veces la misma reserva.

## Actor Principal

Residente, Administrador

## Precondiciones

- El usuario debe haber iniciado sesión en el sistema.
- Debe existir la reserva correspondiente, en estado FINALIZADA.
- El usuario no debe haber registrado previamente una observación sobre esa reserva.
- La reserva no debe tener ya dos observaciones registradas.

## Flujo Principal

1. El usuario selecciona una reserva finalizada y la opción "Añadir Observación".
2. El usuario redacta el texto de la observación.
3. Si el usuario es Administrador, puede activar **CU-07 Solicitar Multa por Infracción**, indicando el motivo.
4. El sistema valida que el usuario no haya comentado antes esa reserva y que no se supere el límite de dos observaciones.
5. El sistema registra la observación asociada a la reserva.
6. El sistema notifica al Residente titular el contenido de la observación (y de la multa, si fue solicitada).

## Postcondiciones

- La observación queda asociada a la reserva.
- El Residente titular recibe una notificación con el contenido registrado.

---

# CU-06 Auditar Reservas

## Descripción

Permite al Administrador visualizar y filtrar el listado completo de reservas del condominio, para su supervisión.

## Actor Principal

Administrador

## Precondiciones

- El Administrador debe haber iniciado sesión en el sistema.
- El módulo de reservas debe encontrarse disponible.

## Flujo Principal

1. El Administrador accede a la pantalla de auditoría de reservas.
2. El sistema muestra el listado completo de reservas registradas.
3. El Administrador aplica filtros por nombre de residente, espacio y/o rango de fechas.
4. El sistema actualiza la tabla de reservas de acuerdo con los filtros aplicados.
5. Desde la tabla, el Administrador puede ejecutar **CU-04 Cancelar Reserva** o **CU-05 Agregar Observación de Reserva** sobre cualquier reserva listada.

## Postcondiciones

- El Administrador cuenta con visibilidad completa y filtrable de las reservas del condominio.

---

# CU-07 Solicitar Multa por Infracción

## Descripción

Permite al Administrador solicitar, junto con una observación, que se registre una multa contra el Residente titular de una reserva por una infracción (por ejemplo, daños al espacio o inasistencia).

## Actor Principal

Administrador

## Precondiciones

- Debe existir una reserva finalizada seleccionada.
- El Administrador debe encontrarse registrando una observación sobre esa reserva (CU-05).

## Flujo Principal

1. Al redactar la observación, el Administrador activa la opción "Añadir Multa".
2. El Administrador selecciona el motivo de la multa (por ejemplo, "Dañar los espacios" o "No show").
3. Al enviar la observación, el sistema ejecuta **CU-10 Registrar Deuda por Reserva** para registrar la multa a nombre del Residente, con vencimiento a 7 días.
4. El sistema notifica al Residente titular la observación junto con la multa impuesta.

## Postcondiciones

- Queda registrada en Finanzas (GRA) una deuda de tipo MULTA asociada al Residente.
- El Residente titular recibe una notificación de la multa impuesta.

---

# CU-08 Finalizar Reservas Vencidas

## Descripción

Actualiza automáticamente el estado de las reservas cuyo horario ya transcurrió, cambiándolas de ACTIVA a FINALIZADA.

## Actor Principal

Sistema

## Precondiciones

- Deben existir reservas en estado ACTIVA cuya fecha y hora de fin ya hayan pasado.

## Flujo Principal

1. El sistema evalúa el vencimiento de las reservas activas al consultarlas (por Residente, por Administrador o por espacio/fecha).
2. El sistema cambia a estado FINALIZADA aquellas reservas cuya hora de fin ya transcurrió.

## Postcondiciones

- Las reservas vencidas quedan en estado FINALIZADA.
- El horario correspondiente deja de bloquear el espacio.
- Las reservas finalizadas quedan habilitadas para recibir observaciones (CU-05).

---

# CU-09 Verificar Mora del Residente

## Descripción

Antes de confirmar una reserva, valida contra el módulo de Finanzas si el Residente tiene deudas en mora, para bloquear la reserva en ese caso.

## Actor Principal

Módulo Finanzas (GRA)

## Precondiciones

- Se está procesando la creación de una reserva (CU-01).
- El módulo de Finanzas debe estar disponible (integrado vía fachada).

## Flujo Principal

1. El sistema obtiene el número de documento del Residente a partir de su identificador de usuario (módulo GRB).
2. El sistema consulta al módulo de Finanzas (GRA) si el Residente tiene deudas en mora.
3. Si el Residente tiene deudas en mora, el sistema informa al proceso de reserva para que la rechace.

## Postcondiciones

- La creación de la reserva queda condicionada al resultado de esta verificación.

---

# CU-10 Registrar Deuda por Reserva

## Descripción

Registra en el módulo de Finanzas el cobro correspondiente a una reserva o a una multa por infracción.

## Actor Principal

Módulo Finanzas (GRA)

## Precondiciones

- Se ha creado una reserva con costo asociado (CU-01) o se ha solicitado una multa (CU-07).

## Flujo Principal

1. El sistema calcula el monto a cobrar: la tarifa vigente del espacio (para reservas) o el monto fijo de la multa (para infracciones).
2. El sistema envía a Finanzas (GRA) el registro de la deuda, indicando cédula del Residente, tipo (RESERVA o MULTA), descripción y fecha máxima de pago (7 días desde su creación).

## Postcondiciones

- La deuda queda registrada en el módulo de Finanzas, asociada al Residente correspondiente.
