#  Football Club Management System API

A RESTful API developed for the Football Club Management System. This project is designed to manage the club's departments and staff members.

---

##  Technologies

* **Java**: 21
* **Framework**: Spring Boot 3.3.0
* **Database**: PostgreSQL 16
* **ORM**: Spring Data JPA / Hibernate
* **Containerization**: Docker & Docker Compose
* **Documentation**: Springdoc OpenAPI (Swagger UI)
* **Testing**: JUnit 5 & Mockito
* **Build Tool**: Gradle

---

##  Configuration (Example application.yml)

Example configuration of the `src/main/resources/application.yml` file:

```jaml
spring: 
applications: 
name: FootballClubManagement 

data source: 
url: jdbc:postgresql://localhost:5432/football_club_db 
username: postgres 
password: postgrespassword 
driver-class-name: org.postgresql.Driver 

jpa: 
hibernate: 
ddl-auto: update
show-sql: true
properties:
hibernate:
format_sql: true

springdoc:
api-docs:
path: /v3/api-docs
swagger-ui:
path: /swagger-ui.html
enabled: true
```

---

## Installation Steps
1. Loading the Database (with Docker)
   To start the PostgreSQL database in a Docker container, run this command in the terminal:
```Bash

docker-compose up -d
```

2. Running the Application
   Run the project via Gradle:

```Bash

./gradlew bootRun
```

The application will run on port http://localhost:8080.

API Documentation (Swagger / OpenAPI)
To interactively view and test all endpoints, access the following link in your browser after the application is launched:

http://localhost:8080/swagger-ui/index.html

## Authentication & Authorization (Week 2 Updates)

A role-based security system has been implemented in the project using **Spring Security** and **JWT (JSON Web Tokens)**.

### 🛠 Security Architecture
- **Password Encoding:** User passwords are not stored in plain-text in the database, they are hashed with `BCryptPasswordEncoder`.
- **Stateless Session:** The application runs in stateless mode, each protected request is checked with the `Authorization: Bearer <token>` header.
- **Config-Driven Secrets:** JWT configurations (`secret` and `expiration`) are managed from the `application.yml` file.

---

### Roles & Permissions
There are 2 main security roles in the system: `USER` and `ADMIN`.

| Endpoint Pattern | HTTP Method | Accessible Roles | Description |
| :--- | :---: | :---: | :--- |
| `/api/v1/auth/**` | POST | `Public` | Registration and Login endpoints |
| `/swagger-ui/**`, `/v3/api-docs/**` | GET | `Public` | Swagger API documentation |
| `/api/v1/**` | GET | `USER`, `ADMIN` | Read data (Read) |
| `/api/v1/**` | POST, PUT, DELETE | `ADMIN` | Create, modify, delete data (Write) |

---

### Exception Handling (HTTP Status Codes)
Following proper REST semantics, errors are divided into 2 categories:

1. **`401 Unauthorized` (`CustomAuthenticationEntryPoint`)**
- Returned when the user is not logged in, does not send a token, or the token has expired.
2. **`403 Forbidden` (`CustomAccessDeniedHandler`)**
- Returned when the user is logged in (e.g., in the `USER` role), but accesses a resource for which they do not have permission (e.g., the `POST` endpoint belonging to `ADMIN`).

---

### Authentication API Endpoints

#### 1. Registration (`POST /api/v1/auth/register`)
```json
{ 
"username": "admin1", 
"email": "admin@club.com", 
"password": "password123", 
"role": "ADMIN"
}
```

#### 2. Login (POST /api/v1/auth/login)
```json
{
"username": "admin1",
"password": "password123"
}
```