<<<<<<< HEAD
✈ Airline Management System
Sistema de gestión para aerolíneas desarrollado con Spring Boot (backend REST API) y Angular (frontend SPA). Permite administrar pilotos, tripulantes, aviones, vuelos y usuarios con autenticación JWT y control de acceso por roles.

🏗 Arquitectura
=======
# ✈ Airline Management System

Sistema de gestión para aerolíneas desarrollado con **Spring Boot** (backend REST API) y **Angular** (frontend SPA). Permite administrar pilotos, tripulantes, aviones, vuelos y usuarios con autenticación JWT y control de acceso por roles.

---

## 🏗 Arquitectura

```
┌─────────────────────┐         ┌──────────────────────┐
│   Angular 21        │  HTTP   │   Spring Boot 4       │
│   Puerto: 4200      │◄───────►│   Puerto: 8081        │
│   (Frontend SPA)    │  JWT    │   (REST API)          │
└─────────────────────┘         └──────────┬───────────┘
                                           │
                                ┌──────────▼───────────┐
                                │   PostgreSQL          │
                                │   Puerto: 5432        │
                                │   BD: aerolinea       │
                                └──────────────────────┘

🛠 Tecnologías
Backend
TecnologíaVersiónJava23.0.1Spring Boot4.0.6Apache Maven3.9.12Spring Security7.xJWT (jjwt)0.12.3Hibernate / JPA7.xPostgreSQL Driver42.xLombokLatest
Frontend
TecnologíaVersiónNode.js24.15.0npm11.12.1Angular CLI21.2.9Angular21.2.11TypeScript5.9.3Bootstrap5.xSweetAlert2Latest

📋 Requisitos Previos

Java JDK 21+
Apache Maven 3.9+
Node.js 20+
Angular CLI 21
PostgreSQL 15+
Git


🚀 Instalación y Configuración
1. Clonar el repositorio
bashgit clone https://github.com/tu-usuario/airline-management-system.git
cd airline-management-system
2. Configurar la base de datos
Crear la base de datos en PostgreSQL:
sqlCREATE DATABASE aerolinea;
3. Configurar variables de entorno del backend
Crea un archivo .env en la carpeta backend/:
envDB_URL=jdbc:postgresql://localhost:5432/aerolinea

```

---

## 🛠 Tecnologías

### Backend
| Tecnología | Versión |
|---|---|
| Java | 23.0.1 |
| Spring Boot | 4.0.6 |
| Apache Maven | 3.9.12 |
| Spring Security | 7.x |
| JWT (jjwt) | 0.12.3 |
| Hibernate / JPA | 7.x |
| PostgreSQL Driver | 42.x |
| Lombok | Latest |

### Frontend
| Tecnología | Versión |
|---|---|
| Node.js | 24.15.0 |
| npm | 11.12.1 |
| Angular CLI | 21.2.9 |
| Angular | 21.2.11 |
| TypeScript | 5.9.3 |
| Bootstrap | 5.x |
| SweetAlert2 | Latest |

---

## 📋 Requisitos Previos

