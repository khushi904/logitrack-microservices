🚚 LogiTrack – Shipment Tracking Microservices

LogiTrack is a Spring Boot microservices-based shipment management and tracking system designed to demonstrate real-world backend development concepts such as JWT authentication, role-based authorization, service-to-service communication, API Gateway, MongoDB, OTP-based authentication, and notification services.

The project follows a modular microservices architecture where each service is responsible for a specific business capability.

⸻

🏗️ Architecture

                         ┌──────────────────────┐
                         │       Client         │
                         │  Postman / Frontend  │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │     API Gateway      │
                         │        :8080         │
                         │  JWT Authentication │
                         └──────────┬───────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    │                               │
                    ▼                               ▼
          ┌──────────────────┐             ┌──────────────────┐
          │   Auth Service   │             │ Shipment Service │
          │      :8081       │◄────────────│      :8082       │
          │                  │   Feign     │                  │
          │ JWT + OTP + User │             │ Shipment + Track │
          │ Role Management  │             │ Status Management│
          └────────┬─────────┘             └────────┬─────────┘
                   │                                │
                   │                                │ RestClient
                   │                                ▼
                   │                       ┌──────────────────┐
                   │                       │ Notification     │
                   │                       │ Service :8083    │
                   │                       │                  │
                   │                       │ Email Notification│
                   │                       └────────┬─────────┘
                   │                                │
                   └──────────────┬─────────────────┘
                                  ▼
                         ┌──────────────────┐
                         │     MongoDB      │
                         └──────────────────┘

⸻

✨ Key Features

* 🔐 JWT-based authentication
* 👥 Role-based authorization
* 📧 Email-based OTP authentication
* 👤 User registration and profile management
* 🚚 Shipment creation and management
* 📍 Shipment tracking and tracking events
* 🔄 Shipment status management
* 📩 Email notification service
* 🔗 Inter-service communication using Feign Client
* 🌐 REST-based communication using RestClient
* 🚪 API Gateway for centralized request handling
* 🛡️ JWT validation at Gateway and service level
* 🗄️ MongoDB database integration
* ⚠️ Global exception handling
* ✅ Input validation
* 🧪 JUnit 5 and Mockito for testing

⸻

🧩 Microservices

1. API Gateway

Port: 8080

The API Gateway acts as the single entry point for client requests.

Responsibilities

* Routes requests to appropriate microservices
* Validates JWT tokens
* Extracts authentication information from the token
* Prevents unauthorized requests from reaching downstream services

⸻

2. Auth Service

Port: 8081

Responsible for authentication and user management.

Responsibilities

* Email-based OTP generation and verification
* User creation
* JWT token generation
* User profile management
* User roles and status management
* Authentication and authorization

Roles

CUSTOMER
ADMIN
DELIVERY_AGENT

⸻

3. Shipment Service

Port: 8082

Core business service responsible for shipment management.

Responsibilities

* Create shipments
* Retrieve shipment information
* Update shipment status
* Assign delivery agents
* Track shipment events
* Validate authenticated users
* Communicate with Auth Service
* Trigger notifications

Example Shipment Flow

Create Shipment
      ↓
Shipment Created
      ↓
Shipment Status Updated
      ↓
Tracking Event Created
      ↓
Notification Triggered
      ↓
Email Sent

⸻

4. Notification Service

Port: 8083

Responsible for sending notifications.

Responsibilities

* Receive notification requests
* Send email notifications
* Store notification information
* Handle notification-related exceptions

The Shipment Service communicates with the Notification Service using RestClient.

⸻

🔐 Authentication Flow

LogiTrack uses JWT-based authentication.

User
 │
 │ Request OTP
 ▼
Auth Service
 │
 │ OTP sent to email
 ▼
User verifies OTP
 │
 ▼
Auth Service
 │
 │ Generates JWT
 ▼
JWT Token
 │
 ▼
Client
 │
 │ Authorization: Bearer <token>
 ▼
API Gateway
 │
 │ JWT validation
 ▼
Shipment Service
 │
 │ JWT validation
 ▼
Protected API

JWT contains authentication information that can be used by the application to identify the authenticated user and enforce authorization rules.

⸻

👥 Role-Based Authorization

The application supports role-based access control using Spring Security.

Example:

@PreAuthorize("hasRole('ADMIN')")

Different operations can be restricted based on the authenticated user’s role.

Example roles:

Role	Responsibility
CUSTOMER	Create and track shipments
ADMIN	Manage shipments and users
DELIVERY_AGENT	Handle assigned deliveries

⸻

