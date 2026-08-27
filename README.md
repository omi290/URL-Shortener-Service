# Shortify – URL Shortener & Analytics Service

Shortify is a complete, production-quality full-stack URL Shortener Service built with Spring Boot, Spring Security, MySQL, Thymeleaf, and Bootstrap 5. It allows users to create shortened URLs, manage them in a dashboard, and view detailed click analytics.

## Features

- **User Authentication:** Secure registration and login using Spring Security and BCrypt password hashing. Each user manages their own URLs.
- **URL Shortening:** Generate random 6-character short codes (Base62) or provide custom aliases.
- **Redirection:** Fast and reliable redirection tracking the original URL.
- **URL Management:** Dashboard to view, edit status (enable/disable), delete, and manage all your shortened links.
- **Analytics Dashboard:** Visual representation of click data using Chart.js, including detailed tables for recent clicks with referrer and user-agent information.
- **REST APIs:** Full suite of RESTful APIs to integrate URL shortening capabilities into other clients.
- **Modern UI:** Glassmorphism aesthetics, responsive design with Bootstrap 5, and smooth micro-animations.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3.2.x
- **Data Access:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **Security:** Spring Security 6
- **Frontend:** Thymeleaf, Bootstrap 5, Chart.js, Axios
- **Build Tool:** Maven

## Architecture

The application follows a standard layered architecture:
- **`controller`**: Handles incoming web and API requests. `WebController` serves Thymeleaf views, `ApiController` serves JSON responses, and `RedirectController` handles the core URL resolution.
- **`service`**: Contains business logic (URL shortener algorithm, analytics aggregation).
- **`repository`**: Data access interfaces extending Spring Data JPA.
- **`entity`**: JPA mapped entities (`User`, `ShortUrl`, `ClickEvent`).
- **`dto`**: Data Transfer Objects used to receive and validate incoming requests.
- **`config`**: Security and application configuration.
- **`exception`**: Global exception handler for standardized error responses.

## Database Schema Overview

- **User**: `id`, `name`, `email` (unique), `password`, `createdAt`
- **ShortUrl**: `id`, `originalUrl`, `shortCode` (unique, indexed), `active`, `expiresAt`, `clickCount`, `createdAt`, `user_id` (Foreign Key)
- **ClickEvent**: `id`, `clickedAt`, `referrer`, `userAgent`, `short_url_id` (Foreign Key)

## API Endpoints

| HTTP Method | Endpoint | Description | Auth Required |
| --- | --- | --- | --- |
| `POST` | `/api/urls` | Create a new short URL | Yes |
| `GET` | `/api/urls` | Get all URLs for the authenticated user | Yes |
| `GET` | `/api/urls/{id}` | Get details for a specific URL | Yes |
| `PUT` | `/api/urls/{id}/toggle` | Toggle the active status of a URL | Yes |
| `DELETE` | `/api/urls/{id}` | Delete a URL | Yes |
| `GET` | `/api/urls/{id}/analytics`| Get click history for a URL | Yes |

## Setup and Installation

### Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL Server 8.0+

### Database Setup

1. Create a database named `shortify` in your MySQL instance:
```sql
CREATE DATABASE shortify;
```
2. Update the database credentials in `src/main/resources/application.properties` if they differ from the default (`root` with empty password).

### Running the Application

1. Clone the repository and navigate to the project directory.
2. Build the project using Maven:
```bash
mvn clean install
```
3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```
4. Access the application in your browser at `http://localhost:8080`.

## Screenshots

*(Placeholders for future screenshots)*

- **Landing Page**: Shows the modern login and registration interface.
- **Dashboard**: Displays the summary statistics and the URL management table.
- **Analytics**: Displays the Chart.js graph for click history.

## Future Improvements

- Add support for caching (e.g., Redis) to speed up URL resolution.
- Implement rate-limiting for the public redirect endpoint and API endpoints.
- Add support for QR code generation for shortened URLs.
- Include IP-based geolocation tracking for clicks.
