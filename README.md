# Shortify – URL Shortener & Analytics Service

Shortify is a full-stack, production-quality URL Shortener & Analytics Service built with **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA**, **Thymeleaf**, and **Bootstrap 5**. It allows users to register, create customized short URLs, manage their links, and track real-time click analytics with interactive visual graphs.

---

## 🚀 Features

- **User Authentication:** Secure registration and login using Spring Security and BCrypt password hashing. Users only see and manage their own URLs.
- **URL Shortening:** Generate random 6-character short codes (Base62) or define your own custom aliases.
- **Instant Redirection:** High-performance redirection tracking the original destination URL.
- **Link Management:** Dashboard to view, copy, toggle active/inactive status, and delete shortened links.
- **Click Analytics:** Visual click representation powered by Chart.js, including detailed breakdown logs (timestamps, referrers, user agents).
- **RESTful APIs:** Complete API endpoints to integrate URL shortening capabilities with external clients.
- **H2 In-Memory DB & Console:** Runs out of the box with zero external database installation required, featuring an interactive web console for database inspection.
- **Flexible Database Support:** Seamlessly switchable between embedded H2, MySQL, or PostgreSQL (e.g. Supabase).
- **Modern UI:** Glassmorphism aesthetics, responsive Bootstrap 5 layout, Lucide/Bootstrap icons, and micro-animations.

---

## 🛠️ Tech Stack

- **Backend:** Java 17+, Spring Boot 3.2.x
- **Security:** Spring Security 6 (BCrypt, Session Management)
- **Data Access:** Spring Data JPA, Hibernate ORM
- **Database:** H2 Database (in-memory, default) / MySQL / PostgreSQL (Supabase)
- **Frontend:** Thymeleaf, Bootstrap 5, Chart.js, Axios
- **Build Tool:** Maven (Maven Wrapper included)

---

## 📐 Architecture & Project Structure

The project follows a standard Spring Boot layered architecture:

```
src/main/java/com/shortify/
├── config/             # Spring Security and application configuration
├── controller/         # Web views (Thymeleaf), REST API & Redirect controllers
├── dto/                # Request/response data transfer objects with validation
├── entity/             # JPA entity models (User, ShortUrl, ClickEvent)
├── exception/          # Global exception handler for standardized error responses
├── repository/         # Spring Data JPA repositories
├── security/           # Custom UserDetailsService and authentication provider
└── service/            # Business logic (Base62 shortener, click tracking, analytics)
```

---

## 🗄️ Database Schema Overview

- **User**: `id`, `name`, `email` (unique), `password`, `createdAt`
- **ShortUrl**: `id`, `originalUrl`, `shortCode` (unique, indexed), `active`, `expiresAt`, `clickCount`, `createdAt`, `user_id` (Foreign Key)
- **ClickEvent**: `id`, `clickedAt`, `referrer`, `userAgent`, `short_url_id` (Foreign Key)

---

## 🔌 API Endpoints

| HTTP Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/urls` | Create a new short URL | Yes |
| `GET` | `/api/urls` | Get all URLs for authenticated user | Yes |
| `GET` | `/api/urls/{id}` | Get details for a specific URL | Yes |
| `PUT` | `/api/urls/{id}/toggle` | Toggle active status of a URL | Yes |
| `DELETE` | `/api/urls/{id}` | Delete a URL | Yes |
| `GET` | `/api/urls/{id}/analytics` | Get click analytics history | Yes |
| `GET` | `/{shortCode}` | Public redirect to original URL | No |

---

## ⚡ Getting Started

### Prerequisites

- **Java 17** or higher installed ([OpenJDK 17+](https://learn.microsoft.com/en-us/java/openjdk/download) recommended)
- Git

> [!NOTE]
> Maven is not required to be pre-installed! The project includes the Maven Wrapper (`mvnw` / `mvnw.cmd`).

---

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/omi290/URL-Shortener-Service.git
   cd URL-Shortener-Service
   ```

2. **Run using Maven Wrapper:**
   - On **Windows (PowerShell/CMD)**:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   - On **Linux / macOS**:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Access the application:**
   - **Web App:** [http://localhost:8081](http://localhost:8081)
   - **H2 Web Console:** [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
     - **JDBC URL:** `jdbc:h2:mem:shortify`
     - **Username:** `sa`
     - **Password:** *(leave blank)*

---

### Switching Databases (Optional)

By default, the application uses an in-memory **H2 database** with zero configuration required.

#### Using MySQL:
1. In `src/main/resources/application.properties`, configure:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/shortify?createDatabaseIfNotExist=true
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

#### Using Supabase / PostgreSQL:
1. Add the PostgreSQL driver in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.postgresql</groupId>
       <artifactId>postgresql</artifactId>
       <scope>runtime</scope>
   </dependency>
   ```
2. In `src/main/resources/application.properties`, configure your Supabase connection string:
   ```properties
   spring.datasource.url=jdbc:postgresql://<your-supabase-host>:5432/postgres?sslmode=require
   spring.datasource.username=postgres
   spring.datasource.password=<your-supabase-password>
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

---

## 🔮 Future Enhancements

- [ ] Redis caching for ultra-low latency redirection
- [ ] IP-based geolocation and device analytics for click events
- [ ] Rate-limiting on public redirect and API endpoints (Bucket4j)
- [ ] QR code generation for shortened links
- [ ] Expiration date scheduler / background cleaner

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
