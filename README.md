# Parking Lot Reservation System

A full-stack parking lot management system featuring a Java & Spring Boot backend with a React & Tailwind CSS frontend.

![Spring](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?style=for-the-badge&logo=tailwindcss)

---

## Table of Contents
* [About the Project](#about-the-project)
* [Core Features](#core-features)
* [Tech Stack](#tech-stack)
* [Getting Started](#getting-started)
* [Testing](#testing)
* [Usage](#usage)
* [API Endpoint Overview](#api-endpoint-overview)

---

## About the Project

This project implements a complete parking lot reservation system. The backend provides a REST API for managing floors, slots, and reservations, while preventing booking conflicts. It enforces key business rules like time validation, booking duration limits, and dynamic fee calculation based on vehicle type. The frontend is a modern, interactive single-page application with separate views for users and administrators.

---

## Core Features

### Backend (REST API)
* Admin endpoints to create floors and slots.
* Reservation engine with double-booking prevention.
* Dynamic pricing based on vehicle type and duration.
* Availability check for any given time range.
* Endpoints to fetch and cancel specific reservations.
* Includes bonus features: optimistic locking, pagination, and Swagger API documentation.

### Frontend (React UI)
* **User Dashboard**:
    * Search for available slots by time and vehicle type.
    * View estimated cost before booking.
    * View and cancel active bookings.
    * Interactive UI with animations and toast notifications.
* **Admin Dashboard**:
    * Real-time dashboard of all floors and slot statuses.
    * Forms to add/delete floors and slots.
    * Ability to set a slot's status to `MAINTENANCE`.

---

## Tech Stack

### Backend
* **Framework**: Java 17 & Spring Boot 3+
* **Database**: MySQL
* **Data Access**: Spring Data JPA (Hibernate)
* **Testing**: JUnit 5 & Mockito
* **Validation**: Jakarta Bean Validation
* **Error Handling**: Centralized with `@ControllerAdvice`

### Frontend
* **Library**: React (with Vite)
* **Styling**: Tailwind CSS
* **Animations**: Framer Motion
* **Notifications**: React Toastify

---

## Getting Started

### Prerequisites
* Java JDK 17 or later
* Apache Maven
* Node.js & npm
* MySQL Server

### Backend Setup
1.  Clone the repository.

2.  Configure the database in `src/main/resources/application.properties` with your local MySQL credentials.

3.  Run the application from your IDE or via the command line with `mvn spring-boot:run`. The backend will start on `http://localhost:8080`.

### Frontend Setup
1.  Navigate to the `parking-lot-frontend` directory.

2.  Install dependencies: `npm install`.

3.  Run the development server: `npm run dev`. The frontend will start on `http://localhost:5173`.

---

## Testing

The backend includes a suite of unit tests for the service layer to ensure the core business logic is correct and robust. These tests were written using **JUnit 5** and **Mockito** to isolate service logic from the database layer, satisfying the requirement for high code coverage.

### How to Run the Tests
You can run the tests in two ways:

1.  **Using your IDE**:
    * Navigate to the `src/test/java` directory.
    * Right-click on the `com.vikranth.parkinglotsystem` package and select "Run tests".

2.  **Using the Command Line**:
    * Open a terminal in the root directory of the backend project.
    * Run the following Maven command:
        ```bash
        mvn test
        ```
Maven will execute all unit tests and generate a report.

---

## Usage

### API Documentation (Swagger)
The project includes live, interactive API documentation.
* Ensure the backend is running.
* Navigate to: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Frontend Access
* **User View**: **[http://localhost:5173/](http://localhost:5173/)**
* **Admin View**: **[http://localhost:5173/admin](http://localhost:5173/admin)**

---

## API Endpoint Overview

| Method | URL Path | Description |
| :--- | :--- | :--- |
| `POST` | `/api/admin/floors` | Creates a new parking floor. |
| `POST` | `/api/admin/slots` | Creates a new parking slot on a floor. |
| `DELETE` | `/api/admin/slots/{id}` | Deletes a parking slot. |
| `PUT` | `/api/admin/slots/{id}/status` | Updates a slot's status. |
| `GET` | `/api/admin/overview` | Gets a full overview of all floors/slots. |
| `POST` | `/api/reserve` | Creates a new reservation for a slot. |
| `GET` | `/api/availability` | Lists available slots for a given time. |
| `GET` | `/api/reservations/active` | Lists all current and future reservations. |
| `GET` | `/api/reservations/{id}` | Fetches details of a specific reservation. |
| `DELETE` | `/api/reservations/{id}` | Cancels a specific reservation. |
