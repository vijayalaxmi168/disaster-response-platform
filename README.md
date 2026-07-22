# Enterprise Smart Disaster Response Platform

A microservices-based disaster response platform built with **Spring Boot**, **Spring Cloud Gateway**, **Eureka**, **OpenFeign**, and a **React** frontend. Citizens raise rescue requests, volunteers get assigned to them, rescued people get placed into shelters, and everyone gets notified by email at each step.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2.5 |
| Service discovery | Netflix Eureka |
| Gateway | Spring Cloud Gateway |
| Inter-service calls | OpenFeign |
| Persistence | Spring Data JPA + PostgreSQL |
| API docs | springdoc-openapi (Swagger UI) |
| Frontend | React 18 + Vite + React Router + Axios |
| Build | Maven (multi-module) |

## Project Structure

disaster-response-platform/
├── pom.xml ← parent Maven POM (aggregator)
├── eureka-server/ (port 8761)
├── api-gateway/ (port 8080)
├── user-service/ (port 8081)
├── rescue-request-service/ (port 8082)
├── volunteer-service/ (port 8083)
├── shelter-service/ (port 8084)
├── notification-service/ (port 8085)
└── frontend/ (Vite React app, port 5173)


## Prerequisites

- Java 17+ (JDK)
- Maven 3.8+
- React.js 18 and npm
- PostgreSQL 14+ installed locally

## 1. Set Up PostgreSQL

Create the 5 databases (`user_db`, `rescue_db`, `volunteer_db`, `shelter_db`, `notification_db`) using `docker/init-db.sql` as a reference, and update the `spring.datasource.username` / `password` in each service's `application.yml` if your credentials differ.

## 2. Build Everything

From the project root:
```bash
mvn clean install
```

## 3. Run the Services (in this order)

Open a separate terminal per service:

```bash
# 1. Eureka Server — wait for it to fully start before continuing
cd eureka-server && mvn spring-boot:run

# 2. API Gateway
cd api-gateway && mvn spring-boot:run


```

Confirm everything registered correctly at the Eureka dashboard: **http://localhost:8761**
You should see all 6 apps (API-GATEWAY + 5 services... eureka-server itself doesn't register).

## 4. Run the Frontend

```bash
cd frontend
npm install
npm run dev
```

## Using the App

1. Register a **Citizen** account → log in → submit a rescue request.
2. Register a **Volunteer** account → log in → create your volunteer profile → accept a pending request.
3. Register an **Admin** account → log in → add a shelter, assign rescued people to it, and mark completed requests.
4. Watch each service's console logs — the Notification Service prints a `SIMULATED EMAIL` block every time a notification fires, so you can see the full flow without needing real SMTP credentials.

## API Documentation (Swagger)

Each business service exposes Swagger UI directly (bypassing the gateway is fine for docs):
- User Service: http://localhost:8081/swagger-ui.html
- Rescue Request Service: http://localhost:8082/swagger-ui.html
- Volunteer Service: http://localhost:8083/swagger-ui.html
- Shelter Service: http://localhost:8084/swagger-ui.html
- Notification Service: http://localhost:8085/swagger-ui.html

## Sending Real Emails (optional)

By default, `notification-service` **simulates** emails (logs them + saves to DB) so the project runs with zero external setup. To send real emails:

1. Open `notification-service/src/main/resources/application.yml`
2. Set `notification.email.mode` to `real`
3. Fill in `spring.mail.username` / `spring.mail.password` with a real SMTP account (e.g. a Gmail address + App Password)

## Key REST Endpoints (via API Gateway on :8080)

| Method | Path | Description |
|---|---|---|
| POST | `/api/users/register` | Register (Admin/Volunteer/Citizen) |
| POST | `/api/users/login` | Login |
| POST | `/api/rescue-requests` | Citizen creates a rescue request |
| GET | `/api/rescue-requests/status/{status}` | List by status (PENDING/ASSIGNED/COMPLETED) |
| PUT | `/api/rescue-requests/{id}/status` | Update request status |
| POST | `/api/volunteers` | Create volunteer profile |
| PUT | `/api/volunteers/{volunteerId}/assign/{requestId}` | Assign volunteer to a request |
| POST | `/api/shelters` | Add a shelter |
| PUT | `/api/shelters/{id}/assign` | Assign rescued people to a shelter |
| POST | `/api/notifications/send` | Send a notification (used internally by Feign) |

