# User Management Service with Spring Boot

## Features
- User Management: Create, update, delete, and view users.
- Role-Based Access Control: Admins manage users, users update their own profiles.
- Caching: User data cached for faster retrieval.
- Metrics: Tracks user creation, update, and deletion counts.

## Technologies Used
- Spring Boot
- Spring Security (JWT)
- Micrometer (Metrics)
- JpaRepository (Database)

## Setup
1. Clone repo: `git clone <repository-url>`
2. Run app: `mvn spring-boot:run`
3. Access app: `http://localhost:8080`

## Endpoints
- `POST /users` : Create user
- `GET /users` : Get all users (Admin only)
- `GET /users/{id}` : Get user by ID
- `PUT /users/{id}` : Update user
- `DELETE /users/delete/{id}` : Delete user
- `PUT /users/updatePassword/{username}` : Update password
- `GET /users/getByUserName/{username}` : Get user by username

