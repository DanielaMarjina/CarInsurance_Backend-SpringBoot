# Car Insurance Management System - Backend

A **Spring Boot** recreation of a Car Insurance Management System previously developed with **FastAPI**.

The project is being rebuilt in Java to deepen my understanding of Spring Boot and apply backend development concepts in a different technology stack while maintaining a similar application structure and functionality.

## Current Features

* RESTful API for managing vehicle owners
* CRUD operations
* Request and response DTOs
* Input validation
* Exception handling
* Structured error responses
* Relational database integration
* Layered architecture

## Technologies

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Lombok
* MapStruct
* Jakarta Validation
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
* **Exceptions** - handle application-specific errors

## Development Status

The project is currently under development. The owner and car management module has been implemented, including its DTOs, validation, mapping, service and repository layers, exception handling, and REST endpoints.

The remaining modules will progressively recreate the functionality of the original FastAPI application.

## Purpose

This project is a hands-on migration/reimplementation exercise designed to strengthen my Java and Spring Boot backend development skills by recreating an application previously built with FastAPI.
