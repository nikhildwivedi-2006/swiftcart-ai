# 🛒 SwiftCart — AI-Powered E-Commerce Platform

SwiftCart is a full-stack e-commerce web application built using **React, Spring Boot, MySQL, Docker, and AWS**.

The project provides common e-commerce features such as user authentication, product management, cart and order management, online payment, COD, and an AI-powered shopping assistant.

The application is also **containerized and deployed on AWS EC2**, with a **GitHub Actions CI/CD pipeline** for automated deployment.

---

## 🚀 Live Demo

**Live Application:** `http://3.111.15.26/`

> Note: The application is deployed on an AWS EC2 instance for demonstration purposes.

---

## 📌 Project Overview

SwiftCart follows a three-tier architecture:

```text
                    User
                     |
                     v
              React Frontend
                     |
                     v
               Nginx Server
                     |
              /api/ requests
                     |
                     v
            Spring Boot Backend
                     |
              +------+------+
              |             |
              v             v
            MySQL        External APIs
                         |
                    Razorpay / Gemini
```

The frontend communicates with the Spring Boot backend through REST APIs.

The backend handles authentication, business logic, orders, payments, and database operations.

MySQL stores application data such as users, products, carts, orders, and other required information.

---

# ✨ Features

## 👤 User Features

* User registration and login
* JWT-based authentication
* Role-based access control
* Product browsing
* Product search and pagination
* Add products to cart
* Update cart quantity
* Remove products from cart
* Place orders
* View order information
* Online payment using Razorpay
* Cash on Delivery (COD)

## 👨‍💼 Admin Features

* Admin authentication
* Role-based authorization
* Product management
* Product-related administrative operations
* Order management

## 🤖 AI Features

SwiftCart also integrates **Google Gemini through Spring AI**.

The AI functionality can be used to provide an AI-powered shopping experience and assist users with product-related interactions.

## 💳 Payment Features

The application supports:

* Razorpay online payments
* Cash on Delivery
* Payment status handling
* Order/payment status management

---

# 🛠️ Tech Stack

## Frontend

* React
* Vite
* JavaScript
* HTML
* CSS

## Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* Spring AI
* Maven

## Database

* MySQL

## DevOps & Cloud

* Docker
* Docker Compose
* Docker Hub
* GitHub Actions
* AWS EC2
* Nginx
* Linux / Ubuntu

## External Services

* Razorpay
* Google Gemini

## Development Tools

* Git
* GitHub
* Postman
* Swagger / OpenAPI
* IntelliJ IDEA

---

# 🏗️ Application Architecture

SwiftCart is divided into three main application layers.

### 1. Frontend

The React application provides the user interface.

It communicates with the backend using REST APIs.

### 2. Backend

The Spring Boot application handles:

* Authentication
* Authorization
* Product operations
* Cart operations
* Order processing
* Payment processing
* Database communication
* AI integration
* Exception handling
* Validation

### 3. Database

MySQL stores persistent application data.

The database runs inside a Docker container in the deployed environment.

---

# 🔐 Authentication & Security

SwiftCart uses **Spring Security and JWT** for authentication.

The basic authentication flow is:

```text
User Login
    |
    v
Spring Boot Backend
    |
    v
Validate Credentials
    |
    v
Generate JWT Token
    |
    v
Client Stores Token
    |
    v
Token sent with API requests
    |
    v
Backend validates JWT
    |
    v
Access protected resources
```

The application also uses role-based authorization.

Example roles:

```text
USER
ADMIN
```

Protected APIs require a valid Bearer token.

---

# 💳 Payment Flow

SwiftCart supports both online payment and Cash on Delivery.

### Razorpay

```text
User places order
       |
       v
Select Razorpay
       |
       v
Backend creates/handles payment
       |
       v
Razorpay payment
       |
       v
Payment status updated
       |
       v
Order processed
```

### Cash on Delivery

```text
User places order
       |
       v
Select COD
       |
       v
Order created
       |
       v
Payment method = COD
       |
       v
Order processed
```

---

# 🐳 Docker Architecture

SwiftCart is containerized using Docker.

The application uses separate containers for the major services.

```text
+-------------------------+
|     Frontend Container  |
|       React + Nginx     |
+------------+------------+
             |
             |
+------------v------------+
|      Backend Container  |
|      Spring Boot        |
+------------+------------+
             |
             |
+------------v------------+
|       MySQL Container   |
|         MySQL DB        |
+-------------------------+
```

All required containers communicate through a Docker network.

