# Módulo GRB - Gestión de Usuarios y Administradores

## Índice

- [CU-01 registrarCuenta](#cu-01-registrarcuenta)
- [CU-02 actualizarInformacionDeCuenta](#cu-02-actualizarinformaciondecuenta)
- [CU-03 desactivarCuenta](#cu-03-desactivarcuenta)
- [CU-04 asignarRolAUsuario](#cu-04-asignarrolausuario)
- [CU-05 definirPermisosDeRol](#cu-05-definirpermisosderol)
- [CU-06 iniciarSesion](#cu-06-iniciarsesion)
- [CU-07 actualizarPerfil](#cu-07-actualizarperfil)
- [CU-08 recuperarContrasena](#cu-08-recuperarcontrasena)
- [CU-09 enviarTokenDeRestablecimiento](#cu-09-enviartokenderestablecimiento)

---

# CU-01 registrarCuenta

## Descripción

Permite al Administrador registrar una nueva cuenta de usuario en el sistema del condominio.

## Actor Principal

Administrador

## Precondiciones

- El Administrador ha iniciado sesión.
- Posee permisos para gestionar usuarios.

## Postcondiciones

- La cuenta queda registrada.

## Escenario Básico

1. El caso de uso comienza cuando el Administrador registra un Usuario.
2. El Administrador proporciona los datos del Usuario nombre y apellido.
3. El Sistema valida el nombre y apellido del Usuario entonces el Administrador proporciona el correo electrónico.
4. El Sistema valida el correo electrónico del Usuario entonces el Administrador proporciona la contraseña.
5. El Sistema registra la cuenta y envía las credenciales al correo del Usuario.
6. El caso de uso termina cuando el Sistema muestra el mensaje "Usuario registrado exitosamente".

## Escenario Alternos

### A1. Correo ya registrado

1. El caso de uso comienza cuando el Administrador registra un Usuario.
2. El Administrador proporciona los datos del Usuario nombre y apellido.
3. El Sistema valida el nombre y apellido del Usuario entonces el Administrador proporciona el correo electrónico.
4. El Sistema detecta que el correo electrónico del Usuario ya está registrado en el sistema.
5. El Sistema muestra el mensaje "El correo electrónico ya está registrado".
6. El caso de uso finaliza sin registrar al usuario.

### A2. Campos incompletos

1. El caso de uso comienza cuando el Administrador registra un Usuario.
2. El Administrador proporciona los datos del Usuario nombre y apellido.
3. El Sistema valida el nombre y apellido del Usuario entonces el Administrador proporciona el correo electrónico.
4. El Sistema valida el correo electrónico del Usuario entonces el Administrador proporciona la contraseña.
5. El Sistema detecta que uno o más campos obligatorios del Usuario están vacíos.
6. El Sistema muestra el mensaje "Todos los campos son obligatorios".
7. El caso de uso finaliza sin registrar al Usuario.

---

# CU-02 actualizarInformacionDeCuenta

## Descripción

Permite al Administrador actualizar la información de una cuenta de usuario existente en el sistema del condominio.

## Actor Principal

Administrador

## Precondiciones

- El Administrador ha iniciado sesión.
- Posee permisos para gestionar usuarios.
- La cuenta del Usuario existe en el sistema.

## Postcondiciones

- La información de la cuenta queda actualizada.

## Escenario Básico

1. El caso de uso comienza cuando el Administrador selecciona una cuenta de Usuario.
2. El Sistema muestra la información actual de la cuenta.
3. El Administrador modifica los datos necesarios de la cuenta.
4. El Sistema valida la información ingresada.
5. El Administrador confirma la actualización de la cuenta.
6. El Sistema registra los cambios realizados.
7. El caso de uso termina cuando el Sistema muestra el mensaje "Información de cuenta actualizada exitosamente".

## Escenarios Alternos

### A1. Datos inválidos

1. El caso de uso comienza cuando el Administrador modifica la información de una cuenta.
2. El Sistema detecta que uno o más datos ingresados son inválidos.
3. El Sistema muestra el mensaje "Los datos ingresados no son válidos".
4. El caso de uso finaliza sin actualizar la cuenta.

### A2. Campos obligatorios vacíos

1. El caso de uso comienza cuando el Administrador modifica la información de una cuenta.
2. El Sistema detecta que uno o más campos obligatorios están vacíos.
3. El Sistema muestra el mensaje "Existen campos obligatorios vacíos".
4. El caso de uso finaliza sin actualizar la cuenta.

### A3. Correo electrónico ya registrado
1. El caso de uso comienza cuando el Administrador modifica la información de la cuenta.
2. El Administrador ingresa un correo electrónico que ya se encuentra asociado a otra cuenta en el sistema.
3. El Sistema detecta la duplicidad del correo electrónico.
4. El Sistema muestra el mensaje "El correo electrónico ingresado ya está registrado por otro usuario".
5. El caso de uso finaliza sin actualizar la cuenta.

---

# CU-03 desactivarCuenta

## Descripción

Permite al Administrador desactivar o suspender el acceso de una cuenta de usuario existente en el sistema del condominio, bloqueando su inicio de sesión sin eliminar sus datos históricos.

## Actor Principal

Administrador

## Precondiciones

- El Administrador ha iniciado sesión.
- Posee permisos para gestionar usuarios.
- La cuenta del Usuario existe en el sistema y actualmente se encuentra en estado activo.

## Postcondiciones

- El estado de la cuenta se actualiza a "desactivada".
- El usuario propietario de la cuenta pierde el acceso al sistema.

## Escenario Básico

1. El caso de uso comienza cuando el Administrador selecciona la cuenta de un Usuario específico.
2. El Sistema muestra la información general de la cuenta y su estado actual.
3. El Administrador selecciona la opción de desactivar la cuenta.
4. El Sistema solicita al Administrador una confirmación para proceder con la desactivación.
5. El Administrador confirma la acción.
6. El Sistema actualiza el estado de la cuenta a "desactivada" en la base de datos.
7. El caso de uso termina cuando el Sistema muestra el mensaje "Cuenta desactivada exitosamente".

## Escenarios Alternos

### A1. Cuenta ya desactivada

1. El caso de uso comienza cuando el Administrador selecciona la cuenta de un Usuario.
2. El Sistema detecta que el estado actual de la cuenta ya es "desactivada".
3. El Sistema muestra el mensaje "La cuenta seleccionada ya se encuentra desactivada".
4. El caso de uso finaliza sin realizar modificaciones en el sistema.

### A2. Cancelación de la operación

1. El caso de uso comienza cuando el Administrador selecciona la cuenta de un Usuario.
2. El Sistema solicita confirmación de la acción (Paso 4 del Escenario Básico).
3. El Administrador selecciona la opción de cancelar o rechaza la confirmación.
4. El Sistema muestra el mensaje "Operación cancelada".
5. El caso de uso finaliza sin modificar el estado de la cuenta.

---

# CU-04 asignarRolAUsuario

## Descripción

Permite al Administrador asignar o modificar el rol de seguridad de una cuenta de usuario existente para controlar sus niveles de acceso dentro del sistema.

## Actor Principal

Administrador

## Precondiciones

- El Administrador ha iniciado sesión correctamente.
- El usuario al que se le asignará el rol debe estar previamente registrado en el sistema.

## Postcondiciones

- El rol del usuario es actualizado en la base de datos.
- Los permisos asociados al nuevo rol se aplican de inmediato a la sesión del usuario.

## Escenario Básico

1. El Administrador solicita gestionar los roles de un usuario.
2. El Sistema solicita el identificador (correo o nombre) del usuario.
3. El Administrador ingresa los datos de búsqueda.
4. El Sistema valida la existencia del usuario y muestra su información actual.
5. El Administrador selecciona la opción "Asignar nuevo rol".
6. El Sistema despliega la lista de roles disponibles (ej: Residente, Conserje, Directiva).
7. El Administrador selecciona el rol deseado y confirma la acción.
8. El Sistema guarda los cambios y muestra un mensaje de éxito.

## Escenario Alternos

### A1. Usuario no encontrado

1. En el paso 4, el Sistema no encuentra coincidencias.
2. El Sistema muestra un mensaje de error: "Usuario inexistente".
3. El flujo regresa al punto de búsqueda o finaliza.

### A2. El usuario ya posee ese rol

1. En el paso 7, el Administrador selecciona un rol que el usuario ya tiene asignado.
2. El Sistema informa que no hubo cambios necesarios.
3. El caso de uso finaliza.

---

# CU-05 definirPermisosDeRol

## Descripción

Permite al Administrador definir los permisos de un rol en el sistema del condominio.

## Actor Principal

Administrador

## Precondiciones

- El Administrador ha iniciado sesión.
- Posee permisos para gestionar roles.

## Postcondiciones

- Los permisos del rol quedan registrados en el sistema.

## Escenario Básico

1. El caso de uso comienza cuando el Administrador define los permisos de un rol nuevo.
2. El Administrador proporciona el nombre del rol.
3. El Sistema valida el nombre del rol entonces el Administrador proporciona los permisos del rol.
4. El Sistema valida los permisos del rol entonces el Administrador confirma la definición de permisos.
5. El Sistema registra el rol con sus permisos.
6. El caso de uso termina cuando el Sistema muestra el mensaje "Permisos del rol registrados exitosamente".

## Escenarios Alternativos

### A1. Editar permisos de un rol existente

1. El caso de uso comienza cuando el Administrador edita los permisos de un rol existente.
2. El Administrador proporciona el nombre del rol.
3. El Sistema valida el nombre del rol entonces el Administrador proporciona los nuevos permisos del rol.
4. El Sistema valida los nuevos permisos del rol entonces el Administrador confirma la edición de permisos.
5. El Sistema actualiza los permisos del rol.
6. El caso de uso termina cuando el Sistema muestra el mensaje "Permisos del rol actualizados exitosamente".

### A2. Campos incompletos

1. El caso de uso comienza cuando el Administrador define los permisos de un rol nuevo.
2. El Administrador proporciona el nombre del rol.
3. El Sistema valida el nombre del rol entonces el Administrador proporciona los permisos del rol.
4. El Sistema detecta que uno o más campos obligatorios están vacíos.
5. El Sistema muestra el mensaje "Todos los campos son obligatorios".
6. El caso de uso finaliza sin definir los permisos del rol.

---

# CU-06 iniciarSesion

## Descripción

Permite al Usuario o Administrador autenticarse en el sistema del condominio.

## Actor Principal

Usuario, Administrador

## Precondiciones

- El Actor tiene una cuenta registrada.
- La cuenta no se encuentra desactivada.

## Postcondiciones

- El Actor inicia sesión en el sistema.

## Escenario Básico

1. El caso de uso comienza cuando el Actor inicia sesión.
2. El Actor proporciona el correo electrónico.
3. El Sistema valida el correo electrónico entonces el Actor proporciona la contraseña.
4. El Sistema valida la contraseña y el estado de la cuenta.
5. El caso de uso termina cuando el Sistema muestra el mensaje "Sesión iniciada exitosamente".

## Escenarios Alternos

### A1. Credenciales incorrectas

1. El caso de uso comienza cuando el Actor inicia sesión.
2. El Actor proporciona el correo electrónico.
3. El Sistema valida el correo electrónico entonces el Actor proporciona la contraseña.
4. El Sistema detecta que el correo electrónico o la contraseña son incorrectos.
5. El Sistema muestra el mensaje "Credenciales incorrectas".
6. El caso de uso finaliza sin iniciar sesión.

### A2. Cuenta desactivada

1. El caso de uso comienza cuando el Actor inicia sesión.
2. El Actor proporciona el correo electrónico.
3. El Sistema valida el correo electrónico entonces el Actor proporciona la contraseña.
4. El Sistema detecta que la cuenta se encuentra desactivada.
5. El Sistema muestra el mensaje "La cuenta se encuentra desactivada".
6. El caso de uso finaliza sin iniciar sesión.

### A3. Campos incompletos

1. El caso de uso comienza cuando el Actor inicia sesión.
2. El Actor proporciona el correo electrónico.
3. El Sistema valida el correo electrónico entonces el Actor proporciona la contraseña.
4. El sistema detecta que uno o más campos obligatorios están vacíos.
5. El Sistema muestra el mensaje "Todos los campos son obligatorios".
6. El caso de uso finaliza sin iniciar sesión.

---

# CU-07 actualizarPerfil

## Descripción

Permite al Usuario actualizar la información de su perfil en el sistema del condominio.

## Actor Principal

Usuario

## Precondiciones

- El Usuario ha iniciado sesión.
- El Usuario posee una cuenta activa en el sistema.

## Postcondiciones

- La información del perfil del Usuario queda actualizada en el sistema

## Escenario Básico

1. El caso de uso comienza cuando el Usuario accede a la sección de configuración de perfil.
2. El Sistema muestra la información actual del perfil.
3. El Usuario modifica los datos que desea actualizar.
4. El Sistema valida la información ingresada.
5. El Usuario confirma la actualización del perfil.
6. El Sistema registra los cambios realizados.
7. El caso de uso termina cuando el Sistema muestra el mensaje "Perfil actualizado exitosamente".

## Escenarios Alternos

### A1. Datos inválidos

1. El caso de uso comienza cuando el Usuario modifica la información de su perfil.
2. El Sistema detecta que uno o más datos ingresados son inválidos.
3. El Sistema muestra el mensaje "Los datos ingresados no son válidos".
4. El caso de uso finaliza sin actualizar el perfil.

### A2. Campos obligatorios vacíos

1. El caso de uso comienza cuando el Usuario modifica la información de su perfil.
2. El Sistema detecta que uno o más campos obligatorios están vacíos.
3. El Sistema muestra el mensaje "Existen campos obligatorios vacíos".
4. El caso de uso finaliza sin actualizar el perfil.

---

# CU-08 recuperarContrasena

## Descripción

Permite al Usuario recuperar el acceso a su cuenta mediante el restablecimiento de su contraseña utilizando un token de verificación enviado a su correo electrónico.

## Actor Principal

Usuario

## Precondiciones

- El Usuario posee una cuenta registrada en el Sistema.
- El Usuario tiene acceso al correo electrónico asociado a la cuenta.

## Postcondiciones

- La contraseña de la cuenta es actualizada exitosamente.
- El Usuario puede iniciar sesión con la nueva contraseña.

## Escenario Básico

1. El caso de uso comienza cuando el Usuario intenta recuperar su contraseña.
2. El Usuario proporciona el correo electrónico asociado a su cuenta.
3. El Sistema valida que el correo electrónico exista.
4. El Sistema ejecuta el caso de uso "enviarTokenDeRestablecimiento".
5. El Usuario ingresa el token recibido.
6. El Sistema valida el token de recuperación.
7. El Usuario proporciona la nueva contraseña.
8. El Sistema valida la nueva contraseña.
9. El Sistema actualiza la contraseña de la cuenta.
10. El caso de uso termina cuando el Sistema muestra el mensaje "Contraseña restablecida exitosamente".

## Escenarios Alternativos

### A1. Correo electrónico no registrado

1. El caso de uso comienza cuando el Usuario intenta recuperar su contraseña.
2. El Usuario proporciona el correo electrónico asociado a su cuenta.
3. El Sistema detecta que el correo electrónico no se encuentra registrado.
4. El Sistema muestra el mensaje "No existe una cuenta asociada a este correo electrónico".
5. El caso de uso finaliza sin iniciar el proceso de recuperación.

### A2. Token inválido o expirado

1. El caso de uso comienza cuando el Usuario intenta recuperar su contraseña.
2. El Usuario proporciona el correo electrónico asociado a su cuenta.
3. El Sistema valida que el correo electrónico exista.
4. El Sistema ejecuta el caso de uso "enviarTokenDeRestablecimiento".
5. El Usuario ingresa el token recibido.
6. El Sistema detecta que el token es inválido o ha expirado.
7. El Sistema muestra el mensaje "Token inválido o expirado".
8. El caso de uso finaliza sin restablecer la contraseña.

### A3. Contraseña no válida

1. El caso de uso comienza cuando el Usuario intenta recuperar su contraseña.
2. El Usuario proporciona el correo electrónico asociado a su cuenta.
3. El Sistema valida que el correo electrónico exista.
4. El Sistema ejecuta el caso de uso "enviarTokenDeRestablecimiento".
5. El Usuario ingresa el token recibido.
6. El Sistema valida el token de recuperación.
7. El Usuario proporciona una nueva contraseña.
8. El Sistema detecta que la contraseña no cumple con las políticas de seguridad establecidas.
9. El Sistema muestra el mensaje "La contraseña no cumple con los requisitos de seguridad".
10. El caso de uso finaliza sin actualizar la contraseña.

---

# CU-09 enviarTokenDeRestablecimiento

## Descripción

Permite al Sistema generar y enviar un código de seguridad para la recuperación de una cuenta.

## Actor Principal

Sistema

## Precondiciones

- Se ha solicitado la recuperación de contraseña de una cuenta existente.

## Postcondiciones

- El token de restablecimiento es enviado al correo del usuario.

## Escenario Básico

1. El caso de uso comienza cuando el Sistema aprueba la solicitud de recuperación de contraseña.
2. El Sistema genera el token temporal de recuperación.
3. El Sistema asocia el token generado a la cuenta correspondiente.
4. El Sistema envía el mensaje con el token al correo electrónico registrado.
5. El caso de uso termina cuando el Sistema muestra el mensaje "Token enviado exitosamente".

## Escenarios Alternos

### A1. Falla de conexión con el servidor

1. El caso de uso comienza cuando el Sistema aprueba la solicitud de recuperación de contraseña.
2. El Sistema genera el token temporal de recuperación.
3. El Sistema asocia el token generado a la cuenta correspondiente.
4. El Sistema detecta un error de conexión al intentar enviar el correo electrónico.
5. El Sistema muestra el mensaje "Error al enviar el token, intente más tarde".
6. El caso de uso finaliza sin enviar el token.

### A2. Límite de intentos excedido

1. El caso de uso comienza cuando el Sistema aprueba la solicitud de recuperación de contraseña.
2. El Sistema detecta que se ha superado el límite de intentos permitidos para generar tokens.
3. El Sistema muestra el mensaje "Límite de envíos excedido, espere un momento".
4. El caso de uso finaliza sin enviar el token.
