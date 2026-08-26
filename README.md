# Skivo Airlines

A production-style airline booking platform built as a distributed Spring Boot application. The project models core airline operations through independently deployable microservices and focuses on scalability, asynchronous communication, service discovery, security, resilience, and performance.

## Architecture

The system is organized around independently deployable services communicating through REST APIs and asynchronous Kafka events.

### Platform Services

* **API Gateway**: Central entry point for client requests and cross-cutting concerns
* **Service Registry**: Eureka-based service discovery
* **Config Server**: Centralized configuration management
* **Common Library**: Shared models and utilities used across services

### Business Services

* **User Service**: User management and authentication
* **Airline Core Service**: Core airline and booking domain operations
* **Flight Operations Service**: Flight and operational management
* **Location Service**: Airport and location data
* **Booking Service**: Booking lifecycle and reservation management
* **Seat Service**: Seat availability and allocation
* **Pricing Service**: Dynamic pricing and fare management
* **Ancillary Service**: Additional services associated with bookings
* **Payment Service**: Payment processing workflow
* **Notification Service**: Asynchronous customer notifications

## Key Engineering Features

### Microservices Architecture

The application is decomposed into domain-focused services with independent business logic, data access, and deployment boundaries.

### Event-Driven Communication

Kafka is used for asynchronous communication between services where synchronous request-response communication is not required.

### Service Discovery

Eureka provides service registration and discovery, allowing services to communicate without relying on hardcoded service locations.

### Centralized Configuration

Spring Cloud Config Server provides centralized configuration management across the microservices.

### API Gateway

A dedicated API Gateway provides a single entry point into the system and handles request routing and security concerns.

### Security

Spring Security is used to secure application APIs and control access to protected resources.

### Redis Caching

Redis is used to cache frequently accessed data and reduce repeated database and downstream service calls for latency-sensitive operations.

### Fault Tolerance

Resilience patterns such as circuit breaking are used to prevent failures in one service from cascading through dependent services.

### Performance Engineering

The application includes performance-focused improvements such as database indexing, pagination, asynchronous processing, parallel execution of independent service calls, and application thread pool tuning.

### Containerized Infrastructure

Docker Compose is provided for local infrastructure setup, including Kafka. The project is structured to support running the distributed system as a collection of containerized services.

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Cloud
* Spring Web
* Spring Data JPA
* Spring Security

### Distributed Systems

* Apache Kafka
* Netflix Eureka
* Spring Cloud Config
* Spring Cloud Gateway
* Redis

### Infrastructure

* Docker
* Docker Compose
* Maven

## Project Structure

```text
skivo-airlines-project/
├── cloud/
│   ├── api-gateway/
│   ├── config-server/
│   └── service-registry/
│
├── common-lib/
│
├── services/
│   ├── airline-core-service/
│   ├── ancillary-service/
│   ├── booking-service/
│   ├── flight-ops-service/
│   ├── location-service/
│   ├── notification-service/
│   ├── payment-service/
│   ├── pricing-service/
│   ├── seat-service/
│   └── user-service/
│
└── docker-compose/
```

## Running the Project

### Prerequisites

* Java 17
* Maven
* Docker
* Docker Compose

### Start Infrastructure

```bash
cd docker-compose
docker compose up -d
```

### Build the Project

From the project root:

```bash
mvn clean install
```

Individual services can then be started from their respective modules.

## Architecture Goals

The project was built to explore practical backend engineering concerns beyond basic CRUD development, including:

* Designing domain-oriented microservices
* Choosing between synchronous and asynchronous communication
* Handling distributed service dependencies
* Improving API latency and throughput
* Managing service failures and degraded dependencies
* Applying caching to reduce downstream load
* Securing distributed APIs
* Containerizing supporting infrastructure
* Measuring and improving application performance

## Project Status

This project is a continuously evolving learning and engineering project focused on applying production-oriented backend architecture and performance practices with the Spring ecosystem.
