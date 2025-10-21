# Tasks for Minimal Docker Compose Application

**Branch**: `001-specify-scripts-bash` | **Date**: 2025-10-18 | **Spec**: /Users/kerba/Documents/pmant/specs/001-specify-scripts-bash/spec.md

## Phase 1: Setup (Project Initialization)

- [x] T001 Create project root directories: `backend-app`, `frontend-app`, `nginx`, `db`
- [x] T002 Create `docker-compose.yml` in project root
- [x] T003 Create `docker-compose.override.yml` in project root

## Phase 2: Foundational (Blocking Prerequisites)

- [x] T004 Configure `nginx/nginx.conf` for frontend proxy and routing
- [x] T005 Initialize backend-app with Maven and Spring Boot in `backend-app/`
- [x] T006 Initialize frontend-app with React in `frontend-app/`
- [x] T007 Configure PostgreSQL database service in `docker-compose.yml`

## Phase 3: User Story 1 - Launch Minimal Application (Priority: P1)

**Story Goal**: As a developer, I want to launch the minimal application using Docker Compose so that I can quickly see the frontend and backend services running.
**Independent Test Criteria**: Can be fully tested by running `docker-compose up` and verifying that all services start without errors and are accessible.

- [x] T008 [US1] Create minimal `index.html` in `frontend-app/public/index.html`
- [x] T009 [US1] Configure frontend service in `docker-compose.yml` to serve `frontend-app/public`
- [x] T010 [US1] Configure backend service in `docker-compose.yml`
- [x] T011 [US1] Verify all services start successfully with `docker-compose up`
- [x] T012 [US1] Verify `index.html` is accessible in browser

## Phase 4: User Story 2 - Ping Backend from Frontend (Priority: P1)

**Story Goal**: As a developer, I want to click the "ping" button on the frontend so that a request is sent to the backend and I receive a response, verifying inter-service communication.
**Independent Test Criteria**: Can be fully tested by interacting with the frontend in a browser and observing the network requests and responses.

- [x] T013 [US2] Add "ping" button to `frontend-app/public/index.html`
- [x] T014 [US2] Implement JavaScript in `frontend-app/public/index.html` to send HTTP request to backend on button click
- [x] T015 [US2] Implement backend endpoint in `backend-app/src/main/java/eu/pmant/app/controller/PingController.java` to respond to "ping" request
- [x] T016 [US2] Implement frontend display of "Loading..." text in `frontend-app/public/index.html`
- [x] T017 [US2] Implement frontend display of backend response in `frontend-app/public/index.html`
- [x] T018 [US2] Implement frontend error display for unreachable backend in `frontend-app/public/index.html`
- [x] T019 [US2] Configure backend logging with SLF4J and Logback (console appender) in `backend-app/pom.xml` and `backend-app/src/main/resources/logback.xml`
- [x] T020 [US2] Verify "ping" functionality, loading state, and response display
- [x] T021 [US2] Verify error display for unreachable backend
- [x] T022 [US2] Verify backend logs incoming "ping" requests

## Phase 5: Polish & Cross-Cutting Concerns

- [x] T023 Review and refine `docker-compose.yml` and `docker-compose.override.yml` for local development
- [x] T024 Add basic `README.md` to project root with setup and run instructions

## Dependency Graph (User Story Completion Order)

- User Story 1 -> User Story 2

## Parallel Execution Examples

- Within User Story 1: T008, T009, T010 can be done in parallel after T001-T007.
- Within User Story 2: T013, T014, T015, T016, T017, T018, T019 can be done in parallel after T008-T012.

## Implementation Strategy

- MVP first, focusing on User Story 1 and then User Story 2. Incremental delivery.
