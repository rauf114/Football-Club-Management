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