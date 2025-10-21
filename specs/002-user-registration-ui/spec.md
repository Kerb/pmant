# Feature Specification: User Registration with Centered UI

**Feature Branch**: `004-user-registration-ui`  
**Created**: 2025-10-19  
**Status**: Draft  
**Input**: User description: "Я, как пользователь, хочу иметь возможность зарегистрироваться в системе. Форма регистрации должна запрашивать login, password. При регистрации пользователь и его логин и пароль должны сохраняться в БД. Пароль не может быть короче 8 символов. Форма ввода логина и пароля должна находиться в центре экрана"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Successful User Registration with Centered Form (Priority: P1)

A new user wants to create an account. They navigate to the registration page, where they find the form for login and password input visually centered on the screen. They provide a unique login and a password that meets the minimum length requirement. The system should successfully create the user, store their credentials, and automatically log them in.

**Why this priority**: This is the core functionality, enabling new users to access the system with an optimal user experience. Without it, the system cannot acquire users.

**Independent Test**: Can be fully tested by submitting a registration form with valid data and verifying account creation, login functionality, and the visual centering of the form.

**Acceptance Scenarios**:

1. **Given** a user is on the registration page, **When** they enter a unique login and a password (8+ characters), **Then** their account is created, they are automatically logged in, and the registration form was displayed in the center of the screen.
2. **Given** a user attempts to register with an already existing login, **When** they submit the form, **Then** the system informs them that the login is already taken, and the form remains centered.

---

### User Story 2 - Password Too Short with Centered Form (Priority: P2)

A new user attempts to register with a password shorter than 8 characters. The centered form should display an error message, and the system should reject the registration.

**Why this priority**: This ensures basic security requirements are met and guides users to create strong passwords, while maintaining a consistent UI.

**Independent Test**: Can be fully tested by attempting registration with a short password and observing the error message on the centered form.

**Acceptance Scenarios**:

1. **Given** a user is on the registration page, **When** they enter a login and a password less than 8 characters long, **Then** the system displays an error message indicating the minimum password length requirement on the centered form, and the account is not created.

---

### User Story 3 - Responsive Centered Form (Priority: P2)

A user accesses the registration page on various devices and screen sizes. The login and password input form should remain visually centered and responsive to different screen dimensions.

**Why this priority**: Ensures a consistent and accessible user experience across all supported devices and screen orientations.

**Independent Test**: Can be fully tested by navigating to the registration page on various devices and screen sizes, and visually confirming the form's central alignment and responsiveness.

**Acceptance Scenarios**:

1. **Given** a user navigates to the registration page, **When** the page loads on a desktop browser, **Then** the login and password input form is displayed in the exact center of the visible screen area.
2. **Given** a user resizes their browser window or changes device orientation on a mobile device, **When** the layout adjusts, **Then** the login and password input form remains centered on the screen.

---

### Edge Cases

- How does the system handle special characters in login or password?
- What is the maximum length for login and password?
- How does the form centering behave on extremely small screen sizes where content might be constrained?
- What happens if other UI elements (e.g., headers, footers) are present? Does the form center relative to the available content area or the entire viewport?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST provide a user registration interface.
- **FR-002**: The registration interface MUST require a login and a password.
- **FR-003**: The system MUST store the user's login and password securely in the database upon successful registration.
- **FR-004**: The system MUST enforce a minimum password length of 8 characters during registration.
- **FR-005**: The system MUST provide feedback to the user if the password does not meet the minimum length requirement.
- **FR-006**: The system MUST ensure that each user login is unique.
- **FR-007**: The system MUST display the login and password input form in the horizontal and vertical center of the screen's available content area.
- **FR-008**: The centering of the form MUST be responsive, adapting correctly to different screen sizes, resolutions, and device orientations (e.g., mobile portrait/landscape).
- **FR-009**: The system MUST use HTTPS with TLS 1.2 or higher for all registration data transmission.
- **FR-010**: The system MUST prevent registration with empty login or password fields and provide appropriate error messages.

### Key Entities *(include if feature involves data)*

- **User**: Represents a registered individual in the system.
    *   Attributes: Login (unique identifier), Password (hashed using BCrypt with a randomly generated salt per user).

### Technical Stack

- Backend: Java 17, Spring Boot
- Frontend: React (JavaScript/TypeScript)

## Clarifications

### Session 2025-10-21

- Q: What specific password hashing algorithm and salt generation strategy should be used for secure storage? → A: BCrypt with a randomly generated salt per user.
- Q: After successful registration, should the user be automatically logged in, or redirected to a separate success/verification page? → A: Automatically log in the user.
- Q: What transport layer security (e.g., HTTPS/TLS version) is required for registration data transmission? → A: HTTPS with TLS 1.2 or higher.
- Q: How should the system handle attempts to register with an empty login or password? → A: Display an error message and prevent registration.
- Q: Should the spec explicitly state the backend (Java 17, Spring Boot) and frontend (React) technologies, or are these implicit from project context? → A: Explicitly state the technologies in the spec.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 95% of new users can successfully complete the registration process within 30 seconds.
- **SC-002**: The system successfully prevents 100% of registration attempts with passwords shorter than 8 characters.
- **SC-003**: User credentials (login and hashed password) are securely stored in the database and are retrievable for authentication purposes.
- **SC-004**: The system maintains a unique login for each registered user.
- **SC-005**: The login and password input form is visually centered on 100% of supported devices and screen resolutions, as verified by visual inspection and automated UI tests.
- **SC-006**: User feedback indicates that the registration form's positioning is intuitive and contributes positively to the overall user experience.