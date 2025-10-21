# Feature Specification: User Login Authentication

**Feature Branch**: `003-user-login-auth`  
**Created**: October 21, 2025  
**Status**: Draft  
**Input**: User description: "Я, как пользователь, хочу иметь возможность ввести логин и пароль в форму авторизации и залогиниться. Логин и пароль нужно проверить в БД. Если введен неверный пароль, то вывести сообщение об ошибке: "Login or user does not exists". Если логин и пароль верные, то нужно перенаправить пользователя на его Homepage"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Successful Login (Priority: P1)

As a user, I want to enter my correct login and password into the authorization form so that I can access my personalized homepage.

**Why this priority**: This is the primary and most critical user flow, enabling access to the application's core functionality.

**Independent Test**: Can be fully tested by providing valid credentials and verifying redirection to the homepage.

**Acceptance Scenarios**:

1.  **Given** I am on the login page, **When** I enter a valid login and password and submit the form, **Then** I am redirected to my homepage.

---

### User Story 2 - Failed Login (Incorrect Credentials) (Priority: P1)

As a user, if I enter incorrect login or password into the authorization form, I want to see an error message so that I understand why I cannot log in.

**Why this priority**: This is a critical user flow for handling errors and providing feedback, preventing user frustration.

**Independent Test**: Can be fully tested by providing invalid credentials and verifying the display of the error message.

**Acceptance Scenarios**:

1.  **Given** I am on the login page, **When** I enter an invalid login or password and submit the form, **Then** I see the error message "Login or user does not exist".

---

### Edge Cases

-   **Empty Credentials**: If a user attempts to log in with an empty username or password, the system should prevent submission (client-side validation) or treat it as invalid credentials (server-side validation) and display the appropriate error message.
-   **Database Unavailability**: If the authentication database is temporarily unavailable, the system should display a generic error message indicating a service issue, rather than a specific credential error.

## Requirements *(mandatory)*

### Functional Requirements

-   **FR-001**: The system MUST provide a user interface with input fields for a login (username/email) and a password, and a submit button for authorization.
-   **FR-002**: The system MUST securely transmit the entered credentials for validation.
-   **FR-003**: The system MUST validate the provided login credentials against a stored database of users.
-   **FR-004**: If the login credentials are valid, the system MUST authenticate the user and establish a session.
-   **FR-005**: If the login credentials are valid, the system MUST redirect the authenticated user to their designated homepage.
-   **FR-006**: If the login credentials are invalid (incorrect login or password), the system MUST display the exact error message "Login or user does not exist" on the login page.
-   **FR-007**: The system MUST enforce a password policy requiring a minimum of 8 characters, including at least one uppercase letter, one lowercase letter, one number, and one special character.
-   **FR-008**: The system MUST implement rate limiting for login attempts based on IP address to mitigate brute-force attacks.
-   **FR-009**: The system MUST treat user logins (username/email) as case-insensitive during authentication.

### Key Entities *(include if feature involves data)*

-   **User**: Represents an individual with an account in the system.
    -   **Attributes**:
        -   `login`: Unique identifier for the user (e.g., username or email address).
        -   `passwordHash`: Securely stored hashed representation of the user's password.

## Success Criteria *(mandatory)*

### Measurable Outcomes

-   **SC-001**: 99% of users can successfully complete the login process within 3 seconds when providing correct credentials.
-   **SC-002**: The system accurately displays the error message "Login or user does not exist" for 100% of invalid login attempts.
-   **SC-003**: Authenticated users are successfully redirected to their homepage in 100% of successful login attempts.
-   **SC-004**: The login form is accessible and usable across all supported browsers and devices.
-   **SC-005**: The login system MUST support up to 1,000 concurrent users without performance degradation.
-   **SC-006**: The authentication service MUST maintain a 99% uptime, allowing for no more than approximately 3.65 days of downtime per year.

## Clarifications

### Session 2025-10-21

- Q: What are the requirements for user passwords? → A: Minimum 8 characters, including uppercase, lowercase, numbers, and special characters.
- Q: How should the system protect against brute-force login attempts? → A: Rate limit login attempts per IP address.
- Q: Should the user login (username/email) be treated as case-sensitive or case-insensitive during authentication? → A: Case-insensitive (e.g., "User@example.com" is same as "user@example.com").
- Q: What is the expected number of concurrent users or daily active users for the login system? → A: Up to 1,000 concurrent users.
- Q: What is the target uptime percentage for the authentication service? → A: 99% (allowing ~3.65 days downtime per year).