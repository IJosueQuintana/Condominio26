# Casos de Uso - Módulo de Pagos y Finanzas

## Requisito: generarReporteDePagosRealizados

El sistema permitirá generar un reporte de los pagos realizados de un residente.

## Datos del Caso de Uso

- **Actor:** Administrador
- **Entradas:** identificador del residente
- **Salidas:** reporte de pagos realizados y mensaje

## Escenario Básico

1. El caso de uso inicia con el Administrador ingresando el identificador del residente.
2. El Sistema verifica que exista un residente asociado al identificador ingresado.
3. SI existe un residente asociado al identificador ingresado ENTONCES el Sistema verifica que existan pagos realizados asociados al residente.
4. SI existen pagos realizados asociados al residente ENTONCES el Sistema genera un reporte con el identificador del residente, número de cédula de identidad, nombre completo, correo electrónico, valor pagado, descripción del pago, fecha del pago y método de pago.
5. El caso de uso termina con el Sistema mostrando el reporte generado y emitiendo el mensaje "Reporte de pagos generado correctamente".

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Administrador ingresando el identificador del residente.
2. El Sistema verifica que exista un residente asociado al identificador ingresado.
3. SI NO existe un residente asociado al identificador ingresado ENTONCES el Sistema emite el mensaje "No existe un residente asociado al identificador ingresado".
4. El caso de uso termina sin generar el reporte de pagos realizados.

### Escenario Alterno 2

1. El caso de uso inicia con el Administrador ingresando el identificador del residente.
2. El Sistema verifica que exista un residente asociado al identificador ingresado.
3. SI existe un residente asociado al identificador ingresado ENTONCES el Sistema verifica que existan pagos realizados asociados al residente.
4. SI NO existen pagos realizados asociados al residente ENTONCES el Sistema emite el mensaje "No existen pagos realizados asociados al residente".
5. El caso de uso termina sin generar el reporte de pagos realizados.

---

## Requisito: enviarRecordatorioDeDeudaPendiente

El sistema permitirá enviar un recordatorio al correo electrónico de un residente cuando tenga obligaciones de pago en estado "PENDIENTE" o "EN MORA".

## Datos del Caso de Uso

- **Actor:** Sistema Automático
- **Entradas:** obligación de pago en estado "PENDIENTE" o "EN MORA"
- **Salidas:** recordatorio de deuda pendiente y mensaje

## Escenario Básico

1. El caso de uso inicia cuando el Sistema identifica una obligación de pago en estado "PENDIENTE" o "EN MORA".
2. El Sistema verifica que la obligación de pago esté asociada a un residente.
3. SI la obligación de pago está asociada a un residente ENTONCES el Sistema verifica que el residente tenga un correo electrónico registrado.
4. SI el residente tiene un correo electrónico registrado ENTONCES el Sistema genera un recordatorio con el nombre completo del residente, el identificador del departamento y el saldo pendiente total.
5. El caso de uso termina con el Sistema enviando el recordatorio y emitiendo el mensaje "Recordatorio de deuda pendiente enviado correctamente".

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia cuando el Sistema identifica una obligación de pago en estado "PENDIENTE" o "EN MORA".
2. El Sistema verifica que la obligación de pago esté asociada a un residente.
3. SI la obligación de pago NO está asociada a un residente ENTONCES el Sistema emite el mensaje "No existe un residente asociado a la obligación de pago".
4. El caso de uso termina sin enviar el recordatorio de deuda pendiente.

### Escenario Alterno 2

1. El caso de uso inicia cuando el Sistema identifica una obligación de pago en estado "PENDIENTE" o "EN MORA".
2. El Sistema verifica que la obligación de pago esté asociada a un residente.
3. SI la obligación de pago está asociada a un residente ENTONCES el Sistema verifica que el residente tenga un correo electrónico registrado.
4. SI el residente NO tiene un correo electrónico registrado ENTONCES el Sistema emite el mensaje "No se puede enviar el recordatorio porque el residente no tiene un correo electrónico registrado".
5. El caso de uso termina sin enviar el recordatorio de deuda pendiente.

---

## Requisito: registrarEntidadBancaria

El sistema permitirá a un Administrador registrar una entidad bancaria.

## Datos del Caso de Uso

- **Actor:** Administrador
- **Entradas:** nombre de la entidad bancaria y número de cuenta
- **Salidas:** mensaje