Example services:

```text
frontend
backend
mysql
```

Docker Compose is used to manage these services together.

---

# 🌐 Nginx Reverse Proxy

Nginx is used as a reverse proxy in the deployment.

The basic request flow is:

```text
Browser
   |
   v
AWS EC2 :80
   |
   v
Nginx
   |
   +------ / ------> Frontend
   |
   +------ /api/ --> Spring Boot Backend
```

This allows the frontend and backend to work through the same public server.

For example:

```text
http://server-ip/
```

serves the frontend.

API requests such as:

```text
/api/products
```

are forwarded by Nginx to the Spring Boot backend.

---

# ☁️ AWS Deployment

SwiftCart is deployed on an **AWS EC2 Ubuntu server**.

The deployment environment contains:

* Ubuntu
* Docker
* Docker Compose
* Nginx
* Docker network
* Persistent MySQL volume

High-level deployment architecture:

```text
                    Internet
                       |
                       v
                AWS EC2 Server
                       |
                       v
                    Nginx
                  /       \
                 /         \
                v           v
          Frontend       Backend
                            |
                            v
                          MySQL
```

---

# 💾 Persistent MySQL Storage

Database persistence is important because containers can be recreated or replaced during deployment.

SwiftCart uses a **Docker persistent volume** for MySQL data.

```text
MySQL Container
       |
       v
Persistent Docker Volume
       |
       v
Database data remains safe
```

This means restarting or recreating the MySQL container does not automatically remove the stored database data.

The deployed environment uses an external/persistent volume for MySQL.

---

# 🔄 CI/CD Pipeline

SwiftCart uses **GitHub Actions** for CI/CD.

The goal of the pipeline is to automate the process from source code changes to deployment.

### Pipeline Flow

```text
Developer
    |
    v
Git Push
    |
    v
GitHub Repository
    |
    v
GitHub Actions
    |
    +------------------+
    |                  |
    v                  v
Build Backend      Build Frontend
    |                  |
    +--------+---------+
             |
             v
       Docker Images
             |
             v
         Docker Hub
             |
             v
        AWS EC2
             |
             v
       Pull New Images
             |
             v
      Restart Services
             |
             v
        Live Website
```

### CI/CD Process

1. Developer pushes code to GitHub.
2. GitHub Actions workflow starts.
3. Backend application is built.
4. Frontend application is built.
5. Docker images are created.
6. Images are pushed to Docker Hub.
7. AWS EC2 pulls the latest images.
8. Docker Compose updates the running services.
9. Nginx continues serving the application.

This reduces the need for manual deployment after every code change.

---

# ⚙️ GitHub Actions

The CI/CD workflow is defined using a YAML file inside:

```text
.github/workflows/
```

A workflow generally contains:

```yaml
name:
on:
jobs:
  steps:
```

The workflow defines when the pipeline should run and what tasks should be executed.

Typical pipeline tasks include:

```text
Checkout Code
     ↓
Build Application
     ↓
Build Docker Image
     ↓
Login to Docker Hub
     ↓
Push Image
     ↓
Deploy to AWS EC2
```

Sensitive values such as Docker Hub credentials and deployment secrets are stored using **GitHub Secrets** instead of writing them directly in the workflow file.

---

# 📦 Docker Hub

Docker Hub is used as a container image registry.

The basic flow is:

```text
Source Code
     |
     v
GitHub Actions
     |
     v
Docker Image
     |
     v
Docker Hub
     |
     v
AWS EC2
     |
     v
Running Container
```

Docker images contain the packaged application and its required runtime environment.

Docker Hub stores these images so that the EC2 server can pull them during deployment.

---

# 🔌 REST APIs

The backend exposes REST APIs for different application features.

Examples include APIs related to:

```text
Authentication
Products
Cart
Orders
Payments
Users
AI features
```

The APIs are protected according to the required authentication and authorization rules.

---

# 📚 API Documentation

Swagger / OpenAPI is integrated into the Spring Boot backend.

It provides an interactive interface for exploring and testing APIs.

The Swagger documentation can be used to:

* View available APIs
* Understand request parameters
* Check response structures
* Test endpoints
* Authorize using Bearer JWT tokens

---

# 🗂️ Project Structure

A simplified project structure looks like:

```text
swiftcart-ai/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── Dockerfile
│
├── docker-compose.yml
│
├── nginx/
│   └── nginx.conf
│
└── .github/
    └── workflows/
        └── *.yml
```

> The exact folder structure can change as the project evolves.

