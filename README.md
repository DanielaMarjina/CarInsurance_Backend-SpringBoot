# Car Insurance Management System - Backend

A **Spring Boot** recreation of a Car Insurance Management System previously developed with **FastAPI**.

The project is being rebuilt in Java to deepen my understanding of Spring Boot and apply backend development concepts in a different technology stack while maintaining similar functionality and architecture.

## Current Features

- RESTful API for managing vehicle owners, cars, insurance policies and claims
- CRUD operations
- Request and response DTOs
- Input validation using Jakarta Validation
- Centralized exception handling and structured error responses
- PostgreSQL database integration
- Docker-based PostgreSQL environment
- Automatic PostgreSQL container startup through Spring Boot Docker Compose integration
- Persistent database storage using Docker volumes
- Layered architecture
- JPA entity relationships
- Repository-based data access with Spring Data JPA
- MapStruct-based DTO/entity mapping
- Dynamic car filtering using JPA Specifications
- Car history combining insurance policies and claims
- Car history filtering by type (`POLICY` / `CLAIM`)
- Chronological sorting of car history
- User registration and login
- Password hashing with BCrypt
- JWT-based authentication
- Role-based authorization using Spring Security
- Role-based endpoint access control
- Swagger/OpenAPI documentation with JWT authentication support

## Technologies

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker
- Docker Compose
- Lombok
- MapStruct
- Jakarta Validation
- JSON Web Tokens (JWT)
- JUnit
- Mockito
- H2 Database for integration testing
- Swagger/OpenAPI
- Maven
- Git

## Architecture

The project follows a layered architecture with separate responsibilities:

- **Controllers** — handle HTTP requests and responses
- **Services** — implement application and business logic
- **Repositories** — handle database access
- **Entities** — represent database entities
- **DTOs** — define API request and response models
- **Mappers** — convert between DTOs and entities
- **Specifications** — implement dynamic filtering and querying
- **Exceptions** — handle application-specific errors
- **Enums** — represent domain-specific states and types
- **Security** — handle authentication, JWT validation and authorization

## Implemented Modules

### Owner

- CRUD operations
- Request validation
- Entity/DTO mapping
- Exception handling
- Repository-based persistence

### Car

- CRUD operations
- Request validation
- Entity/DTO mapping
- Dynamic filtering using JPA Specifications
- Owner association management
- Exception handling

### Insurance Policy

- CRUD operations
- Policy status handling based on policy dates
- Request validation
- Entity/DTO mapping
- Insurance policy date-range validation
- Prevention of overlapping insurance policies for the same car
- Custom exception handling for overlapping policies
- Policy validity checking for a specific date
- Retrieval of the currently active policy for a car
- Scheduled policy status updates for expired and upcoming policies

### Claim

- CRUD operations
- Request validation
- Entity/DTO mapping
- Exception handling

### Car History

- Combines insurance policies and claims into a unified response
- Supports filtering by `POLICY` or `CLAIM`
- Sorts history entries chronologically
- Validates that the requested car exists

### Authentication & Authorization

- User registration
- User login
- BCrypt password hashing
- JWT generation and validation
- JWT authentication filter
- Role-based access control
- Endpoint authorization using Spring Security

## Docker Compose Integration

PostgreSQL is managed as a Docker Compose service instead of requiring a local PostgreSQL installation.

- Uses the official PostgreSQL 16 Docker image
- Defines database configuration through environment variables
- Stores database credentials in a local `.env` file excluded from Git
- Uses an existing external Docker volume for persistent database storage
- Integrates Spring Boot with Docker Compose through `spring-boot-docker-compose`
- Automatically starts the PostgreSQL container when the Spring Boot application starts
- Waits for the database to become healthy before establishing the application connection

This simplifies the local development setup and makes the database environment easier to reproduce.

## Automated Testing

Automated testing has been implemented across the main application layers.

- **Service unit tests** using JUnit and Mockito
- Tests for successful execution and exceptional cases
- Verification of service interactions with repositories and mappers
- **Controller tests** for REST endpoint behavior, request validation and authorization
- **Security tests** for JWT authentication, token validation and user loading
- **Integration tests** using Spring Boot and an H2 in-memory database
- Integration tests verifying owner persistence, owner lookup and car persistence with its owner relationship

## Development Status

The core functionality for owners, cars, insurance policies, claims and car history has been implemented.

Authentication and authorization using Spring Security, BCrypt, JWT and role-based access control have also been implemented.

Insurance policy business rules include date-range overlap prevention and scheduled status transitions based on policy dates.

Docker Compose integration is configured to automatically start PostgreSQL during local application startup while preserving database data through a persistent volume.

Automated testing covers the current application scope. Development is now moving toward additional business rules and production-oriented backend improvements.

## Next Steps

### 1. Business Rules
- Add additional domain-specific validations

### 2. Background Processing
- Improve scheduled policy expiration handling and logging
- Prevent duplicate expiration logs

### 3. Additional Improvements
- Request ID / correlation ID
- Improved application logging
- Further security improvements
- Pagination and sorting for collection endpoints
- Docker Compose configuration for the full application stack (backend, frontend and PostgreSQL)
- Additional production-oriented refinements

## Purpose

This project is a hands-on migration and reimplementation exercise designed to strengthen my **Java and Spring Boot backend development skills** by recreating an application previously built with FastAPI.

The goal is not only to reproduce the existing functionality, but also to apply common backend development practices, including layered architecture, REST API design, authentication, authorization, automated testing, relational database integration, containerization and business-rule validation.