🔗 Service-to-Service Communication

LogiTrack demonstrates multiple approaches for communication between microservices.

Feign Client

Used for communication between:

Shipment Service → Auth Service

Feign provides a declarative approach for calling another REST service.

RestClient

Used for:

Shipment Service → Notification Service

RestClient provides a programmatic way to make HTTP requests between services.

⸻

🗄️ Database

LogiTrack uses MongoDB for persistence.

MongoDB stores data related to:

* Users
* OTP verification
* Shipments
* Tracking events
* Notifications

Each service can maintain its own data access layer and business logic.

⸻

🛠️ Tech Stack

Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data MongoDB
* Spring Cloud Gateway
* OpenFeign
* RestClient
* JWT
* Maven

Database

* MongoDB

Testing

* JUnit 5
* Mockito

Development Tools

* IntelliJ IDEA
* Postman
* Git
* GitHub

⸻

📁 Project Structure

Logitrack/
│
├── api-gateway/
│   └── src/
│
├── auth-service/
│   └── src/
│
├── shipment-service/
│   └── src/
│
├── notification-service/
│   └── src/
│
├── .gitignore
└── README.md

⸻

🚀 Getting Started

Prerequisites

Make sure the following are installed:

* Java 17+
* Maven
* MongoDB
* Git
* IntelliJ IDEA
* Postman

⸻

Clone the Repository

git clone https://github.com/khushi904/logitrack-microservices.git

Navigate into the project:

cd logitrack-microservices

⸻

🔑 Environment Variables

Sensitive configuration such as JWT secrets, MongoDB connection strings, and email credentials should be provided through environment variables.

Example:

JWT_SECRET=your-secret
MONGODB_URI=your-mongodb-uri
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-app-password

⚠️ Never commit actual secrets, passwords, API keys, or .env files to GitHub.

⸻

▶️ Running the Services

Start each service from IntelliJ IDEA or using Maven.

API Gateway

cd api-gateway
./mvnw spring-boot:run

Auth Service

cd auth-service
./mvnw spring-boot:run

Shipment Service

cd shipment-service
./mvnw spring-boot:run

Notification Service

cd notification-service
./mvnw spring-boot:run

⸻

🧪 API Testing

The APIs can be tested using Postman.

Typical flow:

1. Send OTP
      ↓
2. Verify OTP
      ↓
3. Receive JWT
      ↓
4. Add JWT to Authorization header
      ↓
5. Create Shipment
      ↓
6. Update Shipment Status
      ↓
7. Track Shipment
      ↓
8. Trigger Email Notification

Authorization Header

For protected APIs:

Authorization: Bearer <JWT_TOKEN>

⸻

📌 Example Shipment

Example shipment information:

{
  "receiverName": "Rahul Sharma",
  "pickupLocation": "Vijay Nagar, Indore",
  "deliveryLocation": "Bandra West, Mumbai",
  "packageType": "ELECTRONICS",
  "weight": 2.5
}

⸻

📊 Shipment Lifecycle

A shipment can move through different stages of its lifecycle.

CREATED
   ↓
ASSIGNED
   ↓
PICKED_UP
   ↓
IN_TRANSIT
   ↓
OUT_FOR_DELIVERY
   ↓
DELIVERED

Invalid status transitions are handled through exception handling and appropriate API responses.

⸻

🛡️ Exception Handling

The application uses centralized exception handling to provide consistent API responses.

Examples include:

* Invalid request
* Unauthorized access
* Forbidden access
* Shipment not found
* Invalid shipment status
* Authentication failures
* Validation errors

⸻

🎯 Learning Objectives

This project was built to demonstrate practical understanding of:

* Microservices architecture
* Spring Boot
* Spring Security
* JWT authentication
* Role-based authorization
* REST APIs
* API Gateway
* Inter-service communication
* Feign Client
* RestClient
* MongoDB
* Exception handling
* Input validation
* Unit testing
* Clean service-layer architecture

⸻

🔮 Future Enhancements

Possible future improvements include:

* Apache Kafka for event-driven communication
* Resilience4j circuit breaker and retry mechanisms
* Service discovery using Eureka
* Distributed tracing
* Centralized configuration
* Docker Compose for running all services together
* API documentation with Swagger/OpenAPI
* Advanced shipment analytics
* Delivery tracking improvements

⸻

👩‍💻 Author

Khushi Verma

Java Developer | Spring Boot | Microservices

GitHub:
https://github.com/khushi904

⸻

⭐ Project

If you find this project useful for learning Spring Boot and Microservices, feel free to explore the repository.