- [Java JDK 21+](https://www.oracle.com/java/technologies/downloads/)
- [Apache Maven 3.9+](https://maven.apache.org/download.cgi)
- [Node.js 20+](https://nodejs.org/)
- [Angular CLI 21](https://angular.dev/)
- [PostgreSQL 15+](https://www.postgresql.org/download/)
- [Git](https://git-scm.com/)

---

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/airline-management-system.git
cd airline-management-system
```

### 2. Configurar la base de datos

Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE aerolinea;
```

### 3. Configurar variables de entorno del backend

Crea un archivo `.env` en la carpeta `backend/`:

```env
DB_URL=jdbc:postgresql://localhost:5432/aerolinea
DB_USERNAME=postgres
DB_PASSWORD=tu_password
JWT_SECRET=tu_clave_base64
MAIL_USERNAME=tu_correo@gmail.com
MAIL_PASSWORD=tu_app_password_gmail

Nota: Para generar el JWT_SECRET en Base64:
javaString encoded = Base64.getEncoder().encodeToString(
    "tu_clave_secreta_minimo_256_bits".getBytes(StandardCharsets.UTF_8));
System.out.println(encoded);

4. Ejecutar el backend
bashcd backend
mvn spring-boot:run
El servidor arrancará en http://localhost:8081
5. Instalar dependencias del frontend
bashcd frontend
npm install
6. Ejecutar el frontend
bashng serve
La aplicación estará disponible en http://localhost:4200

🔐 Roles del Sistema
RolDescripciónAccesoADMINAdministrador del sistemaPanel completo de gestiónPILOTOPiloto de aeronaveDashboard con sus vuelosTRIPULANTEMiembro de tripulaciónDashboard con sus vuelosOPERADOROperador de vuelosGestión de programación

📡 Endpoints de la API
Autenticación (/api/auth)
MétodoEndpointDescripciónAuthPOST/api/auth/registroRegistro de usuarioNoPOST/api/auth/loginInicio de sesiónNoPOST/api/auth/recuperar-passwordSolicitar recuperaciónNoPOST/api/auth/cambiar-passwordCambiar contraseñaNo
Usuarios (/api/usuarios)
MétodoEndpointDescripciónAuthGET/api/usuariosListar todosADMINGET/api/usuarios/{id}Buscar por IDADMINPUT/api/usuarios/{id}ActualizarADMINDELETE/api/usuarios/{id}Desactivar (lógico)ADMINPATCH/api/usuarios/{id}/reactivarReactivarADMIN
Pilotos (/api/pilotos)
MétodoEndpointDescripciónAuthGET/api/pilotosListar activosADMINGET/api/pilotos/{id}Buscar por IDADMINPOST/api/pilotosCrear pilotoADMINPUT/api/pilotos/{id}ActualizarADMINDELETE/api/pilotos/{id}Desactivar (lógico)ADMINPATCH/api/pilotos/{id}/reactivarReactivarADMIN
Tripulantes (/api/tripulantes)
MétodoEndpointDescripciónAuthGET/api/tripulantesListar activosADMINGET/api/tripulantes/{id}Buscar por IDADMINPOST/api/tripulantesCrear tripulanteADMINPUT/api/tripulantes/{id}ActualizarADMINDELETE/api/tripulantes/{id}Desactivar (lógico)ADMINPATCH/api/tripulantes/{id}/reactivarReactivarADMIN
Aviones (/api/aviones)
MétodoEndpointDescripciónAuthGET/api/avionesListar activosADMINGET/api/aviones/{id}Buscar por IDADMINPOST/api/avionesCrear aviónADMINPUT/api/aviones/{id}ActualizarADMINDELETE/api/aviones/{id}Desactivar (lógico)ADMINPATCH/api/aviones/{id}/reactivarReactivarADMINPUT/api/aviones/{id}/mantenimientoRegistrar mantenimientoADMIN
Bases (/api/bases)
MétodoEndpointDescripciónAuthGET/api/basesListar todasADMINGET/api/bases/{id}Buscar por IDADMINPOST/api/basesCrear baseADMINPUT/api/bases/{id}ActualizarADMINDELETE/api/bases/{id}EliminarADMIN
Vuelos (/api/vuelos)
MétodoEndpointDescripciónAuthGET/api/vuelosListar todosADMIN, OPERADORGET/api/vuelos/{id}Buscar por IDADMIN, OPERADORGET/api/vuelos/estado/{estado}Filtrar por estadoADMIN, OPERADORGET/api/vuelos/piloto/{id}Vuelos de un pilotoADMIN, PILOTOGET/api/vuelos/tripulante/{id}Vuelos de un tripulanteADMIN, TRIPULANTEPOST/api/vuelosCrear vueloADMIN, OPERADORPUT/api/vuelos/{id}ActualizarADMIN, OPERADORPUT/api/vuelos/{id}/cancelarCancelar vueloADMIN, OPERADORPATCH/api/vuelos/{id}/aterrizarRegistrar aterrizajeADMIN, OPERADORPATCH/api/vuelos/{id}/retrasarRetrasar vueloADMIN, OPERADOR

🗄 Modelo de Base de Datos
```

> **Nota:** Para generar el `JWT_SECRET` en Base64:
> ```java
> String encoded = Base64.getEncoder().encodeToString(
>     "tu_clave_secreta_minimo_256_bits".getBytes(StandardCharsets.UTF_8));
> System.out.println(encoded);
> ```

### 4. Ejecutar el backend

```bash
cd backend
mvn spring-boot:run
```

El servidor arrancará en `http://localhost:8081`

### 5. Instalar dependencias del frontend

```bash
cd frontend
npm install
```

### 6. Ejecutar el frontend

```bash
ng serve
```

La aplicación estará disponible en `http://localhost:4200`

---

## 🔐 Roles del Sistema

| Rol | Descripción | Acceso |
|---|---|---|
| `ADMIN` | Administrador del sistema | Panel completo de gestión |
| `PILOTO` | Piloto de aeronave | Dashboard con sus vuelos |
| `TRIPULANTE` | Miembro de tripulación | Dashboard con sus vuelos |
| `OPERADOR` | Operador de vuelos | Gestión de programación |

---

## 📡 Endpoints de la API

### Autenticación (`/api/auth`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `POST` | `/api/auth/registro` | Registro de usuario | No |
| `POST` | `/api/auth/login` | Inicio de sesión | No |
| `POST` | `/api/auth/recuperar-password` | Solicitar recuperación | No |
| `POST` | `/api/auth/cambiar-password` | Cambiar contraseña | No |

### Usuarios (`/api/usuarios`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/usuarios` | Listar todos | ADMIN |
| `GET` | `/api/usuarios/{id}` | Buscar por ID | ADMIN |
| `PUT` | `/api/usuarios/{id}` | Actualizar | ADMIN |
| `DELETE` | `/api/usuarios/{id}` | Desactivar (lógico) | ADMIN |
| `PATCH` | `/api/usuarios/{id}/reactivar` | Reactivar | ADMIN |

### Pilotos (`/api/pilotos`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/pilotos` | Listar activos | ADMIN |
| `GET` | `/api/pilotos/{id}` | Buscar por ID | ADMIN |
| `POST` | `/api/pilotos` | Crear piloto | ADMIN |
| `PUT` | `/api/pilotos/{id}` | Actualizar | ADMIN |
| `DELETE` | `/api/pilotos/{id}` | Desactivar (lógico) | ADMIN |
| `PATCH` | `/api/pilotos/{id}/reactivar` | Reactivar | ADMIN |

### Tripulantes (`/api/tripulantes`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/tripulantes` | Listar activos | ADMIN |
| `GET` | `/api/tripulantes/{id}` | Buscar por ID | ADMIN |
| `POST` | `/api/tripulantes` | Crear tripulante | ADMIN |
| `PUT` | `/api/tripulantes/{id}` | Actualizar | ADMIN |
| `DELETE` | `/api/tripulantes/{id}` | Desactivar (lógico) | ADMIN |
| `PATCH` | `/api/tripulantes/{id}/reactivar` | Reactivar | ADMIN |

### Aviones (`/api/aviones`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/aviones` | Listar activos | ADMIN |
| `GET` | `/api/aviones/{id}` | Buscar por ID | ADMIN |
| `POST` | `/api/aviones` | Crear avión | ADMIN |
| `PUT` | `/api/aviones/{id}` | Actualizar | ADMIN |
| `DELETE` | `/api/aviones/{id}` | Desactivar (lógico) | ADMIN |
| `PATCH` | `/api/aviones/{id}/reactivar` | Reactivar | ADMIN |
| `PUT` | `/api/aviones/{id}/mantenimiento` | Registrar mantenimiento | ADMIN |

### Bases (`/api/bases`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/bases` | Listar todas | ADMIN |
| `GET` | `/api/bases/{id}` | Buscar por ID | ADMIN |
| `POST` | `/api/bases` | Crear base | ADMIN |
| `PUT` | `/api/bases/{id}` | Actualizar | ADMIN |
| `DELETE` | `/api/bases/{id}` | Eliminar | ADMIN |

### Vuelos (`/api/vuelos`)
| Método | Endpoint | Descripción | Auth |
|---|---|---|---|
| `GET` | `/api/vuelos` | Listar todos | ADMIN, OPERADOR |
| `GET` | `/api/vuelos/{id}` | Buscar por ID | ADMIN, OPERADOR |
| `GET` | `/api/vuelos/estado/{estado}` | Filtrar por estado | ADMIN, OPERADOR |
| `GET` | `/api/vuelos/piloto/{id}` | Vuelos de un piloto | ADMIN, PILOTO |
| `GET` | `/api/vuelos/tripulante/{id}` | Vuelos de un tripulante | ADMIN, TRIPULANTE |
| `POST` | `/api/vuelos` | Crear vuelo | ADMIN, OPERADOR |
| `PUT` | `/api/vuelos/{id}` | Actualizar | ADMIN, OPERADOR |
| `PUT` | `/api/vuelos/{id}/cancelar` | Cancelar vuelo | ADMIN, OPERADOR |
| `PATCH` | `/api/vuelos/{id}/aterrizar` | Registrar aterrizaje | ADMIN, OPERADOR |
| `PATCH` | `/api/vuelos/{id}/retrasar` | Retrasar vuelo | ADMIN, OPERADOR |

---

## 🗄 Modelo de Base de Datos

```
usuario
├── id, username, email, password, rol, activo
├── piloto_id (FK → piloto)
└── tripulante_id (FK → tripulante)

persona (abstract)
├── id, codigo, nombre, activo
└── base_id (FK → base)

piloto extends persona
└── licencia, horas_de_vuelo, estado_personal_aereo

tripulante extends persona
└── rol_tripulante, estado_tripulante

avion
├── id, codigo, tipo, capacidad, horas_de_vuelo
├── estado, year_fabricacion
├── ultimo_mantenimiento, proximo_mantenimiento
└── base_id (FK → base)

base
└── id, nombre, codigo_iata, codigo_icao, ciudad, pais, es_base_mantenimiento

vuelo
├── id, num_vuelo, fecha_hora_salida, fecha_hora_llegada, estado
├── origen_id (FK → base)
├── destino_id (FK → base)
├── avion_id (FK → avion)
└── piloto_id (FK → piloto)

vuelo_tripulacion
├── vuelo_id (FK → vuelo)
└── tripulante_id (FK → tripulante)
<<<<<<< HEAD

📁 Estructura del Proyecto
```

---

## 📁 Estructura del Proyecto

```
airline-management-system/
├── backend/                          # Spring Boot API REST
│   ├── src/main/java/
│   │   └── com/tallerpiloto/piloto/
│   │       ├── config/               # SecurityConfig, CorsConfig
│   │       ├── controller/           # REST Controllers
│   │       ├── dto/                  # Data Transfer Objects
│   │       ├── model/                # Entidades JPA
│   │       ├── repository/           # Spring Data JPA
│   │       ├── security/             # JWT Filter, UserDetailsService
│   │       └── service/              # Lógica de negocio
│   └── src/main/resources/
│       └── application.properties
│
└── frontend/                         # Angular SPA
    └── src/app/
        ├── core/
        │   ├── guards/               # AuthGuard
        │   ├── interceptors/         # JWT Interceptor
        │   └── services/             # Auth, Piloto, Avion, etc.
        ├── layout/                   # Sidebar, Header
        ├── models/                   # Interfaces TypeScript
        └── pages/
            ├── login/
            ├── registro/
            ├── recuperar-password/
            ├── cambiar-password/
            └── dashboard/
                ├── admin/            # Pilotos, Aviones, Vuelos, etc.
                ├── piloto/
                ├── tripulante/
                └── operador/

🔑 Autenticación
El sistema usa JWT (JSON Web Tokens). Para acceder a los endpoints protegidos:

Hacer login en POST /api/auth/login
Copiar el token de la respuesta
Incluirlo en el header de cada petición:

Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...

📮 Colección Postman
Importa el archivo airline-api.postman_collection.json en Postman para probar todos los endpoints directamente.

🤝 Contribución

Fork el repositorio
Crea tu rama: git checkout -b feature/nueva-funcionalidad
Commit tus cambios: git commit -m 'feat: agregar nueva funcionalidad'
Push a la rama: git push origin feature/nueva-funcionalidad
Abre un Pull Request


📄 Licencia

```

---

## 🔑 Autenticación

El sistema usa **JWT (JSON Web Tokens)**. Para acceder a los endpoints protegidos:

1. Hacer login en `POST /api/auth/login`
2. Copiar el token de la respuesta
3. Incluirlo en el header de cada petición:

```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## 📮 Colección Postman

Importa el archivo `airline-api.postman_collection.json` en Postman para probar todos los endpoints directamente.

---

## 🤝 Contribución

1. Fork el repositorio
2. Crea tu rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit tus cambios: `git commit -m 'feat: agregar nueva funcionalidad'`
4. Push a la rama: `git push origin feature/nueva-funcionalidad`
5. Abre un Pull Request

---

## 📄 Licencia


Este proyecto fue desarrollado como trabajo académico.
