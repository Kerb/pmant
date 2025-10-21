# Implementation Tasks for User Login Authentication

## Feature: User Login Authentication

This document outlines the detailed, dependency-ordered tasks for implementing the User Login Authentication feature. Each task is designed to be actionable and independently verifiable.

## Phase 1: Setup

**Goal**: Ensure the development environment is ready and core infrastructure components are in place.

- [ ] T001 Verify Docker Compose setup for backend, frontend, database, and Nginx.
- [ ] T002 Ensure PostgreSQL database is accessible and running via Docker Compose.
- [ ] T003 Review existing `backend-app` and `frontend-app` project structures for consistency.

## Phase 2: Foundational

**Goal**: Establish core backend components that are prerequisites for all user stories.

- [ ] T004 Implement Liquibase migration for `User` table with `login` and `passwordHash` columns in `backend-app/src/main/resources/db/changelog/db.initial.sql`.
- [ ] T005 Create `User` entity/model in `backend-app/src/main/java/eu/pmant/app/model/User.java`.
- [ ] T006 Create `UserRepository` for `User` entity using jOOQ in `backend-app/src/main/java/eu/pmant/app/repository/UserRepository.java`.
- [ ] T007 Implement `PasswordUtil` for password hashing (e.g., BCrypt) in `backend-app/src/main/java/eu/pmant/app/util/PasswordUtil.java`.

## Phase 3: User Story 1 - Successful Login (P1)

**Goal**: Enable users to successfully log in with correct credentials and be redirected to their homepage.

**Independent Test Criteria**: A user can enter valid credentials on the login page and is successfully redirected to the homepage.

### Implementation Tasks

- [ ] T008 [US1] Implement `UserService` method for user authentication, including case-insensitive login and password verification, in `backend-app/src/main/java/eu/pmant/app/service/UserService.java`.
- [ ] T009 [US1] Create `LoginRequest` DTO with `login` and `password` fields in `backend-app/src/main/java/eu/pmant/app/dto/LoginRequest.java`.
- [ ] T010 [US1] Create `LoginResponse` DTO for successful login (e.g., message, token) in `backend-app/src/main/java/eu/pmant/app/dto/LoginResponse.java`.
- [ ] T011 [US1] Implement `AuthController` with `POST /api/auth/login` endpoint to handle login requests, using `UserService` for authentication, in `backend-app/src/main/java/eu/pmant/app/controller/AuthController.java`.
- [ ] T012 [US1] Configure Spring Security for session management and authentication in `backend-app/src/main/java/eu/pmant/app/config/SecurityConfig.java`.
- [ ] T013 [US1] Create `LoginPage` component in `frontend-app/src/pages/LoginPage.js` with login form (username/email, password fields, submit button).
- [ ] T014 [US1] Create `AuthService` in `frontend-app/src/services/authService.js` to handle API calls to `POST /api/auth/login`.
- [ ] T015 [US1] Implement logic in `LoginPage` to call `AuthService` on form submission and redirect to homepage on successful login.
- [ ] T016 [US1] Add routing for `/login` and `/homepage` in `frontend-app/src/App.js` (or equivalent main routing file).

### Test Tasks (if applicable)

- [ ] T017 [US1] Write backend integration tests for successful login via `AuthController` in `backend-app/src/test/java/eu/pmant/app/controller/AuthControllerIntegrationTest.java`.
- [ ] T018 [US1] Write frontend E2E test for successful login and homepage redirection in `frontend-app/src/test/e2e/login.spec.js`.

## Phase 4: User Story 2 - Failed Login (Incorrect Credentials) (P1)

**Goal**: Provide clear error feedback to users when login attempts fail due to incorrect credentials.

**Independent Test Criteria**: A user can enter invalid credentials on the login page and sees the error message "Login or user does not exist".

### Implementation Tasks

- [ ] T019 [US2] Modify `UserService` to return specific error indicators for invalid credentials in `backend-app/src/main/java/eu/pmant/app/service/UserService.java`.
- [ ] T020 [US2] Implement IP-based rate limiting for login attempts in `backend-app/src/main/java/eu/pmant/app/config/RateLimitingConfig.java` (or similar).
- [ ] T021 [US2] Modify `AuthController` to handle `401 Unauthorized` responses and return the error message "Login or user does not exist" in `backend-app/src/main/java/eu/pmant/app/controller/AuthController.java`.
- [ ] T022 [US2] Implement error display logic in `LoginPage` to show the message "Login or user does not exist" on failed login attempts in `frontend-app/src/pages/LoginPage.js`.

### Test Tasks (if applicable)

- [ ] T023 [US2] Write backend integration tests for failed login attempts (incorrect password, non-existent user) in `backend-app/src/test/java/eu/pmant/app/controller/AuthControllerIntegrationTest.java`.
- [ ] T024 [US2] Write frontend E2E test for failed login and error message display in `frontend-app/src/test/e2e/login.spec.js`.

## Final Phase: Polish & Cross-Cutting Concerns

**Goal**: Ensure code quality, adherence to standards, and overall system robustness.

- [ ] T025 Run `npm test && npm run lint` in `frontend-app` and address any reported issues.
- [ ] T026 Run Maven tests in `backend-app` and address any reported issues.
- [ ] T027 Review and update `README.md` with instructions for running and testing the login feature.
- [ ] T028 Ensure all functional requirements (FR-001 to FR-009) and success criteria (SC-001 to SC-006) are met.

## Dependencies

The following table illustrates the completion order of user stories:

| User Story | Depends On |
| :--------- | :--------- |
| US1        | Foundational |
| US2        | Foundational |

## Parallel Execution Examples

-   **Backend Development**:
    -   `T008 [US1] Implement UserService method for user authentication...`
    -   `T009 [US1] Create LoginRequest DTO...`
    -   `T010 [US1] Create LoginResponse DTO...`
    -   `T011 [US1] Implement AuthController with POST /api/auth/login...`
    -   `T012 [US1] Configure Spring Security for session management...`
    -   `T019 [US2] Modify UserService to return specific error indicators...`
    -   `T020 [US2] Implement IP-based rate limiting...`
    -   `T021 [US2] Modify AuthController to handle 401 Unauthorized responses...`
-   **Frontend Development**:
    -   `T013 [US1] Create LoginPage component...`
    -   `T014 [US1] Create AuthService...`
    -   `T015 [US1] Implement logic in LoginPage to call AuthService...`
    -   `T016 [US1] Add routing for /login and /homepage...`
    -   `T022 [US2] Implement error display logic in LoginPage...`
-   **Testing**:
    -   `T017 [US1] Write backend integration tests for successful login...`
    -   `T018 [US1] Write frontend E2E test for successful login...`
    -   `T023 [US2] Write backend integration tests for failed login attempts...`
    -   `T024 [US2] Write frontend E2E test for failed login...`

## Implementation Strategy

The implementation will follow an MVP-first approach, delivering functionality incrementally. User Story 1 (Successful Login) will be prioritized as the initial MVP, followed by User Story 2 (Failed Login). Cross-cutting concerns and polish will be addressed in the final phase. Each user story will be developed and tested independently to ensure modularity and maintainability.
