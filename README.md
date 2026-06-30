# DuoCuida — Sistema de Gestión de Apoyo Estudiantil

Sistema de arquitectura de microservicios desarrollado con Spring Boot para la gestión integral del apoyo estudiantil universitario. Incluye 10 microservicios independientes, un API Gateway con autenticación JWT, documentación Swagger/OpenAPI y pruebas unitarias con JUnit y Mockito.

## Integrantes

| Nombre | Rol |
|--------|-----|
| Christian Vargas Castro | Integrante 1 |
| Alvaro Araya Sanchez | Integrante 2 |

## Descripción del Proyecto

DuoCuida es una plataforma de apoyo estudiantil basada en microservicios independientes que permite gestionar usuarios, perfiles, solicitudes, evaluaciones, planes de apoyo, derivaciones, atenciones, beneficios, autenticación y notificaciones. Cada microservicio posee su propia base de datos, su propia lógica de negocio y se comunica con otros servicios mediante WebClient. El acceso externo se centraliza a través de un API Gateway que valida un token JWT antes de enrutar las peticiones.

## Microservicios

| Microservicio | Puerto | Responsable | Descripción |
|---------------|--------|-------------|-------------|
| usuarios | 8082 | Christian Vargas Castro | Gestión de usuarios del sistema (ESTUDIANTE, GESTOR, ADMIN) |
| perfiles | 8083 | Christian Vargas Castro | Perfiles de estudiantes |
| solicitudes | 8084 | Christian Vargas Castro | Solicitudes de apoyo estudiantil |
| evaluaciones | 8085 | Alvaro Araya Sanchez | Evaluación de solicitudes con resultado y puntaje |
| planes | 8086 | Alvaro Araya Sanchez | Planes de apoyo vinculados a evaluaciones |
| derivaciones | 8087 | Alvaro Araya Sanchez | Derivaciones a unidades de apoyo |
| atenciones | 8088 | Alvaro Araya Sanchez | Registro de atenciones realizadas |
| beneficios | 8089 | Alvaro Araya Sanchez | Gestión de beneficios asignados a estudiantes |
| notificaciones | 8090 | Christian Vargas Castro | Envío de notificaciones a usuarios |
| auth | 8091 | Christian Vargas Castro | Autenticación y autorización |
| **gateway** | **8080** | Equipo | API Gateway con JWT que centraliza el enrutamiento a los 10 microservicios |

## Tecnologías Utilizadas

- Java 21
- Spring Boot 4.0.6 (microservicios) / Spring Boot 3.3.5 (gateway)
- Spring Data JPA + Hibernate
- Spring Validation (Bean Validation JSR 380)
- Flyway (migraciones de base de datos)
- MariaDB (XAMPP)
- WebClient (Spring WebFlux) — comunicación entre microservicios
- Spring Cloud Gateway 2023.0.3 + Spring Security + JWT (jjwt) — API Gateway
- springdoc-openapi 3.0.3 — documentación Swagger/OpenAPI
- JUnit 5 + Mockito + AssertJ — pruebas unitarias
- Configuración con archivos YAML (application.yml)
- Lombok
- SLF4J (logging estructurado)
- Git + GitHub (control de versiones)

## Funcionalidades Implementadas

- CRUD completo en todos los microservicios
- Patrón Controller–Service–Repository con separación de responsabilidades
- DTOs para entrada y salida de datos
- Validaciones con Bean Validation (@NotNull, @NotBlank, @Pattern)
- Manejo centralizado de excepciones con @RestControllerAdvice / @ControllerAdvice
- Respuestas controladas con ResponseEntity y códigos HTTP adecuados
- Logs estructurados con SLF4J en controller, service y exception handler
- Migraciones de base de datos con Flyway (V1: tablas, V2: datos iniciales)
- Comunicación entre microservicios mediante WebClient:
  - `auth` consulta `usuarios` para validar credenciales en login y registro
  - `solicitudes` consulta `perfiles` para validar el estudiante al crear una solicitud
  - `solicitudes` llama `notificaciones` para enviar notificaciones automáticas
  - `planes` consulta `evaluaciones` para validar existencia antes de crear un plan
  - `atenciones` consulta `solicitudes` para validar existencia antes de registrar una atención
- Pruebas unitarias con JUnit 5 y Mockito (capa Service y capa Controller)
- Documentación interactiva de la API con Swagger/OpenAPI
- API Gateway con autenticación JWT centralizada
- Configuración de cada servicio mediante archivos YAML

## API Gateway con JWT

El gateway corre en el puerto **8080** y es la única puerta de entrada controlada. Valida un token JWT en cada petición (salvo el login) antes de reenviarla al microservicio correspondiente.

### Rutas del Gateway

