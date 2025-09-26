# PARKING-LOT-RESERVATION System

A complete, full-stack application designed to manage a multi-floor parking facility. This project provides a robust backend REST API built with **Java & Spring Boot** and a dynamic, interactive frontend built with **React & Tailwind CSS**.

![Spring](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F?style=for-the-badge&logo=spring)
![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)
![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-4-06B6D4?style=for-the-badge&logo=tailwindcss)

---

## Table of Contents
* [About the Project](#about-the-project)
* [Core Features](#core-features)
* [Technical Architecture](#technical-architecture)
* [Getting Started](#getting-started)
* [Usage](#usage)
* [API Endpoint Overview](#api-endpoint-overview)

---

## About the Project

This project is a complete, full-stack application designed to manage a multi-floor parking facility. [cite_start]The backend is a robust REST API built with **Java and Spring Boot** [cite: 4][cite_start], designed to handle the core logic of managing parking infrastructure and processing reservations without conflicts[cite: 5]. The project was extended to include a modern, interactive single-page application (SPA) frontend built with **React and Tailwind CSS**, providing distinct user interfaces for both customers and administrators.

[cite_start]The system enforces key business rules, such as validating reservation times [cite: 30][cite_start], limiting booking duration to 24 hours [cite: 31][cite_start], and ensuring vehicle numbers match a specific format[cite: 32]. [cite_start]It also correctly calculates parking fees based on vehicle type and duration, rounding up partial hours as required[cite: 23, 33].

---

## Core Features

The application's functionality is divided into the backend API and the frontend user interface.

### Backend (REST API)
* [cite_start]**Infrastructure Management**: Provides administrative endpoints to create parking floors (`POST /floors`) [cite: 15] [cite_start]and add parking slots (`POST /slots`) to specific floors[cite: 16].
* [cite_start]**Reservation Engine**: The core `POST /reserve` endpoint handles new bookings [cite: 17][cite_start], performing critical server-side checks to prevent double-booking or conflicts for a given time range[cite: 14, 18].
* **Dynamic Pricing**: Calculates parking fees based on configurable hourly rates for different vehicle types, such as **Rs. 30/hour for 4-wheelers** and **Rs. [cite_start]20/hour for 2-wheelers**[cite: 20]. [cite_start]The system is designed to flexibly support new vehicle types and rate changes[cite: 22].
* [cite_start]**Availability Service**: An endpoint (`GET /availability`) that returns a list of all available parking slots for a user-specified time range[cite: 25].
* [cite_start]**Booking Management**: Endpoints are available to fetch the details of a specific reservation (`GET /reservations/{id}`) [cite: 26] [cite_start]and to cancel a reservation (`DELETE /reservations/{id}`)[cite: 27].
* **Bonus Features Implemented**:
    * [cite_start]**Concurrency Protection**: Optimistic locking is used on reservations to prevent race conditions[cite: 40].
    * [cite_start]**Pagination**: The availability endpoint supports pagination to efficiently handle large numbers of slots[cite: 41].
    * [cite_start]**API Documentation**: Live, interactive API documentation is automatically generated and available via Swagger UI[cite: 42].

### Frontend (React UI)
The frontend provides two distinct experiences based on the URL.

* **User Dashboard (`/`)**:
    * **Dynamic Search**: Users can search for available slots by selecting a start time, end time, and vehicle type.
    * **Live Price Calculation**: Before confirming a booking, the UI displays the estimated cost based on the selected duration and slot type.
    * **Interactive Slot Selection**: Available slots are displayed in a clean, grid-based layout, allowing users to select their desired spot.
    * **Active Bookings**: A dedicated panel shows a list of the user's current and future reservations, with an option to cancel each one.
    * **Animated Feedback**: The interface uses the Framer Motion library for smooth animations and provides clear user feedback via toast notifications.

* **Admin Dashboard (`/admin`)**:
    * **Real-time Overview**: Displays a complete, color-coded overview of all floors and the status of every slot (Available, Reserved, Maintenance).
    * **Infrastructure Management**: Provides intuitive forms to add new floors and slots.
    * **Slot Management**: Allows an administrator to delete slots or toggle a slot's status between `AVAILABLE` and `MAINTENANCE`.

---

## Technical Architecture

### Backend
* [cite_start]**Framework**: Java 17+ and Spring Boot 3+[cite: 35].
* [cite_start]**Database**: MySQL (or any other relational database)[cite: 36].
* **Data Access**: Spring Data JPA (Hibernate).
* **Architecture**: Layered (Controller, Service, Repository, Model).
* [cite_start]**Validation**: Jakarta Bean Validation (`@Valid`) is used at the controller layer[cite: 37].
* [cite_start]**Error Handling**: A centralized `GlobalExceptionHandler` with `@ControllerAdvice` provides consistent JSON error responses[cite: 38].

### Frontend
* **Library**: React (with Vite).
* **Styling**: Tailwind CSS.
* **Animations**: Framer Motion.
* **Notifications**: React Toastify.
* **API Communication**: A centralized API service using the `fetch` API.

---

## Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites
* Java JDK 17 or later
* Apache Maven 3.8+
* Node.js & npm
* MySQL Server

### Backend Setup
1.  **Clone the repository.**
2.  **Configure the database** in `src/main/resources/application.properties` with your local MySQL username and password.
3.  **Run the application** from your IDE or via the command line with `mvn spring-boot:run`. The backend will start on `http://localhost:8080`.

### Frontend Setup
1.  Navigate to the `parking-lot-frontend` directory.
2.  Install dependencies: `npm install`.
3.  Run the development server: `npm run dev`. The frontend will start on `http://localhost:5173`.

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

| Method   | URL Path                         | Description                                |
| :------- | :------------------------------- | :----------------------------------------- |
| `POST`   | `/api/admin/floors`              | Creates a new parking floor.               |
| `POST`   | `/api/admin/slots`               | Creates a new parking slot on a floor.     |
| `DELETE` | `/api/admin/slots/{id}`          | Deletes a parking slot.                    |
| `PUT`    | `/api/admin/slots/{id}/status`   | Updates a slot's status.                   |
| `GET`    | `/api/admin/overview`            | Gets a full overview of all floors/slots.  |
| `POST`   | `/api/reserve`                   | Creates a new reservation for a slot.      |
| `GET`    | `/api/availability`              | Lists available slots for a given time.    |
| `GET`    | `/api/reservations/active`       | Lists all current and future reservations. |
| `GET`    | `/api/reservations/{id}`         | Fetches details of a specific reservation. |
| `DELETE` | `/api/reservations/{id}`         | Cancels a specific reservation.            |
