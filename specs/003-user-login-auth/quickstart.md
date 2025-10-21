# Quickstart: User Login Authentication

This guide provides instructions to quickly set up and test the user login authentication feature.

## Prerequisites

- Docker and Docker Compose installed.
- Project repository cloned locally.

## Steps to Run

1.  **Navigate to the project root directory**:

    ```bash
    cd /path/to/your/pmant
    ```

2.  **Start the application services using Docker Compose**:

    This will build and start the backend, frontend, database, and Nginx proxy.

    ```bash
    docker-compose up --build -d
    ```

3.  **Access the Frontend Application**:

    Once all services are up and running, open your web browser and navigate to:

    ```
    http://localhost
    ```

    You should be redirected to the login page or be able to navigate to it.

## Testing the Login Feature

To test the login feature, you will need a registered user in the database. For initial testing, you might need to manually insert a user into the PostgreSQL database or use a registration endpoint if one is available.

### Example (assuming a user 'testuser' with password 'Password123!' exists):

1.  On the login page, enter `testuser` (or `testuser@example.com` if using email as login) in the login field.
2.  Enter `Password123!` in the password field.
3.  Click the "Login" button.

### Expected Outcomes:

-   **Successful Login**: You should be redirected to the user's homepage.
-   **Failed Login**: If you enter incorrect credentials, you should see the error message "Login or user does not exist" on the login page.
