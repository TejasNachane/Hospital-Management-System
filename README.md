# Hospital Management System - Backend

A Spring Boot application for managing hospital operations including patient registration, doctor management, appointments, and prescriptions.

**Honestly the i done the security related code by chatgpt.**

the Front-End is also ready at another repo -[FrontEnd ](https://github.com/TejasNachane/HMS-FrontEnd)

## Project Structure

```
src/
├── main/
│   ├── java/com/golu/hello/backend/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   ├── util/            # Utility classes
│   │   └── BackendApplication.java
│   └── resources/
│       └── application.properties
└── test/                    # Test classes
```


## Features

- **User Authentication**: JWT-based authentication system
- **Role-based Access**: Admin, Doctor, and Patient roles
- **Patient Management**: Complete patient profile management
- **Doctor Management**: Doctor profiles with specializations
- **Appointment System**: Appointment booking and management
- **Prescription Management**: Digital prescription handling
- **RESTful APIs**:  REST API endpoints

## Technology Stack

- **Java 24**
- **Spring Boot 3.5.3**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Database
- **JWT** - Token-based authentication
- **Lombok** - Code generation
- **Maven** - Build tool

## Prerequisites

1. **Java 17** or higher
2. **MySQL 8.0** or higher
3. **Maven 3.6** or higher (or use included Maven wrapper)

## Database Setup

1. Install and start MySQL server
2. Create a database named `hospital` .
3. Update database credentials in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=root
```

## Installation & Running


### Using Maven wrapper
```cmd
mvnw.cmd clean compile
mvnw.cmd spring-boot:run
```



## Default Roles

The application automatically creates these roles on startup:
- `ADMIN` - System administrator
- `DOCTOR` - Medical practitioners
- `PATIENT` - Hospital patients

## API Endpoints

****Use PostMan** for API testing. use the Api Guide (postman guide for api).**

The application runs on `http://localhost:8082`

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register/doctor` - Doctor registration
- `POST /api/auth/register/patient` - Patient registration

### Public Endpoints (No authentication required)
- `GET /api/doctors` - Get all doctors
- `GET /api/doctors/specializations` - Get all specializations

### Protected Endpoints (JWT token required)
- All other endpoints require a valid JWT token in the Authorization header