---

# 🖥️ Run the Project Locally

## Prerequisites

Make sure the following are installed:

* Java
* Maven
* Node.js
* npm
* MySQL
* Docker
* Docker Compose
* Git

---

## 1. Clone the Repository

```bash
git clone https://github.com/nikhildwivedi-2006/swiftcart-ai.git
```

```bash
cd swiftcart-ai
```

---

## 2. Run Using Docker Compose

Build and start the services:

```bash
docker compose up --build
```

To run in detached mode:

```bash
docker compose up -d --build
```

Check running containers:

```bash
docker ps
```

Stop the services:

```bash
docker compose down
```

---

# 🔧 Environment Variables

The project uses environment variables for sensitive configuration.

Examples include:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
JWT_SECRET
RAZORPAY_KEY
RAZORPAY_SECRET
GEMINI_API_KEY
```

These values should **not be committed to GitHub**.

For local development, configure them using your local environment or an appropriate `.env` / configuration mechanism.

For CI/CD, sensitive values should be stored using **GitHub Secrets**.

---

# 🧪 Testing

During development, APIs can be tested using:

* Postman
* Swagger / OpenAPI
* Browser
* Frontend application

Testing includes areas such as:

```text
Authentication
Product APIs
Cart APIs
Order APIs
Payment APIs
Authorization
```

---

# 🔍 DevOps Implementation

One of the main goals of SwiftCart was to understand how a Java application can be moved from a local development environment to a cloud environment.

The project includes practical DevOps concepts such as:

* Linux server management
* Git and GitHub
* Docker
* Docker Compose
* Docker networking
* Docker volumes
* Docker Hub
* GitHub Actions
* CI/CD
* AWS EC2
* Security Groups
* Nginx reverse proxy
* Persistent database storage
* Container-based deployment
* Basic cloud troubleshooting

---

# 🚨 Deployment & Troubleshooting

Some common checks while managing the deployment include:

### Check running containers

```bash
docker ps
```

### Check container logs

```bash
docker logs <container-name>
```

### Check Docker Compose services

```bash
docker compose ps
```

### Check service logs

```bash
docker compose logs
```

### Check Nginx

```bash
sudo systemctl status nginx
```

### Check listening ports

```bash
sudo ss -tulpn
```

These commands help identify issues related to containers, networking, ports, Nginx, and application services.

---

# 🔐 Security Considerations

The project follows basic security practices:

* JWT-based authentication
* Role-based authorization
* Password protection
* Environment-based secrets
* GitHub Secrets for CI/CD credentials
* AWS Security Groups for network access
* Sensitive API keys are not committed to the repository

> Production applications should use additional security controls such as HTTPS, secret managers, monitoring, stricter network rules, and proper database security.

---

# 📈 Future Improvements

Some possible future improvements are:

* HTTPS using SSL/TLS
* AWS RDS for managed MySQL
* AWS ECR for container images
* Infrastructure as Code using Terraform
* Monitoring using CloudWatch
* Application monitoring using Prometheus and Grafana
* Automated testing in CI/CD
* Better logging and centralized monitoring
* Improved security and secret management
* Auto Scaling and Load Balancing

---

# 🎯 What I Learned

While building SwiftCart, I gained practical experience in:

### Backend Development

* Building REST APIs using Spring Boot
* Spring Security
* JWT authentication
* Role-based authorization
* JPA and MySQL
* Exception handling
* Validation
* Payment integration
* AI integration

### DevOps & Cloud

* Creating Docker images
* Running multi-container applications
* Docker networking
* Persistent Docker volumes
* Docker Hub
* GitHub Actions
* CI/CD pipelines
* AWS EC2 deployment
* Linux server management
* Nginx reverse proxy
* Cloud deployment troubleshooting

---

# 👨‍💻 Author

**Nikhil Dwivedi**

B.Tech Computer Science & Engineering

### Profiles

* GitHub: `nikhildwivedi-2006`
* LinkedIn: `dwivedinikhil075`

---

# ⭐ Project Highlights

SwiftCart demonstrates a complete development-to-deployment workflow:

```text
Java + Spring Boot
        ↓
REST APIs
        ↓
React Frontend
        ↓
MySQL Database
        ↓
Docker
        ↓
Docker Hub
        ↓
GitHub Actions CI/CD
        ↓
AWS EC2
        ↓
Nginx
        ↓
Live Application
```

The project combines **full-stack development, cloud deployment, containerization, and CI/CD automation** in one practical application.
