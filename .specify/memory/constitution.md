<!--
Sync Impact Report:
Version change: 1.3.0 → 1.4.0
Modified principles: IV. Database (refined), VIII. Database Configuration (added)
Added sections: None
Removed sections: None
Templates requiring updates:
- .specify/templates/plan-template.md: ✅ updated (no changes needed)
- .specify/templates/spec-template.md: ✅ updated (no changes needed)
- .specify/templates/tasks-template.md: ✅ updated (no changes needed)
- .specify/templates/agent-file-template.md: ✅ updated (no changes needed)
- .gemini/commands/*.toml: ✅ updated (no changes needed)
Follow-up TODOs: None
-->
# Application Constitution

## Core Principles

### I. Component Isolation
All application components (backend, frontend, database) must run in isolated Docker containers.

### II. Backend Technology Stack
The backend application must be developed using Java 17 and Spring Boot.

### III. Frontend Technology Stack
The frontend application must be developed using React.

### IV. Database
A dedicated database component, utilizing PostgreSQL, must be included in the application architecture.

### V. Local Development Environment
The project root must contain `docker-compose.yml` for defining the application's services and `docker-compose.override.yml` for local development configurations.

### VI. Backend Project Structure and Build Tool
The backend application code must be located in the `backend-app` folder, and Maven must be used as the build tool.

### VII. Frontend Project Structure
The frontend application code must be located in the `frontend-app` folder at the project root.

### VIII. Database Configuration
The database container must be named `db`, run from Docker, and use PostgreSQL as the database system.

## Technology Stack

The application will utilize the following core technologies:
- Backend: Java 17, Spring Boot, Maven
- Frontend: React
- Database: PostgreSQL
- Containerization: Docker

## Deployment Strategy

The application components will be deployed as isolated Docker containers, ensuring portability and environment consistency. The project will utilize `docker-compose.yml` and `docker-compose.override.yml` for defining and managing these containerized services during local development.

## Governance

This Constitution outlines the fundamental principles and architectural decisions for the project. All development activities must adhere to these principles. Amendments to this Constitution require careful consideration and approval.

**Version**: 1.4.0 | **Ratified**: 2025-10-18 | **Last Amended**: 2025-10-18