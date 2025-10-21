# Data Model: User Login Authentication

## Entity: User

Represents an individual with an account in the system.

### Attributes:

-   `login` (String): Unique identifier for the user (e.g., username or email address). Treated as case-insensitive during authentication.
-   `passwordHash` (String): Securely stored hashed representation of the user's password.

### Validation Rules:

-   **Password Policy**: Passwords must be a minimum of 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character.

### Relationships:

-   None directly relevant to the login process within this feature.
