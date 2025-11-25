> **Proyecto de Desarrollo de una API REST con Spring Boot**  
> Acceso a Datos | Programación de Servicios y Procesos  
> Curso 2025-26

## 📋 Descripción

API REST desarrollada con **Spring Boot** que permite gestionar un catálogo de películas, directores y actores, incluyendo sus relaciones y reparto.

### Características principales

- ✅ **CRUD completo** para Directores y Películas
- ✅ **CRUD básico** para Actores (Crear y Listar)
- ✅ **Relación Muchos a Muchos** entre Películas y Actores (Reparto)
- ✅ **Validaciones de negocio** (edad del director, títulos únicos, etc.)
- ✅ **Gestión centralizada de excepciones** con ProblemDetail (RFC 7807)
- ✅ **Documentación OpenAPI 3.0** con Swagger UI
- ✅ **DTOs anidados** para respuestas estructuradas

---

## 🏗️ Modelo de Datos

### Entidades

```
Director (1) ──── dirige ───> (N) Película (N) ──── reparto ───> (N) Actor
```

#### **Director**
- `id` (Long, PK)
- `nombre` (String)
- `anioNacimiento` (Integer)

#### **Película**
- `id` (Long, PK)
- `titulo` (String, único)
- `genero` (String)
- `fechaEstreno` (LocalDate)
- `director` (Relación ManyToOne)
- `actores` (Relación ManyToMany)

#### **Actor**
- `id` (Long, PK)
- `nombre` (String)

---

## 🚀 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 4.0.0**
  - Spring Web
  - Spring Data JPA
- **Base de datos H2** (en memoria)
- **Lombok** (reducción de código boilerplate)
- **SpringDoc OpenAPI** (documentación automática)
- **Maven** (gestión de dependencias)

---
