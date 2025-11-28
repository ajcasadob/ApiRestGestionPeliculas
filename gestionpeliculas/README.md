# 🎬 API REST - Gestión de Películas

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9.0-C71A36?logo=apachemaven)
![H2](https://img.shields.io/badge/H2-Database-blue)
![Swagger](https://img.shields.io/badge/OpenAPI-3.0-85EA2D?logo=swagger)
![License](https://img.shields.io/badge/license-MIT-blue)

> **Proyecto de Desarrollo de una API REST con Spring Boot**  
> Acceso a Datos | Programación de Servicios y Procesos | Curso 2025-26

API REST completa para la gestión de un catálogo de películas, directores y actores.  Implementa operaciones CRUD, gestión de reparto (relación Many-to-Many), validaciones de negocio robustas y documentación automática con OpenAPI/Swagger.

---

## 📋 Tabla de Contenidos

- [Introducción](#-introducción)
- [Modelo de Datos](#-modelo-de-datos)
- [Requisitos Funcionales](#-requisitos-funcionales)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Ejecución](#-instalación-y-ejecución)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Arquitectura](#-arquitectura)
- [Endpoints de la API](#-endpoints-de-la-api)
- [DTOs (Data Transfer Objects)](#-dtos-data-transfer-objects)
- [Sistema de Excepciones](#️-sistema-de-excepciones)
- [Validaciones de Negocio](#-validaciones-de-negocio)
- [Documentación Swagger](#-documentación-swagger)
- [Colección Postman](#-colección-postman)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Manejo de Errores](#️-manejo-de-errores)
- [Autor](#-autor)

---

## 🎯 Introducción

Este proyecto implementa una **API REST** completa para la gestión de un catálogo cinematográfico que permite:

✅ **CRUD completo** de Directores y Películas  
✅ **CRUD básico** de Actores (Crear y Listar)  
✅ **Gestión de reparto** mediante relación Many-to-Many entre Películas y Actores  
✅ **Validaciones de negocio** (directores mayores de edad, películas únicas, etc.)  
✅ **Documentación automática** con OpenAPI 3.0 y Swagger UI  
✅ **Colección Postman** para pruebas completas de la API  
✅ **Manejo robusto de excepciones** con Problem Details (RFC 7807)

---

## 📊 Modelo de Datos

Este proyecto modela un catálogo de películas con **tres entidades** y una **relación Many-to-Many entre Película y Actor**.

### 🎥 Entidades a Modelar

#### **Director** (Entidad Principal)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Clave primaria (auto-generada) |
| `nombre` | String | Nombre completo del director |
| `anioNacimiento` | Integer | Año de nacimiento |

**Asociación:** Un Director **dirige muchas** Películas

---

#### **Película** (Entidad Secundaria)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Clave primaria (auto-generada) |
| `titulo` | String | Título de la película (**único**) |
| `genero` | String | Género cinematográfico |
| `fechaEstreno` | LocalDate | Fecha de estreno |
| `director_id` | Long | Foreign Key → Director |

**Asociación:** Un Director dirige muchas Películas

---

#### **Actor** (Entidad Secundaria - Reparto)
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Clave primaria (auto-generada) |
| `nombre` | String | Nombre completo del actor/actriz |

**Asociación:** Una Película tiene **muchos** Actores y un Actor participa en **muchas** Películas (**M:M**)

---

### 🔗 Diagrama de Relaciones

```
                    1                                    M:N
┌─────────────┐ <───────── ┌─────────────┐ <─────────────────> ┌─────────────┐
│  Director   │     N      │  Película   │                     │    Actor    │
├─────────────┤            ├─────────────┤                     ├─────────────┤
│ id (PK)     │            │ id (PK)     │                     │ id (PK)     │
│ nombre      │            │ titulo      │                     │ nombre      │
│ anioNacim.   │            │ genero      │                     └─────────────┘
└─────────────┘            │ fechaEstreno│
                           │ director_id │
                           └─────────────┘
                                  │
                                  │
                                  ▼
                    ┌──────────────────────────┐
                    │  pelicula_actor          │
                    │  (tabla intermedia M:N)  │
                    ├──────────────────────────┤
                    │ pelicula_id (FK)         │
                    │ actor_id (FK)            │
                    └──────────────────────────┘
```

---

### 📝 Descripción de las Relaciones

| Desde | Hacia | Tipo | Cardinalidad | Descripción |
|-------|-------|------|--------------|-------------|
| **Director** | **Película** | One-to-Many | 1:N | Un director puede dirigir muchas películas |
| **Película** | **Director** | Many-to-One | N:1 | Cada película es dirigida por un solo director |
| **Película** | **Actor** | Many-to-Many | M:N | Una película tiene muchos actores en su reparto |
| **Actor** | **Película** | Many-to-Many | N:M | Un actor puede participar en muchas películas |

---

### 🔧 Implementación JPA

**Director.java:**
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private Integer anioNacimiento;
    
    // Relación 1:N con Película (un director dirige muchas películas)
    @OneToMany(mappedBy = "director")
    @Builder.Default
    private Set<Pelicula> peliculas = new HashSet<>();
    
    private static final int EDAD_MINIMA = 18;
    
    public int calcularEdad(int anioActual) {
        return anioActual - this.anioNacimiento;
    }
    
    public boolean esMayorDeEdad(int anioActual) {
        return calcularEdad(anioActual) >= EDAD_MINIMA;
    }
}
```

**Pelicula.java:**
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String titulo;  // Único
    
    private String genero;
    private LocalDate fechaEstreno;
    
    // Relación N:1 con Director (muchas películas - un director)
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;
    
    // Relación M:N con Actor (una película tiene muchos actores)
    @ManyToMany
    @JoinTable(
        name = "pelicula_actor",
        joinColumns = @JoinColumn(name = "pelicula_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @Builder.Default
    private Set<Actor> actores = new HashSet<>();
    
    // Helper methods para gestionar la relación M:N
    public void addActor(Actor actor) {
        this.actores.add(actor);
        actor.getPeliculas().add(this);
    }
    
    public void removeActor(Actor actor) {
        this.actores. remove(actor);
        actor. getPeliculas().remove(this);
    }
}
```

**Actor.java:**
```java
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    
    // Relación M:N con Película (lado inverso - un actor participa en muchas películas)
    @ManyToMany(mappedBy = "actores")
    @Builder.Default
    private Set<Pelicula> peliculas = new HashSet<>();
}
```

---

### 📊 Resumen de Cardinalidades

```
Director (1) ──────> (N) Película
Película (N) ──────> (1) Director
Película (M) <─────> (N) Actor
```

**Interpretación:**
- ✅ **1 Director** puede dirigir **N Películas** (ejemplo: Christopher Nolan dirigió Inception, Interstellar, Tenet...)
- ✅ **1 Película** tiene **1 Director** (ejemplo: Inception fue dirigida por Christopher Nolan)
- ✅ **1 Película** tiene **M Actores** en su reparto (ejemplo: Inception tiene a DiCaprio, Page, Hardy...)
- ✅ **1 Actor** participa en **N Películas** (ejemplo: DiCaprio actuó en Inception, The Wolf of Wall Street...)

---

## 🎯 Requisitos Funcionales

### CRUD Completo
- ✅ **Director**: CREATE, READ (todos/por ID), UPDATE, DELETE
- ✅ **Película**: CREATE, READ (todos/por ID), UPDATE, DELETE
  - El POST de Película **requiere** el ID de un Director existente

### CRUD Básico
- ✅ **Actor**: CREATE, READ (todos/por ID)
  - **No incluye**: UPDATE (PUT) ni DELETE
  - Los actores se gestionan principalmente a través de la relación M:N con películas

### Gestión de Reparto (Relación Many-to-Many)
- ✅ `POST /api/v1/peliculas/{peliculaId}/actores/{actorId}`: Asignar actor al reparto de una película
- ✅ `GET /api/v1/peliculas/{peliculaId}`: Obtener película con su director y reparto completo de actores

### Excepciones Requeridas
Todas mapeadas a **ProblemDetail (RFC 7807)**:
- ✅ `EntidadNoEncontradaException` (404 Not Found) para Película, Director o Actor
- ✅ `PeliculaYaExisteException` (409 Conflict) al intentar crear película con título duplicado
- ✅ `ActorYaEnRepartoException` (409 Conflict) si se asigna un actor ya existente en el reparto
- ✅ `DirectorMenorEdadException` (400 Bad Request) si el director tenía <18 años en fecha de estreno

---

## 🛠 Tecnologías

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 4.0.0 | Framework principal |
| **Spring Data JPA** | (incluido) | Capa de persistencia ORM |
| **Spring Web MVC** | (incluido) | Framework REST |
| **H2 Database** | (runtime) | Base de datos en memoria |
| **Lombok** | (incluido) | Reducción de código boilerplate |
| **SpringDoc OpenAPI** | 2.8.14 | Documentación automática Swagger |
| **Maven** | 3.9.0 | Gestión de dependencias |

### 📦 Dependencias Principales (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot Web MVC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>
    
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org. springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- H2 Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- SpringDoc OpenAPI (Swagger) -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.8.14</version>
    </dependency>
</dependencies>
```

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 21** ([Descargar aquí](https://www.oracle. com/java/technologies/downloads/#java21))
- **Maven 3.9+** ([Descargar aquí](https://maven.apache.org/download.cgi))
- **Git** ([Descargar aquí](https://git-scm. com/downloads))
- Un IDE como **IntelliJ IDEA**, **Eclipse** o **VS Code**
- **Postman** para probar la API ([Descargar aquí](https://www.postman.com/downloads/))

### Verificar instalación

```bash
java -version   # Debe mostrar Java 21
mvn -version    # Debe mostrar Maven 3.9+
git --version   # Debe mostrar Git instalado
```

---

## 🚀 Instalación y Ejecución

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/ajcasadob/ApiRestGestionPeliculas.git
cd ApiRestGestionPeliculas/gestionpeliculas
```

### 2️⃣ Compilar el proyecto

```bash
./mvnw clean install
```

> 💡 **Windows**: Usa `mvnw.cmd` en lugar de `./mvnw`

### 3️⃣ Ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

### 4️⃣ Acceder a la aplicación

Una vez iniciada la aplicación, podrás acceder a:

| Recurso | URL | Descripción |
|---------|-----|-------------|
| **API Base** | `http://localhost:8080/api/v1` | Endpoint base de la API |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` | Documentación interactiva |
| **OpenAPI JSON** | `http://localhost:8080/v3/api-docs` | Especificación OpenAPI 3.0 |
| **H2 Console** | `http://localhost:8080/h2-console` | Consola de base de datos |

> 🔐 **H2 Console**: JDBC URL: `jdbc:h2:mem:testdb` | User: `sa` | Password: *(vacío)*

---

## 📁 Estructura del Proyecto

```
gestionpeliculas/
├── src/
│   ├── main/
│   │   ├── java/. ../gestionpeliculas/
│   │   │   ├── controller/              # 🎮 Controladores REST
│   │   │   │   ├── ActorController.java
│   │   │   │   ├── DirectorController.java
│   │   │   │   └── PeliculaController.java
│   │   │   │
│   │   │   ├── dto/                     # 📦 Data Transfer Objects
│   │   │   │   ├── ActorRequestDTO.java
│   │   │   │   ├── ActorResponseDTO.java
│   │   │   │   ├── ActorSimpleDTO.java
│   │   │   │   ├── DirectorRequestDTO.java
│   │   │   │   ├── DirectorResponseDTO.java
│   │   │   │   ├── DirectorSimpleDTO. java
│   │   │   │   ├── PeliculaRequestDTO.java
│   │   │   │   ├── PeliculaResponseDTO.java
│   │   │   │   └── PeliculaSimpleDTO.java
│   │   │   │
│   │   │   ├── model/                   # 🗂️ Entidades JPA
│   │   │   │   ├── Actor.java
│   │   │   │   ├── Director.java
│   │   │   │   └── Pelicula.java
│   │   │   │
│   │   │   ├── repository/              # 🗄️ Repositorios JPA
│   │   │   │   ├── ActorRepository.java
│   │   │   │   ├── DirectorRepository.java
│   │   │   │   └── PeliculaRepository.java
│   │   │   │
│   │   │   ├── service/                 # ⚙️ Lógica de negocio
│   │   │   │   ├── ActorService.java
│   │   │   │   ├── DirectorService.java
│   │   │   │   └── PeliculaService.java
│   │   │   │
│   │   │   └── error/                   # ⚠️ Sistema de excepciones
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── EntidadNoEncontradaException.java
│   │   │       ├── ActorNoEncontradoException.java
│   │   │       ├── DirectorNoEncontradoException.java
│   │   │       ├── PeliculaNoEncontradaException.java
│   │   │       ├── PeliculaYaExisteException.java
│   │   │       ├── ActorYaEnRepartoException.java
│   │   │       └── DirectorMenorEdadExcepetion.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties   # ⚙️ Configuración
│   │       └── data.sql                 # 📊 Datos iniciales
│   │
│   └── test/                            # 🧪 Tests
│
├── pom.xml                              # 📋 Dependencias Maven
├── README.md                            # 📖 Este archivo
└── API-Peliculas. postman_collection.json # 📮 Colección Postman
```

---

## 🏗 Arquitectura

El proyecto implementa una **arquitectura en capas** siguiendo principios de **separación de responsabilidades**:

```
┌──────────────────────────────────────────────────┐
│           CAPA DE PRESENTACIÓN                   │
│     Controllers + DTOs (Request/Response)        │
│  ┌────────────────────────────────────────────┐  │
│  │ ActorController (CRUD Básico)              │  │
│  │ PeliculaController (CRUD Completo + M:N)   │  │
│  │ DirectorController (CRUD Completo)         │  │
│  └────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│           CAPA DE NEGOCIO                        │
│      Services + Validaciones de Negocio          │
│  ┌────────────────────────────────────────────┐  │
│  │ ActorService (getAll, getById, crear)      │  │
│  │ PeliculaService (Gestión de reparto M:N)   │  │
│  │ DirectorService (CRUD completo)            │  │
│  │ • Validación edad director ≥18             │  │
│  │ • Validación películas únicas              │  │
│  │ • Validación actores únicos en reparto     │  │
│  └────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│         MANEJO DE EXCEPCIONES                    │
│      GlobalExceptionHandler (@RestControllerAdvice)│
│  ┌────────────────────────────────────────────┐  │
│  │ • EntidadNoEncontradaException → 404       │  │
│  │ • PeliculaYaExisteException → 409          │  │
│  │ • ActorYaEnRepartoException → 409          │  │
│  │ • DirectorMenorEdadException → 400         │  │
│  │ → Problem Details (RFC 7807)               │  │
│  └────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│         CAPA DE PERSISTENCIA                     │
│   Repositories + Entities + Relaciones JPA       │
│  ┌────────────────────────────────────────────┐  │
│  │ ActorRepository                            │  │
│  │ PeliculaRepository                         │  │
│  │ DirectorRepository                         │  │
│  │ Entities: Actor, Pelicula, Director        │  │
│  │ Relaciones: @ManyToOne, @ManyToMany        │  │
│  │ Tabla intermedia: pelicula_actor           │  │
│  └────────────────────────────────────────────┘  │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│            BASE DE DATOS H2                      │
│              (En Memoria)                        │
└──────────────────────────────────────────────────┘
```

---

## 🌐 Endpoints de la API

### 🎭 **Actores** (`/api/v1/actores`) - **CRUD BÁSICO**

| Método | Endpoint | Descripción | Código Éxito |
|--------|----------|-------------|--------------|
| `GET` | `/actores` | **Listar** todos los actores | 200 OK |
| `GET` | `/actores/{id}` | **Obtener** un actor por ID | 200 OK |
| `POST` | `/actores` | **Crear** un nuevo actor | 201 CREATED |

> ⚠️ **Nota**: Actor tiene CRUD **básico**. No incluye operaciones de actualización (PUT) ni eliminación (DELETE).  Los actores se gestionan principalmente a través de su asignación al reparto de películas.

**Request Body (POST):**
```json
{
  "nombre": "Leonardo DiCaprio"
}
```

---

### 🎬 **Películas** (`/api/v1/peliculas`) - **CRUD COMPLETO**

| Método | Endpoint | Descripción | Código Éxito |
|--------|----------|-------------|--------------|
| `GET` | `/peliculas` | Listar todas las películas | 200 OK |
| `GET` | `/peliculas/{id}` | Obtener película con su reparto completo | 200 OK |
| `POST` | `/peliculas` | Crear película (**requiere directorId**) | 201 CREATED |
| `PUT` | `/peliculas/{id}` | Actualizar una película | 200 OK |
| `DELETE` | `/peliculas/{id}` | Eliminar una película | 204 NO CONTENT |

**Request Body (POST/PUT):**
```json
{
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "directorId": 1
}
```

---

### 🎥 **Directores** (`/api/v1/directores`) - **CRUD COMPLETO**

| Método | Endpoint | Descripción | Código Éxito |
|--------|----------|-------------|--------------|
| `GET` | `/directores` | Listar todos los directores | 200 OK |
| `GET` | `/directores/{id}` | Obtener un director por ID | 200 OK |
| `POST` | `/directores` | Crear un nuevo director | 201 CREATED |
| `PUT` | `/directores/{id}` | Actualizar un director | 200 OK |
| `DELETE` | `/directores/{id}` | Eliminar un director | 204 NO CONTENT |

**Request Body (POST/PUT):**
```json
{
  "nombre": "Christopher Nolan",
  "anioNacimiento": 1970
}
```

---

### 🎪 **Gestión de Reparto (M:N)** (`/api/v1/peliculas/{peliculaId}/actores`)

| Método | Endpoint | Descripción | Código Éxito |
|--------|----------|-------------|--------------|
| `POST` | `/peliculas/{peliculaId}/actores/{actorId}` | Asignar un actor al reparto de una película | 200 OK |

> 🎯 **Funcionalidad clave**: Al obtener una película con `GET /peliculas/{id}`, se incluye **automáticamente** su director y la lista completa de actores del reparto.

---

### 📊 Comparación de CRUDs

| Operación | Director | Película | Actor |
|-----------|----------|----------|-------|
| **C**reate (POST) | ✅ | ✅ | ✅ |
| **R**ead All (GET) | ✅ | ✅ | ✅ |
| **R**ead by ID (GET) | ✅ | ✅ | ✅ |
| **U**pdate (PUT) | ✅ | ✅ | ❌ |
| **D**elete (DELETE) | ✅ | ✅ | ❌ |
| **Gestión M:N** | ❌ | ✅ | Pasivo |

---

## 📦 DTOs (Data Transfer Objects)

El proyecto implementa un **patrón DTO completo** con tres tipos de DTOs para evitar referencias circulares y optimizar las respuestas:

### 🔵 **Request DTOs** (Entrada de datos)

Utilizados en **POST** y **PUT** para crear/actualizar entidades.

| DTO | Campos | Uso |
|-----|--------|-----|
| `ActorRequestDTO` | `nombre` | Crear actores |
| `DirectorRequestDTO` | `nombre`, `anioNacimiento` | Crear/actualizar directores |
| `PeliculaRequestDTO` | `titulo`, `genero`, `fechaEstreno`, `directorId` | Crear/actualizar películas |

**Ejemplo:**
```java
public record ActorRequestDTO(String nombre) {
    public Actor toEntity() {
        return Actor.builder(). nombre(nombre).build();
    }
}
```

---

### 🟢 **Response DTOs** (Salida completa)

Utilizados en las respuestas de la API con **información completa** y **relaciones anidadas**.

| DTO | Campos | Información Anidada |
|-----|--------|---------------------|
| `ActorResponseDTO` | `id`, `nombre`, `peliculas` | Set\<PeliculaSimpleDTO\> |
| `DirectorResponseDTO` | `id`, `nombre`, `anioNacimiento`, `peliculas` | Set\<PeliculaSimpleDTO\> |
| `PeliculaResponseDTO` | `id`, `titulo`, `genero`, `fechaEstreno`, `director`, `actores` | DirectorSimpleDTO + Set\<ActorSimpleDTO\> |

**Ejemplo de PeliculaResponseDTO:**
```json
{
  "id": 1,
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan",
    "anioNacimiento": 1970
  },
  "actores": [
    {"id": 1, "nombre": "Leonardo DiCaprio"},
    {"id": 2, "nombre": "Ellen Page"}
  ]
}
```

---

### 🟡 **Simple DTOs** (Referencias anidadas)

Utilizados **dentro** de Response DTOs para **evitar referencias circulares** y reducir el tamaño de las respuestas.

| DTO | Campos | Propósito |
|-----|--------|-----------|
| `ActorSimpleDTO` | `id`, `nombre` | Representación de actor en reparto |
| `DirectorSimpleDTO` | `id`, `nombre`, `anioNacimiento` | Representación de director en película |
| `PeliculaSimpleDTO` | `id`, `titulo` | Representación de película en actor/director |

**¿Por qué Simple DTOs?**
- ✅ **Evitan referencias circulares** (Actor → Película → Actor...)
- ✅ **Reducen el payload** de las respuestas JSON
- ✅ **Mejoran el rendimiento** al no cargar datos innecesarios
- ✅ **Proporcionan información suficiente** para identificar entidades relacionadas

---

## ⚠️ Sistema de Excepciones

### Jerarquía de Excepciones Personalizadas

```
RuntimeException
    │
    ├── EntidadNoEncontradaException (clase base)
    │   ├── ActorNoEncontradoException
    │   ├── DirectorNoEncontradoException
    │   └── PeliculaNoEncontradaException
    │
    ├── PeliculaYaExisteException
    ├── ActorYaEnRepartoException
    └── DirectorMenorEdadExcepetion
```

### Excepciones Implementadas

| Excepción | HTTP Status | Cuándo se lanza |
|-----------|-------------|-----------------|
| `ActorNoEncontradoException` | **404 NOT FOUND** | El actor con el ID especificado no existe |
| `DirectorNoEncontradoException` | **404 NOT FOUND** | El director con el ID especificado no existe |
| `PeliculaNoEncontradaException` | **404 NOT FOUND** | La película con el ID especificado no existe |
| `PeliculaYaExisteException` | **409 CONFLICT** | Ya existe una película con ese título |
| `ActorYaEnRepartoException` | **409 CONFLICT** | El actor ya está asignado al reparto de esa película |
| `DirectorMenorEdadExcepetion` | **400 BAD REQUEST** | El director tenía menos de 18 años en la fecha de estreno |


---

## 🎯 Validaciones de Negocio

### Reglas Implementadas

| Validación | Descripción | Excepción Lanzada |
|------------|-------------|-------------------|
| 🎂 **Director ≥ 18 años** | El director debe tener al menos 18 años en la fecha de estreno de la película | `DirectorMenorEdadExcepetion` |
| 🎬 **Título único** | No puede haber dos películas con el mismo título | `PeliculaYaExisteException` |
| 🎭 **Actor único en reparto** | Un actor no puede estar duplicado en el reparto de la misma película | `ActorYaEnRepartoException` |
| 🔗 **Director existente** | Al crear una película, el `directorId` debe corresponder a un director existente | `DirectorNoEncontradoException` |
| 🔗 **Referencias válidas** | Al asignar un actor, tanto la película como el actor deben existir | `PeliculaNoEncontradaException` / `ActorNoEncontradoException` |

### Implementación de la Validación de Edad del Director

**En la entidad Director:**
```java
@Entity
public class Director {
    private static final int EDAD_MINIMA = 18;
    
    private String nombre;
    private Integer anioNacimiento;
    
    // Calcula la edad del director en un año específico
    public int calcularEdad(int anioActual) {
        return anioActual - this.anioNacimiento;
    }
    
    // Verifica si el director es mayor de edad en un año específico
    public boolean esMayorDeEdad(int anioActual) {
        return calcularEdad(anioActual) >= EDAD_MINIMA;
    }
}
```

**En el servicio PeliculaService:**
```java
public Pelicula create(PeliculaRequestDTO dto) {
    // Validar que el título no esté vacío
    if (! StringUtils.hasText(dto.titulo())) {
        throw new IllegalArgumentException("Falta el campo del título de la película");
    }
    
    // Validar que no exista película duplicada
    if (peliculaRepository.existsByTitulo(dto.titulo())) {
        throw new PeliculaYaExisteException(dto.titulo());
    }
    
    // Obtener el director
    Director d = directorRepository.findById(dto.directorId())
        .orElseThrow(() -> new DirectorNoEncontradoException(dto.directorId()));
    
    // VALIDACIÓN DE EDAD: El director debe ser mayor de 18 años en el año de estreno
    if (! d.esMayorDeEdad(dto.fechaEstreno(). getYear())) {
        throw new DirectorMenorEdadExcepetion(
            d.getNombre(), 
            d.calcularEdad(dto.fechaEstreno().getYear()), 
            dto.fechaEstreno().getYear()
        );
    }
    
    // Crear y guardar la película
    Pelicula p = dto.toEntity();
    p. setDirector(d);
    return peliculaRepository.save(p);
}
```

**Flujo de la validación:**
1. Se obtiene el año de estreno de la película (`dto.fechaEstreno(). getYear()`)
2. Se calcula la edad que tenía el director en ese año
3.  Si la edad es menor a 18 años, se lanza `DirectorMenorEdadExcepetion`
4. El mensaje de error incluye el nombre del director, su edad en ese momento y el año de estreno

---

## 📖 Documentación Swagger

La API incluye **documentación interactiva completa** generada automáticamente con **OpenAPI 3.0** y **Swagger UI**.

### Acceso a Swagger UI

1. **Iniciar la aplicación** (`./mvnw spring-boot:run`)
2. **Abrir navegador** en: `http://localhost:8080/swagger-ui. html`
3. **Explorar y probar** todos los endpoints

### Características de la Documentación

✅ **Todos los endpoints documentados** con descripciones detalladas  
✅ **Ejemplos de Request/Response** para cada operación  
✅ **Códigos de error documentados** con mensajes de ejemplo  
✅ **Modelos de datos** con todos los campos explicados  
✅ **Posibilidad de ejecutar peticiones** directamente desde el navegador  
✅ **Schemas de DTOs** generados automáticamente  

### Endpoints OpenAPI

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

---

## 📮 Colección Postman

El proyecto incluye una **colección completa de Postman** con todos los endpoints de la API para facilitar las pruebas.

### Contenido de la Colección

La colección `API-Peliculas.postman_collection.json` incluye:

- ✅ **CRUD básico de Actores** (GET, GET by ID, POST)
- ✅ **CRUD completo de Directores** (GET, GET by ID, POST, PUT, DELETE)
- ✅ **CRUD completo de Películas** (GET, GET by ID, POST, PUT, DELETE)
- ✅ **Gestión de Reparto M:N** (Asignar actores a películas)
- ✅ **Casos de error** (404, 409, 400)
- ✅ **Ejemplos de validaciones** (director menor de edad, película duplicada, etc.)

### Importar en Postman

1.  Abrir **Postman**
2. Click en **Import**
3. Seleccionar el archivo `ApiGestiónPeliculas.postman_collection.json`
4. La colección estará lista para usar

> 💡 **Tip**: La colección incluye variables de entorno con `baseUrl=http://localhost:8080/api/v1/` que puedes modificar según tu configuración.

---

## 💡 Ejemplos de Uso

### 1️⃣ Crear un Director

**Request:**
```http
POST /api/v1/directores
Content-Type: application/json

{
  "nombre": "Christopher Nolan",
  "anioNacimiento": 1970
}
```

**Response (201 CREATED):**
```json
{
  "id": 1,
  "nombre": "Christopher Nolan",
  "anioNacimiento": 1970,
  "peliculas": []
}
```

---

### 2️⃣ Crear una Película (con director existente)

**Request:**
```http
POST /api/v1/peliculas
Content-Type: application/json

{
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "directorId": 1
}
```

**Response (201 CREATED):**
```json
{
  "id": 1,
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan",
    "anioNacimiento": 1970
  },
  "actores": []
}
```

---

### 3️⃣ Crear un Actor

**Request:**
```http
POST /api/v1/actores
Content-Type: application/json

{
  "nombre": "Leonardo DiCaprio"
}
```

**Response (201 CREATED):**
```json
{
  "id": 1,
  "nombre": "Leonardo DiCaprio",
  "peliculas": []
}
```

---

### 4️⃣ Asignar Actor al Reparto (Relación M:N)

**Request:**
```http
POST /api/v1/peliculas/1/actores/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan",
    "anioNacimiento": 1970
  },
  "actores": [
    {
      "id": 1,
      "nombre": "Leonardo DiCaprio"
    }
  ]
}
```

---

### 5️⃣ Obtener Película con Reparto Completo

**Request:**
```http
GET /api/v1/peliculas/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "titulo": "Inception",
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "director": {
    "id": 1,
    "nombre": "Christopher Nolan",
    "anioNacimiento": 1970
  },
  "actores": [
    {"id": 1, "nombre": "Leonardo DiCaprio"},
    {"id": 2, "nombre": "Ellen Page"},
    {"id": 3, "nombre": "Tom Hardy"}
  ]
}
```

---

### 6️⃣ Listar Todos los Actores

**Request:**
```http
GET /api/v1/actores
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "Leonardo DiCaprio",
    "peliculas": [
      {"id": 1, "titulo": "Inception"},
      {"id": 2, "titulo": "The Wolf of Wall Street"}
    ]
  },
  {
    "id": 2,
    "nombre": "Ellen Page",
    "peliculas": [
      {"id": 1, "titulo": "Inception"}
    ]
  }
]
```

---

## ⚠️ Manejo de Errores

Todos los errores siguen el estándar **Problem Details (RFC 7807)**. 

### Error 404 - Entidad No Encontrada

**Request:**
```http
GET /api/v1/peliculas/999
```

**Response (404 NOT FOUND):**
```json
{
  "type": "https://api.peliculas.com/errors/not-found",
  "title": "Entidad no encontrada",
  "status": 404,
  "detail": "Pelicula con ID 999 no encontrada",
  "instance": "/api/v1/peliculas/999"
}
```

---

### Error 409 - Película Duplicada

**Request:**
```http
POST /api/v1/peliculas
Content-Type: application/json

{
  "titulo": "Inception",  // Ya existe
  "genero": "Ciencia Ficción",
  "fechaEstreno": "2010-07-16",
  "directorId": 1
}
```

**Response (409 CONFLICT):**
```json
{
  "type": "https://api.peliculas.com/errors/pelicula-duplicada",
  "title": "Película ya existe",
  "status": 409,
  "detail": "Esta pelicula ya existe",
  "instance": "/api/v1/peliculas"
}
```

---

### Error 409 - Actor Ya en Reparto

**Request:**
```http
POST /api/v1/peliculas/1/actores/1  // Actor ya asignado
```

**Response (409 CONFLICT):**
```json
{
  "type": "https://api.peliculas.com/errors/actor-duplicado",
  "title": "Actor ya existe en el reparto",
  "status": 409,
  "detail": "El actor 'Leonardo DiCaprio' ya está asignado a la película 'Inception'",
  "instance": "/api/v1/peliculas/1/actores/1"
}
```

---

### Error 400 - Director Menor de Edad

**Request:**
```http
POST /api/v1/peliculas
Content-Type: application/json

{
  "titulo": "Primera Película",
  "genero": "Drama",
  "fechaEstreno": "1985-01-01",  // Director nacido en 1970 = 15 años
  "directorId": 1
}
```

**Response (400 BAD REQUEST):**
```json
{
  "type": "https://api.peliculas.com/errors/director-menor-edad",
  "title": "Director menor de edad",
  "status": 400,
  "detail": "El director 'Christopher Nolan' con edad 15 no puede dirigir una pelicula estrenada en 1985 porque es menor de edad",
  "instance": "/api/v1/peliculas"
}
```

---

### Error 400 - Argumento Inválido

**Request:**
```http
POST /api/v1/actores
Content-Type: application/json

{
  "nombre": ""  // Nombre vacío
}
```

**Response (400 BAD REQUEST):**
```json
{
  "type": "https://api. peliculas.com/errors/bad-request",
  "title": "Argumento inválido",
  "status": 400,
  "detail": "Falta el campo del nombre del actor",
  "instance": "/api/v1/actores"
}
```

---

### Tabla de Códigos HTTP

| Código | Significado | Casos de Uso |
|--------|-------------|--------------|
| **200 OK** | Operación exitosa | GET, PUT exitosos, asignación de actor |
| **201 CREATED** | Recurso creado | POST exitoso de película, director o actor |
| **204 NO CONTENT** | Recurso eliminado | DELETE exitoso |
| **400 BAD REQUEST** | Datos inválidos | Director menor de edad, argumentos inválidos |
| **404 NOT FOUND** | No encontrado | Entidad no existe en la base de datos |
| **409 CONFLICT** | Conflicto | Película duplicada, actor ya en reparto |

---

## 👤 Autor

**Antonio Jesús Casado Bayón**

- 🎓 **Centro**: Salesianos Triana
- 📚 **Ciclo**: Desarrollo de Aplicaciones Multiplataforma (DAM)
- 📅 **Curso**: 2025-26
- 🐙 **GitHub**: [@ajcasadob](https://github.com/ajcasadob)
- 📁 **Repositorio**: [ApiRestGestionPeliculas](https://github.com/ajcasadob/ApiRestGestionPeliculas)

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la [Licencia MIT](LICENSE). 

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Haz un **fork** del proyecto
2.  Crea una **rama** para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Realiza tus **commits** (`git commit -m 'Añadir nueva característica'`)
4. Haz **push** a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un **Pull Request**

---

## 🔍 Características Técnicas Destacadas

### Arquitectura y Diseño
- ✅ **Arquitectura en capas** (Controller → Service → Repository)
- ✅ **Patrón DTO completo** (Request, Response, Simple)
- ✅ **Separación de responsabilidades** clara entre capas
- ✅ **Uso de Records** de Java para DTOs inmutables
- ✅ **CRUD diferenciado**: Completo para Director/Película, Básico para Actor

### Persistencia
- ✅ **Relaciones JPA correctamente configuradas**:
  - `@OneToMany` / `@ManyToOne` para Director ↔ Película
  - `@ManyToMany` para Película ↔ Actor
- ✅ **Tabla intermedia** `pelicula_actor` generada automáticamente
- ✅ **Lazy loading** para optimizar consultas
- ✅ **Cascade types** apropiados para cada relación
- ✅ **Base de datos H2** en memoria para desarrollo
- ✅ **Helper methods** (`addActor`, `removeActor`) para gestión bidireccional de la relación M:N

### Manejo de Errores
- ✅ **Sistema de excepciones jerárquico** y personalizado
- ✅ **GlobalExceptionHandler** con @RestControllerAdvice
- ✅ **Problem Details (RFC 7807)** para respuestas de error consistentes
- ✅ **Códigos HTTP apropiados** para cada tipo de error

### Validaciones
- ✅ **Validaciones de negocio** en capa de servicio
- ✅ **Director mayor de edad** calculado mediante métodos en la entidad
- ✅ **Películas con título único** (constraint en BD)
- ✅ **Actores sin duplicados** en reparto
- ✅ **Constante EDAD_MINIMA** para validación reutilizable

### Documentación
- ✅ **OpenAPI 3.0** con Swagger UI integrado
- ✅ **Anotaciones @Operation** para cada endpoint
- ✅ **Ejemplos de Request/Response** en documentación
- ✅ **Documentación de errores** con @ApiResponses

### Calidad de Código
- ✅ **Lombok** para reducir boilerplate
- ✅ **Builder pattern** para construcción de entidades
- ✅ **Métodos estáticos `of()`** en DTOs Response
- ✅ **Naming conventions** consistentes
- ✅ **Métodos de negocio en entidades** (calcularEdad, esMayorDeEdad)

---

## 📚 Recursos Adicionales

- 📖 [Documentación Spring Boot 4.0. 0](https://spring.io/projects/spring-boot)
- 📖 [Documentación Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- 📖 [SpringDoc OpenAPI Docs](https://springdoc.org/)
- 📖 [RFC 7807 - Problem Details](https://www.rfc-editor.org/rfc/rfc7807)
- 📖 [H2 Database Documentation](https://www.h2database.com/html/main.html)
- 📖 [Project Lombok](https://projectlombok.org/)

---

<div align="center">

### 🎬 API REST Gestión de Películas

**Desarrollado con ❤️ usando Spring Boot 4.0.0 y Java 21**

---

⭐ **Si este proyecto te resulta útil, dale una estrella en GitHub!** ⭐

---

🏫 **Centro Educativo**: Salesianos Triana  
📚 **Módulos**: Acceso a Datos | Programación de Servicios y Procesos

</div>
