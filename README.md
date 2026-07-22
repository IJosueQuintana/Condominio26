# Condominio26 🏢

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-26.0.1-blue.svg)
![SQLite](https://img.shields.io/badge/SQLite-3.53.2-lightgrey.svg)
![License](https://img.shields.io/badge/License-CC0_1.0-green.svg)

**Condominio26** es un sistema integral de gestión de condominios desarrollado en Java. 
Este proyecto fue creado para la asignatura de Diseño de Software de la FIS-EPN (Semestre 2026A) con el objetivo de administrar y automatizar procesos residenciales como el control de accesos, reservas de espacios comunes, finanzas, y comunicación comunitaria.

---

## 🏗 Arquitectura del Sistema

El proyecto está diseñado con una **Arquitectura MVC Multicapa (Capa 3 y 4)** garantizando alta cohesión, bajo acoplamiento y separación de responsabilidades:

1. **Capa de Presentación (Controladores JavaFX):** Interfaz gráfica de usuario basada en archivos FXML y estilizada con CSS modular. Emplea controladores para capturar eventos del usuario y delegar peticiones a la capa de servicios.
2. **Capa de Lógica de Negocio (Servicios):** Implementa las interfaces de casos de uso (ej. `IServicioFinanzas`, `IServicioInmuebles`). Resuelve reglas de negocio, validaciones y cuenta con procesos automatizados (ej. `SistemaAutomatizadoScheduler`).
3. **Capa de Acceso a Datos (DAOs):** Utiliza el patrón Data Access Object (DAO) para aislar la persistencia. Se comunica directamente con la base de datos relacional (SQLite) a través de JDBC.
4. **Capa de Dominio (Entidades):** Define los objetos del núcleo del sistema (POJOs), aplicando conceptos de herencia y composición (ej. `Inmueble`, `EspacioComun`, `UnidadPrivada`, `Usuario`, `Deuda`).

---

## 🧩 Módulos Principales

*   🔐 **Administración:** Gestión de usuarios, perfiles, control de acceso basado en roles (RBAC) y definición de permisos.
*   💰 **Finanzas:** Registro de alícuotas, cobro de multas, validación de pagos (transferencias y simulación de tarjetas de crédito), y reportes de rendición de cuentas.
*   📅 **Reservas:** Catálogo de disponibilidad, validación de choques de horarios, y control del ciclo de vida de la reserva.
*   🏠 **Inmuebles:** Mantenimiento de unidades privadas y espacios comunes, registro de características, disponibilidad y gestión de casos fortuitos.
*   🛡️ **Check-In y Seguridad:** Control de garita para entradas de residentes y visitantes, programación de visitas, asignación de parqueaderos y envío de alertas de emergencia.
*   📢 **Comunicación:** Publicación de anuncios con vigencia, envío de mensajes institucionales y notificaciones.

---


# 🏢 Estructura de Carpetas

```
🏢 Condominio26/                                 - Directorio raíz del proyecto.
├── 📄 pom.xml                                   - Archivo de configuración de Maven (dependencias, plugins, versión de Java 21).
├── 📄 LICENSE                                   - Licencia de código abierto CC0 1.0 Universal (dominio público).
├── 📄 README.md                                 - Documentación principal con instrucciones de instalación y uso.
├── 📁 UML/                                      - Diagramas de modelado y arquitectura del sistema.
│   └── 📄 class_diagram.puml                    - Diagrama de clases detallado en formato PlantUML.
├── 📁 src/                                      - Directorio principal del código fuente y recursos.
│   └── 📁 main/                                 - Código de la aplicación de producción.
│       ├── 📁 java/                             - Clases, interfaces y paquetes en lenguaje Java.
│       │   └── 📁 fis/dsw/sgc/                  - Paquete base del proyecto (Sistema de Gestión de Condominios).
│       │       ├── 📁 administracion/           - Módulo de control de acceso, perfiles, roles y permisos del sistema.
│       │       │   ├── 📁 controller/           - Controladores JavaFX para las vistas de gestión de cuentas y roles.
│       │       │   ├── 📁 dao/                  - Objetos de Acceso a Datos (consultas a la BD de usuarios y permisos).
│       │       │   ├── 📁 model/                - Entidades de negocio (Usuario, Cuenta, Perfil, Rol, Permiso).
│       │       │   ├── 📁 service/              - Casos de uso, lógica de validación y autenticación (Login).
│       │       │   └── 📁 dashboard/            - Controladores para el contenedor principal y menú lateral dinámico.
│       │       ├── 📁 check_in/                 - Módulo de seguridad perimetral, garita y alertas.
│       │       ├── 📁 comunicacion/             - Módulo de mensajería, anuncios institucionales y notificaciones.
│       │       ├── 📁 finanzas/                 - Módulo de cobros, multas, pagos, alícuotas y reportes financieros.
│       │       │   ├── 📁 exception/            - Excepciones personalizadas para reglas de negocio (ej. DeudaNoExisteException).
│       │       ├── 📁 inmuebles/                - Módulo del inventario de bienes (áreas comunes y unidades privadas).
│       │       ├── 📁 reservas/                 - Módulo para administrar el uso de áreas comunes (canchas, salones).
│       │       ├── 📁 conexion_bd/              - Clases globales para manejar la conexión JDBC a SQLite.
│       │       ├── 📁 core/                     - Componentes y utilidades transversales para toda la aplicación.
│       │       │   └── 📁 util/                 - Funciones comunes (ej. NavigationUtil para transiciones entre pantallas).
│       │       └── 📁 usuarios/                 - Utilidades y clases compartidas para gestionar la información de residentes.
│       │           └── 📁 dto/                  - DTOs de fachada (ej. ResidenteFachadaDTO) usados por varios módulos.
│       └── 📂 resources/                        - Archivos estáticos de la interfaz gráfica y estilos.
│           ├── 📂 administracion/               - Interfaz visual del módulo administrativo.
│           │   ├── 📂 css/                      - Hojas de estilo específicas del dashboard y login.
│           │   ├── 📂 img/                      - Avatares de usuario, fondos e iconografía.
│           │   └── 📂 fxml/                     - Archivos XML definiendo la maquetación de las vistas del módulo.
│           ├── 📂 check_in/                     - Interfaz visual del módulo de garita.
│           ├── 📂 comunicacion/                 - Interfaz visual del módulo de mensajería.
│           ├── 📂 finanzas/                     - Interfaz visual del módulo de contabilidad.
│           ├── 📂 inmuebles/                    - Interfaz visual del módulo de inventario físico.
│           └── 📂 reservas/                     - Interfaz visual del módulo de agendamiento.
└── 📂 target/                                   - Directorio autogenerado por Maven tras la compilación.
    └── 📂 classes/                              - Código binario (.class) y recursos preparados para ser empaquetados (JAR).
        ├── 📂 module-info.class                 - Descriptor de módulos compilado (Java 9+ Module System).
        └── 📂 fis/dsw/sgc/                      - Estructura replicada con las clases ejecutables del sistema.
```

---
## 🚀 Guía de Instalación y Configuración

### 1. Requisitos Previos
*   **Java Development Kit (JDK) 21**
*   **Apache Maven 3.8+**
*   **Git**

### 2. Instalación de Maven

**En Windows:**
1. Descarga el archivo ZIP desde la [página oficial de Maven](https://maven.apache.org/download.cgi).
2. Extrae el contenido en un directorio (ej. `C:\Program Files\apache-maven-3.9.x`).
3. Añade la ruta del directorio `bin` a la variable de entorno `PATH` de tu sistema.
4. Verifica la instalación abriendo una terminal y ejecutando:
   ```bash
   mvn -version
   ```

**En macOS (usando Homebrew):**
```bash
brew install maven
```

**En Linux (Debian/Ubuntu):**
```bash
sudo apt update
sudo apt install maven
```

### 3. Proceso de Clonación

Para obtener una copia local del repositorio, ejecuta en tu terminal:

```bash
# Clonar el repositorio
git clone <URL_DEL_REPOSITORIO>

# Ingresar al directorio del proyecto
cd condominio26
```

### 4. Compilación y Ejecución

El proyecto utiliza Maven para la gestión de dependencias (JavaFX, SQLite, jBCrypt, Ikonli). Al ejecutar los siguientes comandos, Maven descargará automáticamente todo lo necesario.

1. **Limpiar y compilar el proyecto:**
   ```bash
   mvn clean compile
   ```
2. **Empaquetar la aplicación (Opcional):**
   ```bash
   mvn package
   ```
3. **Ejecutar la aplicación usando el plugin de JavaFX:**
   ```bash
   mvn javafx:run
   ```

---

## 🛠 Tecnologías Utilizadas

*   **Lenguaje:** Java 21
*   **Framework UI:** JavaFX 26.0.1
*   **Gestor de Dependencias:** Maven
*   **Base de Datos:** SQLite (v3.53.2.0)
*   **Seguridad:** jBCrypt (v0.4) para hashing de contraseñas.
*   **Iconografía:** Ikonli (FontAwesome Pack)

---

## 📜 Licencia

Este proyecto está bajo la licencia **Creative Commons Legal Code CC0 1.0 Universal**.
Los creadores renuncian a todos sus derechos de autor en todo el mundo y a los derechos conexos, donando este código al dominio público. Puede copiar, modificar, distribuir y realizar la obra, incluso con fines comerciales, sin necesidad de solicitar permiso.



