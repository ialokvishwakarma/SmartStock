# SmartStock - A Warehouse Management System

## Description

A web-based inventory and warehouse management system built using Java and SpringBoot with server-side rendering UI using Thymeleaf. It enables efficient management of warehouses, stock, and operations with secure role-based access using Spring Security.

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
* Owner can manage their warehouses 
* Staff can manage the products of their assigned warehouse

## UI Pages (Thymeleaf Views)

* /login → Login page
* /register → Registration page
* /dashboard → User dashboard
* /admin → Admin Dashboard

## API Endpoints

### Auth

* POST /register
* POST /login

### Other Functionalities

* GET /warehouse/{id}
* GET /products/{id}
* POST /warehouse/add
* POST /product/add

## Sample Request & Response

### POST /login

Request:
{
"email": "test@gmail.com",
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
5. Visit: http://localhost:8080

## Project Structure
* securityconfig/ → Spring Security configuration
* controller/ → Handles HTTP requests
* service/ → Business logic
* repository/ → Database access
* model/ → Entity classes
* dto/ → Data Transfer Objects
* templates/ → Thymeleaf ,Html
* static/ →  images, assets

## Future Improvements

* Improve UI design
* Add more validations
* Implement caching (Redis)
* Enhance security features

##  Author

Alok Vishwakarma<br>
GitHub: https://github.com/ialokvishwakarma
