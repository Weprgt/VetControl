# VetControl

Sistema de escritorio para la administración de una clínica veterinaria. Permite gestionar propietarios, mascotas, citas, historiales clínicos, inventario y usuarios mediante una interfaz desarrollada con Java Swing y una base de datos MySQL.

## Capturas

### Inicio de sesión

![Inicio de sesión de VetControl](docs/imagenes/login.png)

### Panel de inicio

El panel principal muestra clientes y mascotas activas, citas del día, productos con existencias bajas y las próximas citas programadas.

![Panel de inicio](docs/imagenes/inicio.png)

### Gestión de clientes

![Gestión de clientes](docs/imagenes/clientes.png)

### Gestión de mascotas

![Gestión de mascotas](docs/imagenes/mascotas.png)

### Gestión de citas

![Gestión de citas](docs/imagenes/citas.png)

### Historial clínico

![Historial clínico](docs/imagenes/historial-clinico.png)

### Inventario

![Gestión de inventario](docs/imagenes/inventario.png)

### Administración de usuarios

![Administración de usuarios](docs/imagenes/usuarios.png)

### Registro de usuarios

![Formulario de registro de usuarios](docs/imagenes/registrar-usuario.png)

## Funcionalidades

- Inicio de sesión con contraseñas protegidas mediante PBKDF2.
- Control de acceso según el rol del usuario.
- Activación y desactivación de cuentas sin eliminar su información.
- Registro, búsqueda, edición y desactivación de clientes.
- Registro, búsqueda, edición y desactivación de mascotas.
- Programación, edición y cancelación de citas.
- Control de horarios para evitar citas activas duplicadas.
- Registro y consulta del historial clínico de las mascotas.
- Gestión de productos y existencias del inventario.
- Registro y consulta de movimientos de inventario.
- Panel de inicio con indicadores y próximas citas.
- Administración de usuarios, roles y datos profesionales de veterinarios.

## Roles

| Rol | Acceso principal |
| --- | --- |
| Administrador | Todos los módulos, incluido inventario y administración de usuarios |
| Recepcionista | Inicio, clientes, mascotas y citas |
| Veterinario | Inicio, mascotas, citas e historial clínico |

## Tecnologías utilizadas

- Java 25
- Java Swing
- FlatLaf 3.7.2
- Maven
- MySQL 8
- MySQL Connector/J
- LGoodDatePicker 11.2.1
- JDBC
- Git y GitHub
- NetBeans 31

## Arquitectura

El proyecto separa sus responsabilidades en paquetes:

```text
src/main/java/com/mycompany/vetcontrol/
├── dao/        # Consultas y operaciones con MySQL
├── modelo/     # Entidades y objetos utilizados por el sistema
├── servicio/   # Reglas de autenticación y lógica de servicio
├── util/       # Conexión y seguridad de contraseñas
└── vista/      # Ventanas, paneles y diálogos Swing
```

## Requisitos

- JDK 25 o una versión compatible configurada en el proyecto.
- Maven 3.9 o superior.
- MySQL Server 8 o una base de datos MySQL compatible.
- Git, si se desea clonar el repositorio.

## Instalación

1. Clona el repositorio:

   ```bash
   git clone URL_DEL_REPOSITORIO
   cd VetControl
   ```

2. Crea la base de datos `vetcontrol` e importa el script SQL del proyecto.

3. Copia el archivo de ejemplo:

   ```text
   src/main/resources/config.properties.example
   ```

   y crea localmente:

   ```text
   src/main/resources/config.properties
   ```

4. Configura la conexión sin compartir tus credenciales:

   ```properties
   db.url=jdbc:mysql://localhost:3306/vetcontrol?serverTimezone=America/Guatemala
   db.usuario=root
   db.contrasena=TU_CONTRASENA
   ```

5. Compila el proyecto:

   ```bash
   mvn clean package
   ```

6. Ejecuta la clase principal:

   ```text
   com.mycompany.vetcontrol.VetControl
   ```

También puede abrirse y ejecutarse directamente desde NetBeans.

## Usuarios de demostración

Estas cuentas son únicamente para pruebas. Las contraseñas deben cambiarse en cualquier instalación real.

| Rol | Usuario | Contraseña |
| --- | --- | --- |
| Administrador | `admin` | `Admin123*` |
| Recepcionista | `ana.recepcion` | `Recepcion123*` |
| Veterinario | `daniel.lopez` | `Veterinario123*` |
| Veterinario | `laura.morales` | `Veterinario123*` |

## Seguridad

- Las contraseñas no se almacenan como texto legible.
- Se utiliza PBKDF2 con HMAC-SHA256, sal aleatoria y 210 000 iteraciones.
- Las consultas utilizan `PreparedStatement`.
- `config.properties` está excluido mediante `.gitignore`.
- Solo `config.properties.example`, sin credenciales reales, debe guardarse en GitHub.
- Los registros importantes se desactivan en lugar de eliminarse físicamente.

## Base de datos

La aplicación utiliza, entre otras, las siguientes tablas:

- `roles`
- `usuarios`
- `veterinarios`
- `clientes`
- `mascotas`
- `citas`
- `historial_clinico`
- `productos`
- `movimientos_inventario`

La base de datos puede ejecutarse localmente o alojarse en un servicio MySQL en la nube. Las credenciales de conexión nunca deben incluirse en el repositorio.

## Estado del proyecto

- [x] Autenticación de usuarios
- [x] Permisos por roles
- [x] Gestión de clientes
- [x] Gestión de mascotas
- [x] Gestión de citas
- [x] Historial clínico
- [x] Inventario
- [x] Movimientos de inventario
- [x] Administración de usuarios
- [x] Resumen dinámico en Inicio
- [ ] Publicación de la base de datos en la nube

## Autor

**William Pereira**  
Proyecto desarrollado como parte del proceso de aprendizaje de desarrollo de aplicaciones de escritorio con Java.

## Aviso

Este proyecto tiene fines educativos. Los nombres y datos mostrados en las capturas y registros de demostración son ficticios.
