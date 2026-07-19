

# Módulo de Finanzas y Pagos GRA

## Índice

### Casos de uso Presidente

- [registrarDeuda](#requisito-registrardeuda)
- [registrarPagoEfectivoTransferenciaResidente](#requisito-registrarpagoefectivotransferenciaresidente)
- [modificarFechaMaximaDePagoDeUnaDeuda](#requisito-modificarfechamaximadepagodeunadeuda)
- [definirValorMensualDeAlicuotas](#requisito-definirvalormensualdealicuotas)
- [generarReporteDePagosRealizados](#requisito-generarreportedepagosrealizados)
- [registrarEntidadBancaria](#requisito-registrarentidadbancaria)
- [eliminarDeuda](#requisito-eliminarDeuda)
- [registrarPagosCondominio](#requisito-registrarpagoscondominio)
- [generarReporteGastos](#requisito-generarReportedeGastos)
- [generarReporteRendicionCuentas](#requisito-generarReporteRendicionCuentas)



### Casos de uso Residente
- [pagarDeuda](#requisito-pagardeuda)
- [solicitarPagoEnCuotas](#requisito-solicitarpagoencuotas)
- [generarCertificadoDeNoDeudor](#requisito-generarcertificadodenodeudor)
- [consultarPagosEfectuados](#requisito-consultarpagosefectuados)



### Casos de uso compartidos

- [consultarDeuda](#requisito-consultardeuda)
- [consultarReporteRendicionCuentas](#requisito-consultarreporterendicioncuentas)


### Casos de uso del Sistema

- [registrarDeudaAlicuotaMensual](#requisito-registrardeudamensual)
- [enviarRecordatorioDeDeudaPendiente](#requisito-enviarrecordatoriodedeudapendiente)
- [registrarMoraDeuda](#requisito-registrarmoradeuda)
- [notificarReporteRendicionCuentas](#requisito-notificarreporterendicioncuentas)


---


# Requisito: registrarDeuda

El sistema permitirá registrar una deuda para un Residente

## Datos del Caso de Uso

- **Entradas:** número de cédula de identidad del residente que cumple el algoritmo de validacion de cedula Ecuatoriana, motivo de la deuda (ALICUOTA, MULTA, RESERVA), fecha maxima de pago mayor a la fecha actual, descripcion: caracteres del alfabeto y " ", ".", ",", "ñ", valor de la deuda, flotante de 2 decimales.
- **Salidas:** mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando el número de cedula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad proporcionado
3. Si el Residente existe, entonces el Presidente/Administrador ingresa el motivo de la deuda
4. El Presidente/Administrador ingresa la fecha máxima de pago
5. El Presidente/Administrador ingresa la descripcion
6. El Presidente/Administrador ingresa el valor de la deuda
7. El Sistema valida que todos los datos tengan un formato válido
8. Si todos los datos tienen un formato válido, entonces el sistema verifica que el motivo de la deuda sea "ALICUOTA", "MULTA" O "RESERVA"
9. Si el motivo de la deuda es ALICUOTA, entonces el Sistema consulta el valor mensual de alícuotas, el tamaño del departamento donde vive el Residente, el tamaño del condomio, y calcula el valor de la deuda por alicuota.
10. El caso de uso finaliza con el Sistema emitiendo el mensaje: "Deuda por motivo de alicuota con el valor de valorDeuda registrada exitosamente para el residente nombreResidente"


## Escenarios Alternos

### Escenario Alterno 1
1. El caso de uso inicia con el Presidente/Administrador ingresando el número de cedula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad proporcionado
3. Si el Residente existe, entonces el Presidente/Administrador ingresa el motivo de la deuda
4. El Presidente/Administrador ingresa la fecha máxima de pago
5. El Presidente/Administrador ingresa la descripcion
6. El Presidente/Administrador ingresa el valor de la deuda
7. El Sistema valida que todos los datos tengan un formato válido
8. Si todos los datos tienen un formato válido, entonces el sistema verifica que el motivo de la deuda sea "ALICUOTA", "MULTA" O "RESERVA"
9. Si el motivo de la deuda es MULTA, el caso de uso finaliza con Sistema emitiendo el mensaje: "Deuda por motivo de multa con el valor de valorDeuda registrada exitosamente para el residente nombreResidente"

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando el número de cedula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad proporcionado
3. Si el Residente existe, entonces el Presidente/Administrador ingresa el motivo de la deuda
4. El Presidente/Administrador ingresa la fecha máxima de pago
5. El Presidente/Administrador ingresa la descripcion
6. El Presidente/Administrador ingresa el valor de la deuda
7. El Sistema valida que todos los datos tengan un formato válido
8. Si todos los datos tienen un formato válido, entonces el sistema verifica que el motivo de la deuda sea "ALICUOTA", "MULTA" O "RESERVA"
9. Si el motivo de la deuda es RESERVA, el caso de uso finaliza con Sistema emitiendo el mensaje: "Deuda por motivo de reserva con el valor de valorDeuda registrada exitosamente para el residente nombreResidente"

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando el número de cedula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad proporcionado
3. Si el Residente NO existe, entonces el Sistema emite el mensaje "No existe un cliente con el número de cédula de identidad proporcionada"
4. El caso de uso finaliza sin registrar una nueva deuda


### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando el número de cedula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad proporcionado
3. Si el Residente existe, entonces el Presidente/Administrador ingresa el motivo de la deuda
4. El Presidente/Administrador ingresa la fecha máxima de pago
5. El Presidente/Administrador ingresa la descripcion
6. El Presidente/Administrador ingresa el valor de la deuda
7. El Sistema valida que todos los datos tengan un formato válido
8. Si los datos NO tienen un formato válido, entonces el Sistema emite el mensaje de error correspondiente
9. El caso de uso finaliza sin que se registre una nueva deuda.

---



# Requisito: registrarPagoEfectivoTransferenciaResidente

## Datos del Caso de Uso

- **Entradas:** idDeuda
- **Salidas:** mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda.
3. SI existe una Deuda con el idDeuda ENTONCES el Sistema verifica el estado de la Deuda.
4. SI el estado de la Deuda es ‘EN PROCESO’ ENTONCES el Presidente/Administrador cambia el estado de la Deuda a ‘PAGADA’.
5. El Sistema muestra el mensaje “Pago registrado exitosamente”.
6. El caso de uso finaliza con el cambio de estado de la Deuda a 'PAGADA' y registrando el Pago del Residente.

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda.
3. SI NO existe una Deuda con el idDeuda ENTONCES el Sistema muestra el mensaje “No existe una deuda con el identificador proporcionado”.
4. El caso de uso termina sin registrar el pago.

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda.
3. SI existe una Deuda con el idDeuda ENTONCES el Sistema verifica el estado de la Deuda.
4. SI el estado de la Deuda es ‘PAGADA’ ENTONCES el Sistema emite el mensaje “Esta deuda ya ha sido pagada”.
5. El caso de uso termina sin registrar el pago.

---


# Requisito: modificarFechaMaximaDePagoDeUnaDeuda
El sistema permitirá modificar la fecha máxima de pago de una deuda.

## Datos del Caso de Uso

- **Entradas:** idDeuda Y nueva fecha máxima de pago: mayor a la fecha actual y mayor a la fecha de pago actual de la deuda
- **Salidas:** mensaje

## Escenario Básico:

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda ingresado.
3. SI existe una deuda ENTONCES el El Presidente/Administrador ingresa la nueva fecha máxima de pago.
4. El Sistema verifica que la nueva fecha máxima de pago sea mayor que la fecha máxima de pago actual registrada en la deuda.
5. Si la nueva fecha máxima de pago es mayor que la fecha máxima de pago actual registrada en la deuda, entonces el Sistema valida que la nueva fecha máxima de pago sea mayor a la fecha actual.
6. Si la nueva fecha máxima de pago es mayor a la fecha actual, entonces el Sistea modifica la fecha máxima de pago de la deuda
7. El caso de uso termina con el Sistema emitiendo el mensaje "Fecha máxima de pago modificada con éxito".

## Escenario(s) alternativo(s):

### Escenario alternativo 1:

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda ingresado.
3. SI existe una Deuda ENTONCES el El Presidente/Administrador ingresa la nueva fecha máxima de pago.
4. El Sistema verifica que la nueva fecha máxima de pago sea mayor que la fecha máxima de pago actual registrada en la deuda.
5. Si la nueva fecha máxima de pago NO es mayor que la fecha máxima de pago actual registrada en la deuda, entonces el Sistema emite el mensaje "La nueva fecha máxima de pago debe ser mayor a la fecha de pago actual de la deuda"
6. El Caso de uso termina sin que el Sistema modifique la fecha máxima de pago de una Deuda

### Escenario alternativo 2:

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda.
2. El Sistema verifica el idDeuda ingresado.
3. SI existe una deuda ENTONCES el El Presidente/Administrador ingresa la nueva fecha máxima de pago.
4. El Sistema verifica que la nueva fecha máxima de pago sea mayor que la fecha máxima de pago actual registrada en la deuda.
5. Si la nueva fecha máxima de pago es mayor que la fecha máxima de pago actual registrada en la deuda, entonces el Sistema valida que la nueva fecha máxima de pago sea mayor a la fecha actual.
6. Si la nueva fecha máxima de pago NO es mayor que la fecha actual, entonces el Sistema emite el mensaje "La nueva fecha máxima de pago debe ser mayor a la fecha actual"
7. El caso de usa finaliza sin que el Sistema actualice la fecha máxima de pago de una Deuda

---

# Requisito: definirValorMensualDeAlicuotas
El sistema permitirá definir el valor mensual a obtener de las alicuotas

## Datos del Caso de Uso

- **Entradas:** valorMensualEsperadoPorAlicuotas, flotante de 2 decimales
- **Salidas:** mensaje

## Escenario Básico:

1. El caso de uso inicia con el Presidente/Administrador  ingresando el nuevo valor mensual esperado por alicuotas.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado es válido ENTONCES el Sistema cambia el valor de la alícuota.
4. El caso de uso termina con el Sistema emitiendo el mensaje “El valor mensual esperado de alicuotas se registró correctamente”.

## Escenario(s) alternativo(s):

### Escenario alternativo 1:

1. El caso de uso inicia con el Presidente/Administrador  ingresando el nuevo valor mensual esperado por alicuotas.
2. El Sistema valida el valor ingresado.
3. SI el valor ingresado NO es válido ENTONCES el caso de uso termina con el Sistema emitiendo el mensaje “El valor mensual esperado de alicuotas ingresado no es válido, asegúrese que haya ingresado un número mayor a 0 con 2 decimales”.

---

# Requisito: generarReporteDePagosRealizados

El sistema permitirá generar un reporte financiero de los pagos realizados por todos los residentes

## Datos del Caso de Uso

- **Entradas:** fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy
- **Salidas:** reporte de pagos realizados y mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema verifica que existan pagos efectuados por los Residentes  durante el periodo de tiempo indicado.
8. Si existen Pagos efectuados por los Residentes entre el periodo de tiempo indicado entonces El Sistema consulta todos los pagos efectuados por todos los Residentes durante el periodo de tiempo indicado
9. El Sistema genera el reporte donde cada registro contiene: número de cédula de identidad del residente asociado al pago, valor del pago y motivo y muestra el total de valor del pago de multas, alicuotas y reservas.
10. El caso de uso termina con el Sistema mostrando el reporte y emitiendo el mensaje "Reporte generado correctamente".


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas NO tienen un formato valido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY"

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
6. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados.

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
7. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados


### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor que la fecha actual, entonces el Sistema emite el mensaje " La fecha de inicio tiene que ser menor o igual a la fecha actual"

### Escenario Alterno 5 
1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema verifica que existan pagos efectuados por los Residentes  durante el periodo de tiempo indicado.
8. Si NO existen Pagos efectuados por los Residentes entre el periodo de tiempo indicado entonces El Sistema emite el mensaje "No existen Pagos entre el fechaInicio y el fechaFin"
9. El Caso de uso termina sin que el Sistema genere el reporte de todos los pagos realizados por los Residentes.
---

# Requisito: registrarEntidadBancaria

El sistema permitirá registrar una entidad bancaria.

## Datos del Caso de Uso

- **Entradas:** nombre de la entidad bancaria: cadena de hasta 100 caracteres que acepta los caracteres del alfabeto y la letra ñ, número de cuenta: cadena que acepta solo numeros , número de cédula de identidad del titular de la cuenta: cadena de 10 digitos, tipo de cuenta: cadena que acepta los valores (AHORROS, CORRIENTE), correo electrónico del titular la cuenta que cumple las validaciones básicas de email de la REGEX: /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
- **Salidas:** mensaje

## Escenario Básico

1. El caso de uso inicia con el Administrador ingresando el número de cuenta de la entidad bancaria
2. El Administrador ingresa el nombre de la entidad bancaria.
3. El Administrador ingresa el número de cédula de identidad del titular de la cuenta
4. El Administrador ingresa el tipo de cuenta
5. El Administrador ingresa el correo electrónico del titular de la cuenta
6. El Sistema valida que la información ingresada tenga formato valido.
7. SI la información ingresada es válida ENTONCES el Sistema verifica que no exista una entidad bancaria registrada con el número de cuenta ingresado.
8. SI no existe una entidad bancaria registrada con el número de cuenta ingresado ENTONCES el Sistema registra la entidad bancaria.
9. El caso de uso termina con el Sistema emitiendo el mensaje "Entidad bancaria registrada correctamente".

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Administrador ingresando el número de cuenta de la entidad bancaria
2. El Administrador ingresa el nombre de la entidad bancaria.
3. El Administrador ingresa el número de cédula de identidad del titular de la cuenta
4. El Administrador ingresa el tipo de cuenta
5. El Administrador ingresa el correo electrónico del titular de la cuenta
6. El Sistema valida que la información ingresada tenga formato valido.
3. SI la información ingresada NO es válida ENTONCES el Sistema emite el mensaje de error correspondiente
4. El caso de uso termina sin registrar la entidad bancaria.

### Escenario Alterno 2

1. El caso de uso inicia con el Administrador ingresando el número de cuenta de la entidad bancaria
2. El Administrador ingresa el nombre de la entidad bancaria.
3. El Administrador ingresa el número de cédula de identidad del titular de la cuenta
4. El Administrador ingresa el tipo de cuenta
5. El Administrador ingresa el correo electrónico del titular de la cuenta
6. El Sistema valida que la información ingresada tenga formato valido.
7. SI la información ingresada es válida ENTONCES el Sistema verifica que no exista una entidad bancaria registrada con el número de cuenta ingresado.
8. SI ya existe una entidad bancaria registrada con el número de cuenta ingresado ENTONCES el Sistema emite el mensaje "Ya existe una entidad bancaria registrada con el número de cuenta ingresado".
9. El caso de uso termina sin registrar nuevamente la entidad bancaria.

---


# Requisito: eliminarDeuda

## Datos del Caso de Uso

- **Entradas:** idDeuda
- **Salidas:** mensaje de confirmacion

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando el idDeuda
2. El Sistema verifica que exista una deuda con el identificador proporcionado
3. Si existe una deuda con el identificador proporcionado, entonces el Sistema cambia el estado de la deuda a "ELIMINADA"
4. El caso de uso finaliza con el Sistema emitiendo el mensaje "Deuda Eliminada Exitosamente"


## Escenarios Alternativos

### Escenario alternativo 1**
1. El caso de uso inicia con el Presidente/Administrador ingresando el identificador de la deuda
2. El Sistema verifica que exista una deuda con el identificador proporcionado
3. Si NO existe una deuda con el identificador proporcionado, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje " No existe una deuda con el identificador proporcionado.

---

# Requisito: registrarPagosCondominio

## Datos del Caso de Uso

- **Entradas:** fecha de pago: menor o igual que la fecha actual, valor pagado: valor decimal de 2 decimales , motivo del pago: cadena que acepta solo los valores (SERVICIO BASICO, SUELDOS, OTROS) , descripcion: cadena de hasta 200 caracteres que acepta valores del alfabeto, ñ, ",", " " y "." o cadena de hasta 200 caracteres si el motivo del pago es SERVICIO BASICO
- **Salidas:** mensaje de confirmacion

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de cuando se realizó el pago
2. El Presidente/Administrador ingresa el valor pagado
3. El Presidente/Administrador ingresa el motivo del pago
4. El Sistema valida que la fecha del pago sea menor o igual a la fecha actual, que el valor pagado sea un decimal con 2 decimales y que el motivo del pago sea SERVICIO BÁSICO, SUELDOS u OTROS.
5. Si los datos tienen un formato válido, entonces el Presidente/Administrador ingresa la descripción del pago
6. Si el motivo del pago es SERVICIO BÁSICO, el Sistema valida que la descripción sea AGUA, LUZ, TELEFONO o INTERNET
7. Si el motivo del pago es SUELDOS u OTROS, el Sistema valida que la descripción tenga un formato válido
8. Si el motivo del pago es correcto, entonces el Sistema emite el mensaje "Pago externo guardado exitosamente"
9. El caso de uso finaliza con el Sistema guardando el Gasto.


## Escenarios Alternativos

### Escenario alternativo 1**
1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de cuando se realizó el pago
2. El Presidente/Administrador ingresa el valor pagado
3. El Presidente/Administrador ingresa el motivo del pago
4. El Sistema valida que la fecha del pago sea menor o igual a la fecha actual, que el valor pagado sea un decimal con 2 decimales y que el motivo del pago sea SERVICIO BÁSICO, SUELDOS u OTROS.
5. Si los datos NO tienen un formato válido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje de error correspondiente y sin guardar el Gasto.

### Escenario alternativo 2**
1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de cuando se realizó el pago
2. El Presidente/Administrador ingresa el valor pagado
3. El Presidente/Administrador ingresa el motivo del pago
4. El Sistema valida que la fecha del pago sea menor o igual a la fecha actual, que el valor pagado sea un decimal con 2 decimales y que el motivo del pago sea SERVICIO BÁSICO, SUELDOS u OTROS.
5. Si los datos tienen un formato válido, entonces el Presidente/Administrador ingresa la descripción del pago
6. Si el motivo del pago es SERVICIO BÁSICO, el Sistema valida que la descripción sea AGUA, LUZ, TELEFONO o INTERNET
7. Si el motivo del pago es SUELDOS u OTROS, el Sistema valida que la descripción tenga un formato válido
8. Si el motivo del pago NO es correcto, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje de error correspondiente y sin guardar el Gasto.

---


# Requisito: generarReportedeGastos

El sistema permitirá generar un reporte financiero de los gastos (pagos externos efectuados del condominio).

## Datos del Caso de Uso

- **Entradas:** fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy
- **Salidas:** reporte de gastos realizados y mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema consulta todos los Gastos efectuados durante el periodo de tiempo indicado.
7. El Sistema calcula el total de gastos por SERVICIOS BÁSICOS desglosado por total de AGUA, LUZ, TELEFONO, INTERNET, el total de gastos por SUELDOS y el total de gastor por motivo de OTROS.
8. El Sistema genera el reporte donde cada registro contiene: , valor del pago, motivo y descripcion, ademas, muestra el total de valor de gastos calculados en el paso 7 del presente caso de uso.
9. El caso de uso termina con el Sistema mostrando el reporte y emitiendo el mensaje "Reporte generado correctamente".


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrado ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas NO tienen un formato valido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY"

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
6. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados.

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
7. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados


### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor que la fecha actual, entonces el Sistema emite el mensaje " La fecha de inicio tiene que ser menor o igual a la fecha actual"

---


# Requisito: generarReporteRendicionCuentas

El sistema permitirá generar un reporte de rendición de cuentas disponible para cualquier Residente del condominio.

## Datos del Caso de Uso

- **Entradas:** fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy. Observaciones del reporte de rendición cuentas que solo admite caracteres del alfabeto, espacios en blanco y numeros
- **Salidas:** reporte de rendición de cuentas y mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica que no exista un reporte de rendición de cuentas generado entre el mismo periodo de tiempo.
8. Si NO existe un reporte de rendición de cuentas generado entre el mismo periodo de tiempo, el Sistema  consulta todos los Gastos del condominio realizados durante ese periodo y todos los Pagos realizados por Residentes durante ese periodo.
9. El Sistema calcula el total de gastos por SERVICIOS BÁSICOS desglosado por total de AGUA, total de LUZ, total TELEFONO, total INTERNET, el total por SUELDOS y el total por OTROS.
10. El Sistema calcula el total de ingresos por multas, total por alicuotas y total por reservas.
11. El Sistema genera el reporte de rendición de cuentas donde se muestra: TOTAL DE GASTOS desglosado por el total por SERVICIO BÁSICO, SUELDOS Y OTROS, y el TOTAL DE INGRESOS desglosado por MULTAS, ALICUOTAS, RESERVAS, muestra el balance entre ingresos y gastos y la fecha de inicio y de fin al que hace referencia ese reporte.
12. El Presidente/Administrador ingresa las observaciones del reporte de rendicion de cuentas
13. El Sistema valida las observaciones del reporte de rendición de cuentas
14. Si las observaciones tienen formato válido, entonces el caso de uso termina con el Sistema mostrando el reporte y emitiendo el mensaje "Reporte de Rendición de Cuentas generado exitosamente, disponible para la consulta de los residentes"
15. El Sistema notifica a los Residentes que existe un nuevo reporte de rendición de cuentas disponible para consulta
16. El caso de uso termina con el Sistema mostrando el reporte y emitiendo el mensaje "Reporte generado y notificado a los Residentes de forma correcta".


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica que no exista un reporte de rendición de cuentas generado entre el mismo periodo de tiempo.
7. Si existe un reporte de rendición de cuentas generado entre el mismo periodo de tiempo, el Sistema emite el mensaje "Ya se generó un reporte de rendición de cuentas para las fechas especificadas, puede consultarlo en cualquier momento".
8. El caso de uso finaliza sin que el Sistema genere el reporte de rendición de cuentas.

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrado ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas NO tienen un formato valido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY"

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
6. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados.

### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
7. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados


### Escenario Alterno 5

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor que la fecha actual, entonces el Sistema emite el mensaje " La fecha de inicio tiene que ser menor o igual a la fecha actual"

---



# Requisito: pagarDeuda
El sistema permitirá registrar el pago de una deuda.

## Datos del Caso de Uso

- **Entradas:** id de la deuda, método de pago (EFECTIVO, TRANSFERENCIA O TARJETA), datos de la tarjeta si el método de pago es tarjeta
- **Salidas:** mensaje

### Escenario Básico:**

1. El caso de uso inicia con el Residente ingresando el id de la deuda a pagar
2. El Sistema verifica que exista una deuda con el id ingresado
3. Si la deuda existe, entonces el Sistema muestra el valor de la deuda a pagar y el estado de la deuda
4. El Residente ingresa el método de pago
5. Si el método de pago es EFECTIVO, entonces el Sistema cambia el estado de la deuda a "EN PROCESO"
6. El caso de uso finaliza con el Sistema emitiendo el mensaje "Acerquese a oficinas de contabilidad para efectuar el pago"

## Escenario(s) alternativo(s):**

### Escenario alternativo 1:**

1. El caso de uso inicia con el Residente ingresando el id de la deuda a pagar
2. El Sistema verifica que exista una deuda con el id ingresado
3. Si la deuda existe, entonces el Sistema muestra el valor de la deuda a pagar y el estado de la deuda
4. El Residente ingresa el método de pago
5. Si el método de pago es TRANSFERENCIA, entonces el Sistema cambia el estado de la deuda a "EN PRCCESO"
6. El Sistema consulta los datos bancarios registrados por el Administrador
7. Si existen datos bancarios, entonces el Sistema muestra los datos bancarios
8. El Residente ingresa el comprobante de deposito
9. El caso de uso finaliza con el Sistema "Se revisara el deposito y se actualizara el estado de su deuda en las próximas horas"

### Escenario alternativo 2:**

1. El caso de uso inicia con el Residente ingresando el id de la deuda a pagar
2. El Sistema verifica que exista una deuda con el id ingresado
3. Si la deuda existe, entonces el Sistema muestra el valor de la deuda a pagar y el estado de la deuda
4. El Residente ingresa el método de pago
5. Si el método de pago es TARJETA, entonces el Sistema muestra en pantalla el mensaje "Ingrese los datos de su tarjeta "
6. El Residente ingresa los datos de su tarjeta
7. La Plataforma externa procesa el pago y devuelve la aceptación o no
8. Si el pago fue aceptado, entonces el Sistema cambia el estado de la deuda a "PAGADO"
9. El caso de uso finaliza con el Sistema emitiendo el mensaje "Deuda cancelada exitosamente" y registrando el Pago del Residente

---

# Requisito: solicitarPagoEnCuotas
El Sistema permitirá a un residente solicitar el pago en cuotas.

## Datos del Caso de Uso

- **Entradas:** idDeuda a diferir, número de meses a diferir: valor entero mayor a 2 y menor a 12
- **Salidas:** mensaje

## Escenario Básico:

1. El caso de uso inicia con el Residente ingresando el idDeuda que desea diferir
2. El Residente ingresa el numero de meses a los cuales desea diferir la deuda
3. El Sistema valida el formato del número de meses a los cuales desea difierir la deuda
4. Si el número de meses es mayor a 2 y menor a 12 entonces el Sistema consulta si el Residente tiene deudas en estado "EN MORA" 
5. Si el Residente NO tiene Deudas en estado "EN MORA", entonces el Sistema calcula el valor a pagar por cada cuota según el valor de la Deuda y el número de meses a diferir
6. El sistema genera tantas Deudas como número de meses a diferir con fechas máximas de pago saltadas de un mes en un mes y el valor calculado para cada una de las Deudas
7. El Sistema emite el mensaje "Deuda diferida exitosamente"
8. El caso de uso finaliza con el Sistema mostrando las deudas que hacen referencia a las cuotas. 


## Escenario(s) alternativo(s):**

### Escenario alternativo 1**
1. El caso de uso inicia con el Residente ingresando el idDeuda que desea diferir
2. El Residente ingresa el numero de meses a los cuales desea diferir la deuda
3. El Sistema valida el formato del número de meses a los cuales desea difierir la deuda
4. Si el número de NO meses es mayor a 2 y menor a 12 entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "El numero de meses a diferir la deuda debe ser de almenos 3 y como máximo 12" y sin diferir la deuda

### Escenario alternativo 2**
1. El caso de uso inicia con el Residente ingresando el idDeuda que desea diferir
2. El Residente ingresa el numero de meses a los cuales desea diferir la deuda
3. El Sistema valida el formato del número de meses a los cuales desea difierir la deuda
4. Si el número de meses es mayor a 2 y menor a 12 entonces el Sistema consulta si el Residente tiene deudas en estado "EN MORA" 
5. Si el Residente  tiene Deudas en estado "EN MORA", entonces el Sistema emite el mensaje "No puede ser beneficiario a este beneficio porque tiene deudas en estado EN MORA"
6. El caso de uso finaliza con el Sistema sin diferir la deuda

---

# Requisito: generarCertificadoDeNoDeudor
El sistema permitirá generar un certificado de no deudor.

## Datos del Caso de Uso

- **Entradas:** número de cédula de identidad del Residente: cadena de 10 digitos. Fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy
- **Salidas:** mensaje y cerficado de no deudor

## Escenario Básico:**

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
7. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica si el Residente tiene deudas en estado PENDIENTE o EN MORA dentro del periodo de fechas indicado
8. Si NO existen deudas, entonces el Sistema genera el cerficado que contien el mensaje "Se certifica que el Residente nombreResidente no tiene Deudas Pendientes de pago ni en Mora entre el fechaInicio y fechaFin.
9. El Sistema emite el mensaje "Certificado generado exitosamente"
10. El caso de uso finaliza con el Sistema descargando el certificado de no deudor.

## Escenario(s) alternativo(s):**

### Escenario alternativo 1

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas NO tienen un formato valido, entonces el Sistema emite el mensaje "Fechas con formato invalido. Ambas fechas deben tener el formato "DD/MM/YYYY"
7. El caso de uso finaliza sin generar un certificado de no deudor

### Escenario Alterno 2

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
7. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
8. El caso de uso finaliza sin que el Sistema genere el certificado de no deudor

### Escenario Alterno 3

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
7. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
8. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
9. El caso de uso finaliza sin que el Sistema genere el certificado de no deudor


### Escenario Alterno 4

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
7. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor o igual a la fecha actual, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser menor o igual a la fecha actual.
8. El caso de uso finaliza sin que el Sistema genere el certificado de no deudor

### Escenario Alternativo 5

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad del Residente
2. El Sistema verifica que exista un Residente con el número de cédula de identidad
3. Si el Residente existe, entonces el Administrador/Residente ingresa la fecha de inicio para la consulta de Deudas.
4. El Administrador/Residente ingresa la fecha de fin para la consulta de Deudas.
5. El Sistema valida el formato de las fechas
6. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
7. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica si el Residente tiene deudas en estado PENDIENTE o EN MORA dentro del periodo de fechas indicado
8. Si existen deudas, entonces el Sistema emite el mensaje "El Residente nombreResidente tiene Deudas Pendientes de Pago o EN MORA, no se puede generar el certificado.
9. El caso de uso finaliza sin que el Sistema genere el certificado de no deudor

---

# Requisito: consultarDeuda
El sistema permitirá consultar las deudas de un Residente mediante su número de cédula

## Datos del Caso de Uso

- **Entradas:** número de cédula de identidad del Residente: cadena de 10 digitos
- **Salidas:** deudas pendientes del residnete

## Escenario Básico:**

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad.
2. El Sistema verifica el número de cédula de identidad.
3. SI existe un Residente con el número de cédula de identidad proporcionado, entonces el Sistema consulta todas las deudas en estado "PENDIENTE", "EN PROCESO" y "MORA" asociadas al Residente.
4. Si existen deudas en estado "PENDIENTE", "EN PROCESO", "MORA" entonces el Sistema emite el mensaje "Deudas del Residente"
5. El caso de uso finaliza con el Sistema mostrando las deudas consultadas.


## Escenario(s) alternativo(s):**

### Escenario alternativo 1:**

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad.
2. El Sistema verifica el número de cédula de identidad.
3. SI existe un Residente con el número de cédula de identidad proporcionado, entonces el Sistema consulta todas las deudas en estado "PENDIENTE", "EN PROCESO" y "MORA" asociadas al Residente.
4. Si NO existen deudas en estado "PENDIENTE", "EN PROCESO", "MORA" entonces el Sistema emite el mensaje "El Residente no tiene deudas"
5. El caso de uso finaliza sin que el Sistema muestra algo.

### Escenario alternativo 2:**

1. El caso de uso inicia con el Administrador/Residente ingresando el número de cédula de identidad.
2. El Sistema verifica el número de cédula de identidad.
3. SI NO existe un Residente con el número de cédula de identidad proporcionado, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "No existe un cliente con el número de cédula proporcionado".

---


# Requisito: consultarPagosEfectuados

El sistema permitirá consultar los pagos efectuados durante una fecha de inicio y una fecha de fin de un residente en particular mediante su número de cédula de identidad

El sistema permitirá generar un reporte financiero de los pagos realizados por todos los residentes

## Datos del Caso de Uso

- **Entradas:** fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy . Número de cédula de identidad: cadena de 10 dígitos
- **Salidas:** reporte de pagos realizados y mensaje

## Escenario Básico

1. El caso de uso inicia con el Residente/Administrador/Presidente ingresando la fecha de inicio
2. El Residente/Administrador/Presidente ingresa la fecha de fin
3. El Residente/Administrador/Presidente ingresa el número de cedula de identidad
4. El Sistema valida el formato de las fechas
5. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
6. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
7. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema verifica que exista un Residente con el número de cédula proporcionada.
8. Si existe un Residente con el número de cédula proporcionada, entonces el Sistema verifica que existan pagos efecutados por el Residente durante el periodo de tiempo indicado.
9. Si existen Pagos efectuados por el Residente entre el periodo de tiempo indicado entonces El Sistema consulta todos los pagos efectuados por el Residente durante el periodo de tiempo indicado
10. El Sistema genera el reporte donde cada registro contiene: número de cédula de identidad del residente asociado al pago, valor del pago y motivo y muestra el total de valor del pago de multas, alicuotas y reservas.
11. El caso de uso termina con el Sistema mostrando el reporte y emitiendo el mensaje "Reporte generado correctamente".


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas NO tienen un formato valido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY"

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
6. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados.

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
7. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados


### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor que la fecha actual, entonces el Sistema emite el mensaje " La fecha de inicio tiene que ser menor o igual a la fecha actual"

### Escenario Alterno 5 
1. El caso de uso inicia con el Residente/Administrador/Presidente ingresando la fecha de inicio
2. El Residente/Administrador/Presidente ingresa la fecha de fin
3. El Residente/Administrador/Presidente ingresa el número de cedula de identidad
4. El Sistema valida el formato de las fechas
5. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
6. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
7. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema verifica que exista un Residente con el número de cédula proporcionada.
8. Si NO existe un Residente con el número de cédula proporcionada, entonces el Sistema emite el mensaje "No existe un Residente con la cédula proporcionada"
9. El Caso de uso termina sin que el Sistema genere el reporte de todos los pagos realizados por el Residente.

### Escenario Alterno 6
1. El caso de uso inicia con el Residente/Administrador/Presidente ingresando la fecha de inicio
2. El Residente/Administrador/Presidente ingresa la fecha de fin
3. El Residente/Administrador/Presidente ingresa el número de cedula de identidad
4. El Sistema valida el formato de las fechas
5. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
6. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
7. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor que la fecha actual, entonces el Sistema verifica que exista un Residente con el número de cédula proporcionada.
8. Si existe un Residente con el número de cédula proporcionada, entonces el Sistema verifica que existan pagos efecutados por el Residente durante el periodo de tiempo indicado.
9. Si NO existen Pagos efectuados por el Residente entre el periodo de tiempo indicado entonces El Sistema emite el mensaje "No existen Pagos asociados para el Residente nombreResidente durante fechaInicio y fechaFin"

--- 


# Requisito: consultarReporteRendicionCuentas

El sistema permitirá consultar un reporte de rendicion de cuentas

## Datos del Caso de Uso

- **Entradas:** fecha de inicio: menor a la fecha actual y fecha de fin: menor o igual a la fecha actual y mayor a la fecha de inicio. Ambas en formato dd/mm/yyyy.
- **Salidas:** reporte de rendición de cuentas y mensaje

## Escenario Básico

1. El caso de uso inicia con el Presidente/Administrador/Residente ingresando la fecha de inicio
2. El Presidente/Administrador/Residente  ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica que exista un reporte de rendición de cuentas generado entre el mismo periodo de tiempo.
8. Si existe un reporte de rendición de cuentas generado entre el mismo periodo de tiempo, el Sistema  consulta el reporte de rendición de cuentas
9. El Sistema muestra el reporte de rendicion de cuentas donde se muestra: TOTAL DE GASTOS desglosado por el total por SERVICIO BÁSICO, SUELDOS Y OTROS, y el TOTAL DE INGRESOS desglosado por MULTAS, ALICUOTAS, RESERVAS, muestra el balance entre ingresos y gastos y la fecha de inicio y de fin al que hace referencia ese reporte.
10. El Sistema emite el mensaje "Reporte Consultado exitosamente"
11. El Caso de uso finaliza con el Sistema consultando el Reporte de Rendición de Cuentas


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin es menor o igual a la fecha actual, entonces el Sistema verifica que exista un reporte de rendición de cuentas generado entre el mismo periodo de tiempo.
8. Si NO existe un reporte de rendición de cuentas generado entre el mismo periodo de tiempo, el Sistema emite el mensaje "No existe un reporte de rendición de cuentas para las fechas especificadas, este atento a sus notificaciones".
9. El caso de uso finaliza sin que el Sistema consulte el reporte de rendición de cuentas.

### Escenario Alterno 2

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrado ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas NO tienen un formato valido, entonces el caso de uso finaliza con el Sistema emitiendo el mensaje "Formato de fechas incorrecto, ingrese la fecha en formato DD/MM/YYYY"

### Escenario Alterno 3

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio NO es menor que la fecha actual, entonces el Sistema emite el mensaje "La fecha de inicio tiene que ser menor que la fecha actual"
6. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados.

### Escenario Alterno 4

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio NO es menor que la fecha de fin, entonces el Sistema emite el mensaje "La fecha de fin tiene que ser mayor que la fecha de inicio"
7. El caso de uso finaliza sin que el Sistema genere el reporte de pagos realizados


### Escenario Alterno 5

1. El caso de uso inicia con el Presidente/Administrador ingresando la fecha de inicio
2. El Presidente/Administrador ingresa la fecha de fin
3. El Sistema valida el formato de las fechas
4. Si las fechas tienen un formato valido, entonces el Sistema valida que la fecha de inicio sea menor a la fecha actual
5. Si la fecha de inicio es menor que la fecha actual, entonces el Sistema valida que la fecha de inicio sea menor que la fecha de fin
6. Si la fecha de inicio es menor que la fecha de fin, entonces el Sistema valida que la fecha de fin sea menor o igual a la fecha actual.
7. Si la fecha de fin NO es menor que la fecha actual, entonces el Sistema emite el mensaje " La fecha de inicio tiene que ser menor o igual a la fecha actual"

---


# Requisito: registrarDeudaMensual

El sistema registrará el valor de alicuotas de cada Residente de forma mensual

## Datos del Caso de Uso

- **Entradas:** número de cédula de identidad del Residente del departamento
- **Salidas:** notificación al Residente sobre la deuda

## Escenario Básico

1. El caso de uso inicia con el Sistema consultado la fecha actual
2. Si la fecha actual es el día primero del mes, entonces el Sistema consulta el número de cédula de identidad del Residente
3. El Sistema consulta las dimensiones del departamento donde vive el Residente
4. El Sistema consulta el valor de alicuota esperada por mes definido por el Presidente/Administrador
5. El Sistema calcula el valor de alicutoa específico para el Residente
6. El Sistema genera una deuda con estado "PENDIENTE" de tipo "ALICUOTA" con el valor calculado asociado al Residente.
7. El caso de uso finaliza con el Sistema enviando la notificación al Residente sobre la deuda de alícuota.


## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Sistema consultado la fecha actual
2. Si la fecha actual NO es el día primero del mes, entonces el caso de uso finaliza sin que el Sistema genera deuda por alícuota.

---

# Requisito: enviarRecordatorioDeDeudaPendiente

El sistema enviará semanalmente una notifiación a un Residente cuando tenga deudas en estado "PENDIENTE" o "MORA".

## Datos del Caso de Uso

- **Entradas:** número de cedula de identidad del cliente
- **Salidas:** recordatorio de deuda pendiente y mensaje

## Escenario Básico

1. El caso de uso inicia con el Sistema consultando un Residente por su número de cédula.
2. El Sistema consulta la fecha actual.
3. Si el día de la fecha actual es domingo, entonces el Sistema consulta las deudas en estado "PENDIENTE" o "MORA" del Residente
4. Si el Residente tiene deudas en estado "PENDIENTE" o "MORA", el Sistema envia una notificación al Residente para recordarle sobre el pago de la deuda.
5. El caso de uso inicia con el Sistema enviando la notificación al cliente.

## Escenarios Alternos

### Escenario Alterno 1

1. El caso de uso inicia con el Sistema consultando un Residente por su número de cédula.
2. El Sistema consulta la fecha actual.
3. Si el día de la fecha actual NO es domingo, entonces el caso de uso finaliza sin que el Sistema realize alguna acción


### Escenario Alterno 2

1. El caso de uso inicia con el Sistema consultando un Residente por su número de cédula.
2. El Sistema consulta la fecha actual.
3. Si el día de la fecha actual es domingo, entonces el Sistema consulta las deudas en estado "PENDIENTE" o "MORA" del Residente
4. Si el Residente NO tiene deudas en estado "PENDIENTE" o "MORA", el caso de uso finaliza sin que el Sistema realice alguna acción.

---

# Requisito: registrarMoraDeuda

El sistema cambiará el estado de una deuda a "MORA" si la fecha de pago venció aplicando un interés del 15% sobre el valor a pagar de la deuda.

## Datos del Caso de Uso

- **Entradas:** número de cedula de identidad del cliente
- **Salidas:** recordatorio de deuda pendiente y mensaje

## Escenario Básico


## Escenarios Alternos


---

# Requisito: notificarReporteRendicionCuentas

El sistema notificará a los Residentes que el Presidente generó un reporte de rendición de cuentas entre dos fechas disponible para consulta.

## Datos del Caso de Uso

- **Entradas:** --
- **Salidas:** recordatorio de deuda pendiente y mensaje

## Escenario Básico


## Escenarios Alternos