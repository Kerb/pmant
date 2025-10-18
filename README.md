# Minimal Docker Compose Application

This project demonstrates a minimal application setup using Docker Compose, consisting of:
- A Java 17 Spring Boot backend
- A React frontend
- A PostgreSQL database
- An Nginx proxy

## Setup

1.  **Prerequisites:**
    *   Docker Desktop (or Docker Engine)
    *   Docker Compose

2.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-name>
    ```

3.  **Start the application:**
    ```bash
    docker compose -f docker-compose.yml up --build -d
    ```

## Usage

Once the application is running:

1.  Access the frontend in your browser: `http://localhost/`
2.  Click the "Ping" button to send a request to the backend.
3.  Observe the "Loading..." state and the "pong" response.

## Stopping the application

```bash
docker compose -f docker-compose.yml down
```
