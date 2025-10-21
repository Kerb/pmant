# Data Model for User Registration Feature

## Entity: User

Represents a registered individual in the system.

### Attributes:

- **Login**: 
    - **Type**: String
    - **Constraints**: Unique, Not Null, Minimum length (to be determined, but typically > 0)
    - **Description**: Unique identifier for the user. Used for authentication.

- **Password**: 
    - **Type**: String (hashed)
    - **Constraints**: Not Null, Minimum length of 8 characters (before hashing)
    - **Description**: User's password, stored as a BCrypt hash with a randomly generated salt per user.

### Relationships:

- None explicitly defined for this feature.

### Validation Rules (derived from Functional Requirements):

- **Login Uniqueness**: Each user login must be unique (FR-006).
- **Password Length**: Password must be at least 8 characters long (FR-004).
- **Non-empty Fields**: Login and Password fields must not be empty (FR-010).
