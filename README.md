# Microservice Booking Event
This repository contains a microservice-based application that manages event booking, payments, and notifications. 
Each service is independently responsible for its domain, and all services communicate via the API Gateway.

# Prerequisites
Ensure the following are installed on your system:

* Java: Version 22
* Docker: Latest version
* Maven: Latest version
* IDE: IntelliJ IDEA

# How to Start the Application
Follow these steps to get the application running:

Start Docker: Make sure Docker is running on your machine. 
Then, open a terminal and run the following command to start the necessary containers:
``docker-compose up -d``


Start the Microservices: Once Docker is up and running, open IntelliJ IDEA and run each of the following applications:

* DiscoveryServerApplication – For service discovery.
* ApiGatewayApplication – For routing requests to the appropriate services.
* EventServiceApplication – Handles event management.
* BookingServiceApplication – Manages event bookings.
* NotificationServiceApplication – Sends notifications for bookings and cancellation.
* PaymentServiceApplication – Processes payments for bookings.


##### **Access the API Gateway:** 
All services communicate through the API Gateway, 
which is exposed on port 8080. You can access the gateway at:

``http://localhost:8080``

# Services Overview
* Discovery Server: Centralized service registry for managing service instances.
* API Gateway: Acts as the entry point for routing requests to the correct microservice.
* Event Service: Manages event creation, updates, and availability.
* Booking Service: Handles event bookings by users.
* Payment Service: Processes payments for booked events.
* Notification Service: Sends booking and cancellation notifications to users.

# Notes
The application uses Spring Cloud Gateway to route API requests.
Inter-service communication is managed via service discovery through the Discovery Server.
You can monitor all microservices through the API Gateway on `port 8080`.