| Ruta (gateway) | Microservicio destino |
|----------------|------------------------|
| `http://localhost:8080/api/usuarios/**` | usuarios (8082) |
| `http://localhost:8080/api/perfiles/**` | perfiles (8083) |
| `http://localhost:8080/api/solicitudes/**` | solicitudes (8084) |
| `http://localhost:8080/api/evaluaciones/**` | evaluaciones (8085) |
| `http://localhost:8080/api/planes/**` | planes (8086) |
| `http://localhost:8080/api/derivaciones/**` | derivaciones (8087) |
| `http://localhost:8080/api/atenciones/**` | atenciones (8088) |
| `http://localhost:8080/api/beneficios/**` | beneficios (8089) |
| `http://localhost:8080/api/notificaciones/**` | notificaciones (8090) |
| `http://localhost:8080/api/auth/**` | auth (8091) |

### Flujo de autenticación

1. Una petición sin token a cualquier ruta devuelve **401 Unauthorized**.
2. Login para obtener el token:
   ```
   POST http://localhost:8080/auth/login
   {
     "username": "admin",
     "password": "1234"
   }
   ```
   Respuesta: `{ "token": "eyJhbGci..." }` (válido por 1 hora).
3. Con el token en el header `Authorization: Bearer <token>`, las peticiones a cualquier ruta son reenviadas al microservicio y devuelven **200 OK**. El mismo token sirve para todos los microservicios.

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación interactiva (levantar el servicio y abrir en el navegador):

| Microservicio | Swagger UI |
|---------------|------------|
| usuarios | http://localhost:8082/swagger-ui.html |
| perfiles | http://localhost:8083/swagger-ui.html |
| solicitudes | http://localhost:8084/swagger-ui.html |
| evaluaciones | http://localhost:8085/swagger-ui.html |
| planes | http://localhost:8086/swagger-ui.html |
| derivaciones | http://localhost:8087/swagger-ui.html |
| atenciones | http://localhost:8088/swagger-ui.html |
| beneficios | http://localhost:8089/swagger-ui.html |
| notificaciones | http://localhost:8090/swagger-ui.html |
| auth | http://localhost:8091/swagger-ui.html |


## Pruebas Unitarias

Cada microservicio incluye pruebas unitarias con **JUnit 5** y **Mockito**:

- **Pruebas de Service:** con `@ExtendWith(MockitoExtension.class)`, `@Mock` (repositorios y clientes WebClient) e `@InjectMocks`. Validan la lógica de negocio y las excepciones (estructura Given–When–Then, asserts con AssertJ).
- **Pruebas de Controller:** con `@WebMvcTest` y `MockMvc`. Validan los endpoints REST y los códigos HTTP (200, 201, 204, 400, 404).

Para ejecutarlas en IntelliJ: clic derecho sobre la carpeta `src/test/java` → *Run tests*, o la flecha verde junto a cada clase de test. (Requiere MariaDB activo para el test de contexto `*ApplicationTests`.)

## Requisitos Previos

- Java 21
- IntelliJ IDEA
- XAMPP con MariaDB activo
- Maven
- Postman (u otro cliente REST)

## Pasos para Ejecutar

### 1. Iniciar MariaDB
Abre XAMPP y presiona **Start** en el módulo **MySQL**.

### 2. Ejecutar cada microservicio
Abre cada proyecto en IntelliJ IDEA y ejecuta la clase principal (`*Application.java`). Flyway creará automáticamente la base de datos, las tablas e insertará los datos iniciales.

Por ejemplo: `EvaluacionesApplication`, `PlanesApplication`, etc.

### 3. Ejecutar el Gateway
Abre el proyecto `gateway` y ejecuta `GatewayJwrApplication` (puerto 8080). Arranca con Netty (servidor reactivo).

### 4. Verificar endpoints

**Directo al microservicio (sin gateway, sin token):**
```
GET http://localhost:8085/api/evaluaciones
GET http://localhost:8086/api/planes
```

**A través del Gateway (requiere token JWT):**
```
1. POST http://localhost:8080/auth/login   { "username": "admin", "password": "1234" }
2. GET  http://localhost:8080/api/planes    (Header: Authorization: Bearer <token>)
```

## Estructura de cada Microservicio

```
src/main/java/com/duocuida/{servicio}/
├── controller/       # Manejo de solicitudes REST
├── service/          # Lógica de negocio
├── repository/       # Acceso a datos (JpaRepository)
├── model/            # Entidades JPA
├── dto/              # Objetos de transferencia de datos
├── exception/        # Manejo centralizado de errores
└── client/           # WebClient (auth, solicitudes, planes, atenciones)

src/main/resources/
├── application.yml   # Configuración (puerto, datasource, JPA, Flyway)
└── db/migration/
    ├── V1__create_tables.sql
    └── V2__insert_data.sql

src/test/java/com/duocuida/{servicio}/
├── service/          # Pruebas unitarias del Service (Mockito)
└── controller/       # Pruebas de Controller (@WebMvcTest)
```

