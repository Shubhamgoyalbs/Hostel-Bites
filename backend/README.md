# Hostel-Bites Backend API Documentation

## 🍕 Overview

Hostel-Bites is a comprehensive Spring Boot-based REST API that serves as the backend for a hostel food ordering system. It enables students to order delicious snacks, meals, and food items from different sellers within their hostel premises, making campus dining convenient and accessible.

## 📋 Table of Contents

1. [Technology Stack](#technology-stack)
2. [Architecture](#architecture)
3. [Database Design](#database-design)
4. [API Documentation](#api-documentation)
5. [Security](#security)
6. [Setup & Installation](#setup--installation)
7. [Configuration](#configuration)
8. [Testing](#testing)
9. [Development Guidelines](#development-guidelines)
10. [Contributing](#contributing)

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Language**: Java 21 with Preview Features
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA with Hibernate
- **Security**: Spring Security with JWT Authentication
- **Build Tool**: Maven
- **Libraries**: 
  - Lombok (Code generation and boilerplate reduction)
  - JWT (JSON Web Tokens for authentication)
  - Jakarta Validation API
  - Spring Boot DevTools

## 🏗 Architecture

The application follows a clean layered architecture pattern with clear separation of concerns:

```
src/main/java/com/shubham/backend/
├── controller/          # REST Controllers (API endpoints)
│   ├── AuthController   # Authentication & authorization endpoints
│   ├── admin/          # Admin-specific controllers
│   │   └── ProductsAdminController
│   ├── seller/         # Seller-specific controllers  
│   │   ├── OrderSellerController
│   │   └── ProductsSellerController
│   └── user/           # User-specific controllers
│       ├── OrderController
│       ├── ProductsUserController
│       ├── ProductsFromSellerController
│       └── SellersUserController
├── service/            # Business logic layer
│   ├── AuthService
│   ├── JwtService
│   ├── admin/         
│   ├── seller/        
│   └── user/          
├── repository/         # Data access layer (JPA repositories)
├── entity/            # JPA entities (Database models)
├── body/              # Request/Response DTOs
├── config/            # Configuration classes
│   ├── SecurityConfig
│   └── JwtAuthenticationFilter
└── BackendApplication  # Main Spring Boot application class
```

## 🗄 Database Design

### Core Entities

#### User Entity
- **Primary Key**: `user_id` (Long)
- **Fields**: 
  - `username` (String, 50 chars, unique)
  - `email` (String, 100 chars, unique)
  - `password` (String, encrypted)
  - `phone_no` (String, 20 chars)
  - `hostel_name` (String, 100 chars)
  - `room_number` (String, 10 chars)
  - `role` (String, default: 'USER')
  - `location` (String, 100 chars)
  - `profile_image` (Text)
  - `created_at` (Timestamp)
- **Roles**: USER, SELLER, ADMIN
- **Relationships**: One-to-Many with UserProduct

#### Product Entity
- **Primary Key**: `product_id` (Long)
- **Fields**: 
  - `name` (String, 100 chars)
  - `description` (Text)
  - `price` (BigDecimal, precision 10, scale 2)
  - `image_url` (Text)
- **Relationships**: One-to-Many with UserProduct, Order

#### Order System
- **OrderUs**: Main order entity with customer and status information
- **Order**: Order-Product relationship with quantities and pricing
- **UserProduct**: Junction table managing User-Product relationships with inventory

### Database Schema Relationships
```sql
-- Core relationships
users (1) ←→ (∞) user_products ←→ (∞) products
orders_us (1) ←→ (∞) orders ←→ (1) products
users (1) ←→ (∞) orders_us
```

## 🔌 API Documentation

### Authentication Endpoints

#### POST `/auth/register`
Register a new user account.

**Request Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com", 
  "password": "password123",
  "phoneNo": "1234567890",
  "hostelName": "Hostel A",
  "roomNo": "101",
  "role": "USER"
}
```

**Response (201):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "hostelName": "Hostel A", 
  "roomNumber": "101",
  "role": "USER",
  "message": "Registration successful"
}
```

#### POST `/auth/login`
Authenticate user and receive JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "hostelName": "Hostel A",
  "roomNumber": "101", 
  "role": "USER",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

#### POST `/auth/validate-token`
Validate if JWT token is still valid.

**Request Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200):**
```json
{
  "valid": true,
  "username": "john_doe",
  "message": "Token is valid"
}
```

#### POST `/auth/refresh-token`  
Refresh an existing JWT token.

**Request Body:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...[new-token]",
  "username": "john_doe",
  "message": "Token refreshed successfully"
}
```

### User Endpoints

#### GET `/api/user/products/all`
Get all available products.

**Response (200):**
```json
[
  {
    "productId": 1,
    "name": "Sandwich",
    "description": "Delicious club sandwich",
    "price": 50.00,
    "imageUrl": "https://example.com/sandwich.jpg"
  },
  {
    "productId": 2,
    "name": "Cold Coffee",
    "description": "Refreshing cold coffee",
    "price": 30.00,
    "imageUrl": "https://example.com/coffee.jpg"
  }
]
```

#### GET `/api/user/products/search/{name}`
Search products by name.

**Parameters:**
- `name`: Product name to search for

**Response (200):**
```json
[
  {
    "productId": 1,
    "name": "Sandwich",
    "description": "Delicious club sandwich",
    "price": 50.00,
    "imageUrl": "https://example.com/sandwich.jpg"
  }
]
```

### Seller Endpoints

#### GET `/api/seller/products/listedProducts/{sellerId}`
Get all products listed by a specific seller.

**Parameters:**
- `sellerId`: ID of the seller

**Response (200):**
```json
[
  {
    "productId": 1,
    "name": "Sandwich",
    "description": "Delicious club sandwich",
    "price": 50.00,
    "imageUrl": "https://example.com/sandwich.jpg",
    "quantity": 10
  }
]
```

#### GET `/api/seller/products/nonListedProducts/{sellerId}`
Get all products not yet listed by a seller.

**Parameters:**
- `sellerId`: ID of the seller

**Response (200):**
```json
[
  {
    "productId": 2,
    "name": "Cold Coffee",
    "description": "Refreshing cold coffee",
    "price": 30.00,
    "imageUrl": "https://example.com/coffee.jpg"
  }
]
```

#### POST `/api/seller/products/addProducts/{sellerId}`
Add products to seller's inventory.

**Parameters:**
- `sellerId`: ID of the seller

**Request Body:**
```json
[1, 2, 3, 4]  // Array of product IDs
```

**Response (200):**
```
Products added successfully
```

#### DELETE `/api/seller/products/deleteProduct/{sellerId}/{productId}`
Remove a product from seller's listing.

**Parameters:**
- `sellerId`: ID of the seller
- `productId`: ID of the product to remove

**Response (200):**
```
Product removed successfully
```

#### PUT `/api/seller/products/updateProduct/{sellerId}/{productId}/{updatedQuantity}`
Update product quantity for a seller.

**Parameters:**
- `sellerId`: ID of the seller
- `productId`: ID of the product to update
- `updatedQuantity`: New quantity value

**Response (200):**
```
Product quantity updated successfully
```

### Order Management

#### Order Controller (`/api/user/orders`)
- Place new orders
- View order history
- Track order status

#### Seller Order Controller (`/api/seller/orders`)  
- View received orders
- Update order status
- Manage order fulfillment

## 🔐 Security

### JWT Authentication
- **Algorithm**: HS256
- **Expiration**: 24 hours (86400000 ms)
- **Header**: Authorization
- **Prefix**: Bearer

### Security Configuration
- **CORS**: Enabled for cross-origin requests
- **CSRF**: Disabled for REST API
- **Session Management**: Stateless
- **Password Encoding**: BCrypt

### Protected Endpoints
All endpoints except `/auth/**` require valid JWT authentication.

### Role-Based Access Control
- **USER**: Can browse products, place orders
- **SELLER**: Can manage product listings, fulfill orders  
- **ADMIN**: Can manage all products and users

## 🚀 Setup & Installation

### Prerequisites
- Java 21 or higher
- Maven 3.6+  
- PostgreSQL 12+
- Git

### Installation Steps

1. **Clone the repository:**
```bash
git clone https://github.com/yourusername/hostel-bites.git
cd hostel-bites/backend
```

2. **Setup PostgreSQL Database:**
```sql
CREATE DATABASE "hostel-bites";
```

3. **Configure database connection:**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hostel-bites
spring.datasource.username=your_username  
spring.datasource.password=your_password
```

4. **Build the project:**
```bash
mvn clean compile
```

5. **Run the application:**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## ⚙️ Configuration

### Application Properties
Located at `src/main/resources/application.properties`:

```properties
# Application
spring.application.name=backend

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/hostel-bites
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA Configuration  
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000  # 24 hours
jwt.token.prefix=Bearer 
jwt.header=Authorization
```

### Environment Variables
For production deployment, use environment variables:
- `DB_URL`: Database connection URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password  
- `JWT_SECRET`: JWT secret key

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductsUserControllerTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Structure
- **Unit Tests**: Service layer testing
- **Integration Tests**: Controller testing with MockMvc
- **Repository Tests**: Data layer testing

## 📝 Development Guidelines

### Code Style
- Use Lombok annotations to reduce boilerplate
- Follow Spring Boot conventions
- Use meaningful variable and method names
- Add proper validation annotations

### Error Handling
- Use appropriate HTTP status codes
- Return consistent error response format
- Log errors for debugging

### API Design
- Follow RESTful principles
- Use appropriate HTTP methods
- Version your APIs when needed
- Document all endpoints

## 📈 Future Enhancements

- [ ] Add API rate limiting
- [ ] Implement caching with Redis
- [ ] Add file upload functionality
- [ ] Implement real-time notifications
- [ ] Add comprehensive logging
- [ ] API versioning strategy
- [ ] Database connection pooling
- [ ] Metrics and monitoring

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

---

**Last Updated:** August 2025  
**Version:** 1.0.0  
**Author:** Shubham Goyal
