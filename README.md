# SmartStock - A Warehouse Management System

## Description

A web-based inventory and warehouse management system built using Java and SpringBoot with server-side rendering UI using Thymeleaf. The project is built to manage warehouse, stock, and operations efficiently and also provides a dynamic UI for user experience. The endpoints are secure with Role-Based Access implemented using Spring Security.

## Tech Stack

* Backend: Java, Spring Boot, Spring Security
* Frontend: HTML, CSS, Thymeleaf
* Database: MySQL, Spring Data JPA
* Tools: Postman, Maven, Intellij
  
## Features

* User registration and login
* Dynamic web pages using Thymeleaf
* Backend REST and secure endpoints
* Admin can see all the owners and their warehouses
* Owner can manages their warehouses 
* Staff can manage the products of their assigned warehouse

## UI Pages (Thymeleaf Views)

* /login → Login page
* /register → Registration page
* /dashboard → User dashboard
* /admin → Admin Dashboard

## API Endpoints

### Auth

* POST /register
* POST login

### Other Functionalities

* GET /warehouse/warehouse_id
* GET /products/product_id
* POST /warehouse/add
* POST /product/add

## Sample Request & Response

### POST /login

Request:
{
"email": "[test@gmail.com](mailto:test@gmail.com)",
"password": "123456"
}

Response:
{
"token": "jwt_token"
}

## Setup Instructions

1. Clone the repository
2. Open in IDE (IntelliJ / Eclipse)
3. Configure database in application.properties
4. Run the Spring Boot application
5. Open browser at http://localhost:8080

##  Project Structure
* securityconfig/ → Handles Spring Security Configuration
* controller/ → Handles HTTP requests
* service/ → Business logic
* repository/ → Database access
* model/ → Entities
* dto/ → Data Transfer Objects for Models
* templates/ → Thymeleaf HTML files
* static/ → Images

## Future Improvements

* Improve UI design
* Add more validations
* Implement caching
* Enhance security

##  Author

Alok Vishwakarma 
GitHub: github.com/ialokvishwakarma
