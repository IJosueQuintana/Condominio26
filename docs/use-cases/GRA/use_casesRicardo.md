## Requisito: modificarFechaMaximaDePagoDeUnaDeuda
El sistema permitirá modificar la fecha máxima de pago de una deuda.

**Escenario Básico:**

1. El caso de uso inicia con el Administrador ingresando el número de deuda.
2. El Sistema verifica el número de deuda ingresado.
3. SI el número de deuda existe ENTONCES el Sistema recupera la deuda identificada por el número de deuda.
4. El Administrador ingresa la nueva fecha de pago.
5. El Sistema valida la nueva fecha de pago ingresada.
6. SI la fecha de pago es válida ENTONCES el Sistema modifica la fecha máxima de pago de la deuda.
7. El caso de uso termina con el Sistema emitiendo el mensaje "Fecha máxima de pago modificada con éxito".

**Escenario(s) alternativo(s):**

**Escenario alternativo 1:**

1. El caso de uso inicia con el Administrador ingresando el número de deuda.
2. El Sistema verifica el número de deuda ingresado.
3. SI el número de deuda NO existe ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje "Deuda no registrada en el sistema".

**Escenario alternativo 2:**

1. El caso de uso inicia con el Administrador ingresando el número de deuda.
2. El Sistema verifica el número de deuda ingresado.
3. SI el número de deuda existe ENTONCES el Sistema recupera la deuda identificada por el número de deuda.
4. El Administrador ingresa la nueva fecha de pago.
5. El Sistema valida la nueva fecha de pago ingresada.
6. SI la fecha de pago NO es válida ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje "La fecha ingresada no es válida, asegúrese que sea mayor que la fecha máxima de pago actual".


## Requisito: consultarDeudasPendientesPorNúmeroDeCédulaDeIdentidad
El sistema permitirá consultar las deudas pendientes de un residente mediante su número de cédula

**Escenario Básico:**

1. El caso de uso inicia con el Administrador ingresando el número de cédula de identidad.
2. El Sistema verifica el número de cédula de identidad.
3. SI el número de cédula de identidad es válido ENTONCES el Sistema busca deudas con estado pendiente asociadas al número de cédula ingresado.
4. SI existen deudas con estado pendiente ENTONCES el Sistema genera un reporte con todos los detalles de las deudas encontradas.
5. El caso de uso termina con el Sistema mostrando el reporte generado.

**Escenario(s) alternativo(s):**

**Escenario alternativo 1:**

1. El caso de uso inicia con el Administrador ingresando el número de cédula de identidad.
2. El Sistema verifica el número de cédula de identidad.
3. SI el número de cédula de identidad NO es válido ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje "El número de cédula ingresado no se encuentra registrado".

**Escenario alternativo 2:**

2. El Sistema verifica el número de cédula de identidad.
3. SI el número de cédula de identidad es válido ENTONCES el Sistema busca deudas con estado pendiente asociadas al número de cédula ingresado.
4. SI existen deudas con estado pendiente ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje "Residente sin deudas pendientes".


## Requisito: definirValorMensualDeAlicuotas
El sistema permitirá definir el valor mensual a obtener de las alicuotas

**Escenario Básico:**

1. El caso de uso inicia con el Administrador ingresando el nuevo valor de la alícuota.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado es válido ENTONCES el Sistema cambia el valor de la alícuota.
4. El caso de uso termina con el Sistema emitiendo el mensaje “Valor de la alícuota establecido correctamente”.

**Escenario(s) alternativo(s):**

**Escenario alternativo 1:**

1. El caso de uso inicia con el Administrador ingresando el nuevo valor de la alícuota.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado NO es válido ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje “El valor de la alícuota ingresado no es válido, asegúrese que haya ingresado un número mayor a 0 con hasta 2 cifras decimales”.


## Requisito: definirPagoDeAlicuotasDeFormaMensual
El sistema permitirá definir el pago de  alícuotas de forma mensual (como pago recurrente).

**Escenario Básico:**

1. El caso de uso inicia con el Administrador ingresando el número de meses de alícuota.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado es válido ENTONCES el Administrador ingresa el día del mes para el pago.
4. El Sistema valida el valor ingresado.
5. Si el valor ingresado es válido ENTONCES el Sistema calcula las fechas de pago de cada alícuota.
6. El caso de uso termina con el sistema definiendo las deudas por concepto de alícuota con su valor a pagar y fecha límite.

**Escenario(s) alternativo(s):**

**Escenario alternativo 1:**

1. El caso de uso inicia con el Administrador ingresando el número de meses de alícuota.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado NO es válido ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje “El número de meses ingresado no es válido, asegúrese que sea un número entero mayor a 0”.

**Escenario alternativo 2:**

1. El caso de uso inicia con el Administrador ingresando el número de meses de alícuota.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado es válido ENTONCES el Administrador ingresa el día del mes para el pago.
4. El Sistema valida el valor ingresado.
5. Si el valor ingresado NO es válido ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje “El día ingresado no es válido, asegúrese que haya ingresado un número entero mayor que 0 y menor que 28”.