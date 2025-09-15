E-Commerce REST API
📌 Introduction

The E-Commerce REST API is a backend application built with Spring Boot that provides endpoints for managing users, products, sellers, purchases, and authentication in an online shopping system. This project demonstrates best practices in API design, layered architecture, DTO mapping, and secure authentication — making it production-ready for real-world e-commerce platforms.

📑 Table of Contents

- Features

- Tech Stack

- Installation

- Configuration

- Usage

- API Endpoints

- Database Schema

- Troubleshooting

- Contributors

🚀 Features

- User authentication & authorization (login, profile management).

- Product management (CRUD operations).

- Seller management.

- Purchase handling with stock updates.

- RESTful API design following clean architecture principles.

- DTOs for request/response handling.

- SQL schema script included for database setup.

🛠 Tech Stack

- Backend: Java, Spring Boot, Spring Data JPA, Hibernate

- Database: MySQL (with schema & data migrations managed using Flyway)

- Build Tool: Maven

- IDE Support: Eclipse

⚙️ Installation

  1. Clone the repository
     ```
     git clone https://github.com/your-username/E_Commerce_Rest.git
     cd E_Commerce_Rest
     ```
 
  2. Set up MySQL Database
     
     ```
     CREATE DATABASE store_api;
     ```

  3. Configure application properties
     Update src/main/resources/application.properties with your MySQL credentials:
     ```
     spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     ```
  4. Build & Run
     ```
     ./mvnw spring-boot:run
     ```
📖 Usage
- API will run on: http://localhost:8080
- Use Postman or cURL to test endpoints.

🔗 API Endpoints (Sample)

👤 User Endpoints
- POST /api/auth/register/user → Register new user
- POST /api/auth/login → User login
- POST /api/users/{userId}/orders → Place order

🛒 Seller Endpoints
- POST /api/sellers/{sellerId}/products → Add new product
- PUT /api/sellers/{sellerId}/products/{productId}/stock → Update Stock 
- DELETE /api/sellers/{sellerId}/products/{productId} → Remove product

🗄 Database Schema
  This project uses Flyway for database versioning and migrations.
- The script.sql file provides the initial schema for setting up the database.
- Additional changes and enhancements are managed through Flyway migration scripts (V1__init.sql, V2__add_profiles.sql, V3__update_products.sql, V4__add_sellers.sql, V5__purchase_flow.sql, etc.).
  
  Core Database Tables
  - Users – Stores customer login and profile information.
  - Profiles – Extended user details and addresses.
  - Products – Catalog of available items.
  - Sellers – Vendor details for product listings.
  - Purchases – Tracks orders and transactions.

🛠 Troubleshooting

- Ensure MySQL service is running.

- Check application.properties for correct DB credentials.

- If port 8080 is in use, change it in application.properties.

👨‍💻 Contributors

Developed by [SAMIRAN SAHA] – Backend Java Developer.
  



