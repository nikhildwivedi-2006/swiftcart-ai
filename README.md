# 🛒 SwiftCart AI - Full Stack E-Commerce Platform

SwiftCart AI is a production-oriented full-stack e-commerce application built using **Spring Boot and React**.  
The project follows industry-standard backend architecture with layered design, DTO pattern, service abstraction, reusable helper methods, entity mapping, and secure API development.

The application includes secure authentication, product management, cart & order workflow, payment integration, and AI-powered customer support using Spring AI.

---

## 🚀 Key Features

### 🔐 Authentication & Authorization
- User Registration and Login
- JWT based authentication
- Role-based authorization (USER / ADMIN)
- Secure API endpoints using Spring Security
- Token based request validation

### 👤 User Management
- User profile management
- User details handling through DTOs
- Secure user data operations

### 🛍️ Product Management
- Create, update, delete and fetch products
- Product search functionality
- Category based filtering
- Price range filtering
- Pagination and sorting support
- Stock management

### 🛒 Cart Management
- Add products to cart
- Update cart quantity
- Remove cart items
- Automatic cart calculation

### 📦 Order Management
- Place orders from cart
- Order history
- Order details tracking
- Order status management

### 💳 Payment Integration
- Razorpay payment gateway integration
- Cash on Delivery support
- Payment status handling

### 🤖 AI Customer Support
- Integrated Spring AI
- Gemini API powered chatbot
- AI based customer query handling

---

### Design Practices Used

✅ DTO Pattern  
- Separate request and response objects
- Prevents direct entity exposure

✅ Mapper Approach  
- Entity to DTO conversion
- DTO to Entity conversion

✅ Service Layer Abstraction  
- Defined service contracts using interfaces
- Implemented business logic separately in service implementation classes
- Keeps controllers clean and improves maintainability, scalability, and testability

✅ Helper Methods  
- Reusable utility methods
- Cleaner and maintainable code

✅ Exception Handling  
- Custom runtime exceptions
- Centralized error handling

✅ Repository Pattern  
- Database operations using Spring Data JPA

---

# 🛠️ Tech Stack

## Backend
- Java 17+
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Spring AI
- Maven

## Frontend
- React.js
- Vite
- React Router
- Axios
- CSS

## Tools & Others
- Git & GitHub
- Docker
- Postman
- AWS (Deployment in progress)

---

# 📂 Project Structure
```
src/main/java/in/swiftcart

├── controller
├── service (interface)
├── serviceImpl (implementation)
├── repository
├── entity
├── dtorequest
├── dtoresponse
├── exception
├── security
├── config
```
