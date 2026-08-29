# Car Insurance Management System - Backend

A **Spring Boot** recreation of a Car Insurance Management System previously developed with **FastAPI**.

The project is being rebuilt in Java to deepen my understanding of Spring Boot and apply backend development concepts in a different technology stack while maintaining a similar application structure and functionality.

## Current Features

* RESTful API for managing vehicle owners, cars, insurance policies and claims
* CRUD operations
* Request and response DTOs
* Input validation
* Exception handling
* Structured error responses
* Relational database integration with PostgreSQL
* Layered architecture
* JPA entity relationships
* Repository-based database access with Spring Data JPA
* MapStruct-based DTO/entity mapping
* Dynamic car filtering using JPA Specifications
* Car history combining insurance policies and claims
* Filtering car history by type (`POLICY` / `CLAIM`)
* Chronological sorting of car history
* User registration and login
* Password hashing with BCrypt
* JWT-based authentication
* Spring Security integration
* Swagger/OpenAPI documentation with JWT authentication support

## Technologies

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* PostgreSQL
* Lombok
* MapStruct
* Jakarta Validation
* JWT
* Swagger/OpenAPI
* Maven
* Git

## Architecture

The project follows a layered architecture with separate responsibilities for:

* **Controllers** - handle HTTP requests and responses
* **Services** - contain business logic
* **Repositories** - handle database access
* **Entities** - represent database entities
* **DTOs** - define API request and response models
* **Mappers** - convert between DTOs and entities
* **Specifications** - implement dynamic filtering and querying
* **Exceptions** - handle application-specific errors
* **Enums** - represent domain-specific states and types
* **Security** - handle authentication and JWT validation

## Implemented Modules

The following modules have been implemented:

* **Owner**

  * CRUD operations
  * DTOs and validation
  * Entity/DTO mapping
  * Exception handling

* **Car**

  * CRUD operations
  * DTOs and validation
  * Entity/DTO mapping
  * Filtering using JPA Specifications
  * Exception handling

* **Insurance Policy**

  * CRUD operations
  * DTOs and validation
  * Entity/DTO mapping
  * Policy status handling
  * Exception handling

* **Claim**

  * CRUD operations
  * DTOs and validation
  * Entity/DTO mapping
  * Exception handling

* **Car History**

  * Combines insurance policies and claims into a unified response
  * Supports filtering by `POLICY` or `CLAIM`
  * Sorts history entries chronologically
  * Validates that the requested car exists

* **Authentication**

  * User registration
  * User login
  * BCrypt password hashing
  * JWT generation and validation
  * JWT authentication filter
  * Protected API endpoints using Spring Security

## Development Status

The core functionality for owners, cars, insurance policies, claims and car history has been implemented.

**User authentication using Spring Security, BCrypt and JWT has also been implemented.**

The project is currently being extended with additional production-style backend features.

### Next Steps

The next stage of development will focus on:

1. **Authorization**

   * Role-based access control
   * User roles and permissions
   * Restricting specific operations based on user roles

2. **Automated Testing**

   * Unit tests with JUnit and Mockito
   * Integration tests for REST endpoints
   * Tests for core business functionality such as car history, claims and insurance validity

3. **Business Rules**

   * Prevent overlapping insurance policies for the same car
   * Validate policy date ranges

4. **Background Processing**

   * Scheduled detection of expired insurance policies
   * Expiry logging while preventing duplicate logs

5. **Additional Improvements**

   * Request ID / correlation ID
   * Improved application logging
   * Additional test coverage
   * Further security improvements

## Purpose

This project is a hands-on migration/reimplementation exercise designed to strengthen my **Java and Spring Boot backend development skills** by recreating an application previously built with FastAPI.

The goal is not only to reproduce the existing functionality, but also to progressively introduce common backend practices such as **authentication, authorization, automated testing, business-rule validation and scheduled background processing**.
