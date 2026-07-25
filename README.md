# ⚽ Football Club Management System API

Football Club Management System üçün hazırlanmış RESTful API. Bu layihə klubun departamentlərini və heyət üzvlərini idarə etmək üçün nəzərdə tutulub.

---

## 🚀 Texnologiyalar

* **Java**: 21
* **Framework**: Spring Boot 3.3.0
* **Database**: PostgreSQL 16
* **ORM**: Spring Data JPA / Hibernate
* **Containerization**: Docker & Docker Compose
* **Documentation**: Springdoc OpenAPI (Swagger UI)
* **Testing**: JUnit 5 & Mockito
* **Build Tool**: Gradle

---

## ⚙️ Konfiqurasiya (application.yml Nümunəsi)

`src/main/resources/application.yml` faylının nümunəvi tənzimlənməsi:

```yaml
spring:
  application:
    name: FootballClubManagement

  datasource:
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

## Quraşdırma Addımları
1. Baza Yükləməsi (Docker ilə)
   PostgreSQL verilənlər bazasını Docker konteynerində işə salmaq üçün terminalda bu əmri icra edin:
```Bash

docker-compose up -d
```


2. Tətbiqi İşə Salmaq
   Proyekti Gradle vasitəsilə işə salın:

```Bash

./gradlew bootRun
```

Tətbiq http://localhost:8080 portunda fəaliyyət göstərəcək.

API Dokumentasiyası (Swagger / OpenAPI)
Bütün endpoint-ləri interaktiv şəkildə görmək və test etmək üçün tətbiq işə düşdükdən sonra brauzerdə aşağıdakı keçidə daxil olun:

http://localhost:8080/swagger-ui/index.html

## Authentication & Authorization (Week 2 Updates)

Layihədə **Spring Security** və **JWT (JSON Web Tokens)** istifadə olunaraq rol əsaslı təhlükəsizlik sistemi tətbiq olunmuşdur.

### 🛠 Security Architecture
- **Password Encoding:** İstifadəçi şifrələri bazada plain-text olaraq saxlanılmır, `BCryptPasswordEncoder` ilə hash-lənir.
- **Stateless Session:** Tətbiq stateless rejimdə çalışır, hər bir qorunan sorğu `Authorization: Bearer <token>` başlığı ilə yoxlanılır.
- **Config-Driven Secrets:** JWT konfiqurasiyaları (`secret` və `expiration`) `application.yml` faylından idarə olunur.

---

### Roles & Permissions
Sistemdə 2 əsas təhlükəsizlik rolu var: `USER` və `ADMIN`.

| Endpoint Pattern | HTTP Method | Accessible Roles | Description |
| :--- | :---: | :---: | :--- |
| `/api/v1/auth/**` | POST | `Public` | Qeydiyyat və Giriş endpoint-ləri |
| `/swagger-ui/**`, `/v3/api-docs/**` | GET | `Public` | Swagger API sənədləşməsi |
| `/api/v1/**` | GET | `USER`, `ADMIN` | Məlumatları oxumaq (Read) |
| `/api/v1/**` | POST, PUT, DELETE | `ADMIN` | Məlumatları yaratmaq, dəyişmək, silmək (Write) |

---

### Exception Handling (HTTP Status Codes)
Düzgün REST semantikasına riayət olunaraq xətalar 2 yerə ayrılmışdır:

1. **`401 Unauthorized` (`CustomAuthenticationEntryPoint`)**
    - İstifadəçi giriş etmədikdə, token göndərmədikdə və ya token-in vaxtı bitdikdə qaytarılır.
2. **`403 Forbidden` (`CustomAccessDeniedHandler`)**
    - İstifadəçi sistemə daxil olub (məsələn `USER` rolundadır), lakin icazəsi olmayan resursa (məsələn `ADMIN`-ə aid `POST` endpoint-inə) müraciət etdikdə qaytarılır.

---

###  Authentication API Endpoints

#### 1. Registration (`POST /api/v1/auth/register`)
```json
{
  "username": "admin1",
  "email": "admin@club.com",
  "password": "password123",
  "role": "ADMIN"
}