## Escenario Básico

1. El caso de uso inicia con el Administrador ingresando la información de la entidad bancaria.
2. El Sistema valida que la información ingresada cumpla con los datos requeridos.
3. SI la información ingresada es válida ENTONCES el Sistema verifica que no exista una entidad bancaria registrada con el número de cuenta ingresado.
4. SI no existe una entidad bancaria registrada con el número de cuenta ingresado ENTONCES el Sistema registra la entidad bancaria.
5. El caso de uso termina con el Sistema emitiendo el mensaje "Entidad bancaria registrada correctamente".

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Administrador ingresando la información de la entidad bancaria.
2. El Sistema valida que la información ingresada cumpla con los datos requeridos.
3. SI la información ingresada NO es válida ENTONCES el Sistema emite el mensaje "La información de la entidad bancaria no es válida".
4. El caso de uso termina sin registrar la entidad bancaria.

### Escenario Alterno 2

1. El caso de uso inicia con el Administrador ingresando la información de la entidad bancaria.
2. El Sistema valida que la información ingresada cumpla con los datos requeridos.
3. SI la información ingresada es válida ENTONCES el Sistema verifica que no exista una entidad bancaria registrada con el número de cuenta ingresado.
4. SI ya existe una entidad bancaria registrada con el número de cuenta ingresado ENTONCES el Sistema emite el mensaje "Ya existe una entidad bancaria registrada con el número de cuenta ingresado".
5. El caso de uso termina sin registrar nuevamente la entidad bancaria.

---

## Requisito: consultarPagosEfectuados

El sistema permitirá consultar los pagos efectuados durante una fecha de inicio y una fecha de fin.

## Datos del Caso de Uso

- **Actores:** Administrador y Residente
- **Entradas:** fecha de inicio y fecha de fin
- **Salidas:** pagos efectuados y mensaje

## Escenario Básico

1. El caso de uso inicia con el Administrador o el Residente ingresando la fecha de inicio y la fecha de fin.
2. El Sistema valida que las fechas ingresadas tengan un formato válido.
3. SI las fechas ingresadas tienen un formato válido ENTONCES el Sistema verifica que la fecha de fin sea mayor o igual a la fecha de inicio.
4. SI el rango de fechas es válido ENTONCES el Sistema verifica que existan pagos efectuados dentro del rango indicado.
5. SI existen pagos efectuados dentro del rango indicado ENTONCES el Sistema muestra los pagos efectuados correspondientes al actor que inició el caso de uso.
6. El caso de uso termina con el Sistema emitiendo el mensaje "Pagos efectuados consultados correctamente".

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Administrador o el Residente ingresando la fecha de inicio y la fecha de fin.
2. El Sistema valida que las fechas ingresadas tengan un formato válido.
3. SI alguna de las fechas ingresadas NO tiene un formato válido ENTONCES el Sistema emite el mensaje "Las fechas ingresadas no son válidas".
4. El caso de uso termina sin mostrar pagos efectuados.

### Escenario Alterno 2

1. El caso de uso inicia con el Administrador o el Residente ingresando la fecha de inicio y la fecha de fin.
2. El Sistema valida que las fechas ingresadas tengan un formato válido.
3. SI las fechas ingresadas tienen un formato válido ENTONCES el Sistema verifica que la fecha de fin sea mayor o igual a la fecha de inicio.
4. SI la fecha de fin es menor que la fecha de inicio ENTONCES el Sistema emite el mensaje "El rango de fechas ingresado no es válido".
6. El caso de uso termina sin mostrar pagos efectuados.

### Escenario Alterno 3

1. El caso de uso inicia con el Administrador o el Residente ingresando la fecha de inicio y la fecha de fin.
2. El Sistema valida que las fechas ingresadas tengan un formato válido.
3. SI las fechas ingresadas tienen un formato válido ENTONCES el Sistema verifica que la fecha de fin sea mayor o igual a la fecha de inicio.
4. SI el rango de fechas es válido ENTONCES el Sistema verifica que existan pagos efectuados dentro del rango indicado.
5. SI NO existen pagos efectuados dentro del rango indicado ENTONCES el Sistema emite el mensaje "No existen pagos efectuados dentro del rango de fechas indicado".
6. El caso de uso termina sin mostrar pagos efectuados.
