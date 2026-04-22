# Skynews (Spring Boot)

This is a Spring Boot 3 application with a simple **User** module (CRUD-ish) backed by **PostgreSQL** (Liquibase-managed schema).

## Requirements

- Java 21+
- Maven 3.9+
- Docker Desktop (for running the app + Postgres stack)

## Run locally (without Docker)

If you run the app on your host machine and Postgres in Docker:

0. Copy environment template:

```bash
cp .env.example .env
```

1. Start Postgres:

```bash
docker compose -f docker/docker-compose.yaml up postgres
```

2. Run the app:

```bash
mvn spring-boot:run
```

The default datasource points to `localhost:15432`.

## Run with Docker (app + postgres)

```bash
docker compose -f docker/docker-compose.yaml up --build
```

App is exposed on `http://localhost:8080`.

## API Docs

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints

- `POST /api/v1/users` (Basic auth required)
- `GET /api/v1/users/{id}` (public)
- `GET /api/v1/users/username/{username}` (public)
- `PUT /api/v1/users/{id}` (Basic auth required)
- `POST /api/v1/users/{userId}/projects` (Basic auth required)
- `GET /api/v1/users/{userId}/projects` (public)
- `GET /api/v1/users/{userId}/projects/{projectId}` (public)
- `DELETE /api/v1/users/{userId}/projects/{projectId}` (Basic auth required)

Basic auth credentials for local development:

- username: `api-admin`
- password: `api-admin-password`

## Tests

Run all tests:

```bash
mvn test
```