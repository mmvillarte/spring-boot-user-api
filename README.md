# User Microservice

Microservicio desarrollado con **Spring Boot 3.x** y **Java 21** que implementa operaciones CRUD sobre usuarios utilizando un enfoque **CQRS** (Command Query Responsibility Segregation).

---

## 🔖 Badges

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)
[![Java](https://img.shields.io/badge/Java-21-blue)](#)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)](#)

> ⚠️ Actualmente se analiza con **SonarLint** localmente.

---

## 🏗️ Arquitectura

### Flujos de negocio (High-Level)

#### Crear usuario
[[Create User](./docs/architecture/high-level/createUserHighLevel.svg)](./docs/architecture/high-level/createUserHighLevel.svg)

#### Actualizar usuario
[[Update User](./docs/architecture/high-level/updateUserHighLevel.svg)](./docs/architecture/high-level/updateUserHighLevel.svg)

#### Eliminar usuario
[[Delete User](./docs/architecture/high-level/deleteUserHighLevel.svg)](./docs/architecture/high-level/deleteUserHighLevel.svg)

---

### Flujos técnicos (detalle)

#### Crear usuario
[[Create User Detail](./docs/architecture/createUser.svg)](./docs/architecture/createUser.svg)

#### Actualizar usuario
[[Update User Detail](./docs/architecture/updateUser.svg)](./docs/architecture/updateUser.svg)

#### Eliminar usuario
[[Delete User Detail](./docs/architecture/deleteUser.svg)](./docs/architecture/deleteUser.svg)

---

## 🚀 Ejecución

### Requisitos

- Java 21
- Maven 3.9+
- IDE con SonarLint (opcional)

### Ejecutar aplicación

```bash
# Usando Maven
mvn spring-boot:run

# Usando aplication class
mvn compile exec:java -Dexec.mainClass=org.exercise.user.UserApplication

# Ejecutar tests
mvn test
```

## 📡 Endpoints principales

| Método | Endpoint    | Descripción | Body Request                                                                                         | Response  |
|--------|-------------|------------|------------------------------------------------------------------------------------------------------|-------------------|
| POST   | /users      | Crear usuario | `{"firstname":"Maria","firstname":"Mar","email":"maria@example.com","dni":"daniValido"}`             | `201 Created {"code": 200,"message": "User successfully updated","data": {"id": "2b65ea8d-1fe4-4a1a-90b6-acbf130ad91f","firstName": "Maria","lastName": "Mar","email": "maria@example.test","dni": "29117868Y"}}` |
| PUT    | /users?id=uuid | Actualizar usuario | `{"firstname":"Maria","firstname":"del Mar","email":"maria-del-mar@example.com","dni":"daniValido"}` | `200 OK {"code": 200,"message": "User successfully updated","data": {"id": "2b65ea8d-1fe4-4a1a-90b6-acbf130ad91f","firstName": "Maria","lastName": "del Mar","email": "maria-del-mar@example.test","dni": "29117868Y"}}`      |
| DELETE | /users?id=uuid      | Eliminar usuario | N/A                                                                                                  | `204 No Content`  |