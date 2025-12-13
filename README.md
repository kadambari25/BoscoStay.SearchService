# BoscoStay – Search Service

This repository contains the Search Service microservice for the BoscoStay application.

The Search Service is responsible for searching and returning available apartments based on city and date range.

---

## Tech Stack
- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL (Docker)
- RabbitMQ (Docker)
- Maven
- Swagger / OpenAPI
- GitHub Actions

---

## How to Run the Service (Local)

1. Start Docker containers:
   docker compose up -d

2. Run the Spring Boot application:
   mvn spring-boot:run

---

## API Endpoint

GET /api/search/available

Example:
http://localhost:8080/api/search/available?city=Paris&checkIn=2025-01-01&checkOut=2025-01-05

---

## Swagger UI

http://localhost:8080/swagger-ui.html

---

## RabbitMQ
- URL: http://localhost:15672
- Username: guest
- Password: guest

---

## Database
- PostgreSQL (Docker)
- Database name: searchdb

---

## Author
Kadambari Suresh
