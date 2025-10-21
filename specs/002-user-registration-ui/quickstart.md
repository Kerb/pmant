# Quickstart Guide: User Registration Feature

This guide provides instructions on how to run the application with the user registration feature and how to test it.

## 1. Prerequisites

Before you begin, ensure you have the following installed:

- Docker and Docker Compose
- Node.js and npm (for frontend development/testing)
- Java 17 and Maven (for backend development/testing)

## 2. Running the Application

1.  **Build the Backend:**
    Navigate to the `backend-app` directory and build the project:
    ```bash
    cd backend-app
    mvn clean install
    cd ..
    ```

2.  **Install Frontend Dependencies:**
    Navigate to the `frontend-app` directory and install dependencies:
    ```bash
    cd frontend-app
    npm install
    cd ..
    ```

3.  **Start the Docker Compose Environment:**
    From the project root directory, start the services using Docker Compose:
    ```bash
    docker-compose up --build
    ```
    This will start the PostgreSQL database, the Spring Boot backend, the React frontend, and the Nginx proxy.

4.  **Access the Application:**
    Open your web browser and navigate to `https://localhost/register`.
    (Note: You might encounter a browser warning about an untrusted certificate, as we are using self-signed certificates for local development. You can safely proceed.)

## 3. Testing User Registration

1.  **Access the Registration Page:**
    Go to `https://localhost/register` in your browser.

2.  **Successful Registration:**
    - Enter a unique login (e.g., `testuser`)
    - Enter a password (e.g., `password123` - must be at least 8 characters long)
    - Click the "Register" button.
    - You should see an alert "Registration successful!" and a message in the browser console.

3.  **Registration with Existing Login:**
    - Try to register with the same login (`testuser`) again.
    - You should see an error message on the form indicating that the login already exists.

4.  **Registration with Short Password:**
    - Enter a login (e.g., `shortpass`)
    - Enter a password less than 8 characters (e.g., `short`)
    - Click the "Register" button.
    - You should see an error message on the form indicating that the password must be at least 8 characters long.

5.  **Registration with Empty Fields:**
    - Try to register with empty login or password fields.
    - You should see error messages on the form indicating that the fields cannot be empty.

## 4. Stopping the Application

To stop the Docker Compose environment, run the following command in your project root directory:

```bash
docker-compose down
```