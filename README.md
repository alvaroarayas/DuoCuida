# DuoCuida — Sistema de Gestión de Apoyo Estudiantil

Sistema de arquitectura de microservicios desarrollado con Spring Boot para la gestión integral del apoyo estudiantil universitario.

## Integrantes

| Nombre | Rol |
|--------|-----|
| Christian Vargas Castro | Integrante 1 |
| Alvaro Araya Sanchez | Integrante 2 |

## Descripción del Proyecto

DuoCuida es una plataforma de apoyo estudiantil basada en microservicios independientes que permite gestionar usuarios, perfiles, solicitudes, evaluaciones, planes de apoyo, derivaciones, atenciones, beneficios, autenticación y notificaciones. Cada microservicio posee su propia base de datos, su propia lógica de negocio y se comunica con otros servicios mediante WebClient.

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

## Tecnologías Utilizadas

- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + Hibernate
- Spring Validation (Bean Validation JSR 380)
- Flyway (migraciones de base de datos)
- MariaDB (XAMPP)
- WebClient (Spring WebFlux) — comunicación entre microservicios
- Lombok
- SLF4J (logging estructurado)
- Git + GitHub (control de versiones)

## Funcionalidades Implementadas

- CRUD completo en todos los microservicios
- Patrón Controller–Service–Repository con separación de responsabilidades
- DTOs para entrada y salida de datos
- Validaciones con Bean Validation (@NotNull, @NotBlank, @Pattern)
- Manejo centralizado de excepciones con @RestControllerAdvice
- Respuestas controladas con ResponseEntity y códigos HTTP adecuados
- Logs estructurados con SLF4J en controller, service y exception handler
- Migraciones de base de datos con Flyway (V1: tablas, V2: datos iniciales)
- Comunicación entre microservicios mediante WebClient:
  - `auth` consulta `usuarios` para validar credenciales en login y registro
  - `solicitudes` consulta `perfiles` para validar el estudiante al crear una solicitud
  - `solicitudes` llama `notificaciones` para enviar notificaciones automáticas
  - `planes` consulta `evaluaciones` para validar existencia antes de crear un plan
  - `atenciones` consulta `solicitudes` para validar existencia antes de registrar una atención

## Requisitos Previos

- Java 21
- IntelliJ IDEA
- XAMPP con MariaDB activo
- Maven

## Pasos para Ejecutar

### 1. Iniciar MariaDB
Abre XAMPP y presiona **Start** en el módulo **MySQL**.

### 2. Ejecutar cada microservicio
Abre cada proyecto en IntelliJ IDEA y ejecuta la clase principal (*Application.java). Flyway creará automáticamente la base de datos, las tablas e insertará los datos iniciales.

por ejemplo: EvaluacionApplication

### 3. Verificar endpoints
Usar Postman para probar los endpoints:
```
GET http://localhost:8085/api/evaluaciones
GET http://localhost:8086/api/planes
GET http://localhost:8087/api/derivaciones
GET http://localhost:8088/api/atenciones
GET http://localhost:8089/api/beneficios
```
Estructura de cada Microservicio
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
├── application.properties
└── db/migration/
    ├── V1__create_tables.sql
    └── V2__insert_data.sql
```
	
	
	
