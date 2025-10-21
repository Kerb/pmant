# Tasks: User Registration with Centered UI

**Input**: Design documents from `/specs/002-user-registration-ui/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: The examples below include test tasks. Tests are OPTIONAL - only include them if explicitly requested in the feature specification.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions
- **Single project**: `src/`, `tests/` at repository root
- **Web app**: `backend-app/src/`, `frontend-app/src/`
- **Mobile**: `api/src/`, `ios/src/` or `android/src/`
- Paths shown below assume single project - adjust based on plan.md structure

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Integrate jOOQ for database access in `backend-app/pom.xml`
- [x] T002 Integrate Liquibase for database migrations in `backend-app/pom.xml`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [x] T003 Create User entity in `backend-app/src/main/java/eu/pmant/app/model/User.java`
- [x] T004 Implement password hashing utility (BCrypt) in `backend-app/src/main/java/eu/pmant/app/util/PasswordUtil.java`
- [x] T005 Implement UserRepository for database operations using jOOQ in `backend-app/src/main/java/eu/pmant/app/repository/UserRepository.java`
- [x] T006 Implement UserService for core user registration logic in `backend-app/src/main/java/eu/pmant/app/service/UserService.java`
- [x] T007 Create initial Liquibase changelog for User table in `backend-app/src/main/resources/db/changelog/db.changelog-master.xml`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Successful User Registration with Centered Form (Priority: P1) 🎯 MVP

**Goal**: Allow a new user to successfully register with valid credentials, automatically log them in, and display the form centered.

**Independent Test**: Submit a registration form with unique login and valid password, verify account creation, automatic login, and visual centering of the form.

### Implementation for User Story 1

- [x] T008 [P] [US1] Create RegistrationRequest DTO in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationRequest.java`
- [x] T009 [P] [US1] Create RegistrationResponse DTO in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationResponse.java`
- [x] T010 [US1] Implement `POST /api/register` endpoint in `backend-app/src/main/java/eu/pmant/app/controller/RegistrationController.java`
- [x] T011 [US1] Implement frontend RegistrationForm component in `frontend-app/src/components/RegistrationForm.js`
- [x] T012 [US1] Implement frontend registration service in `frontend-app/src/services/authService.js`
- [x] T013 [US1] Implement RegistrationPage in `frontend-app/src/pages/RegistrationPage.js`
- [x] T014 [US1] Add routing for `/register` to `frontend-app/src/index.js`
- [x] T015 [US1] Implement basic CSS for centering the form in `frontend-app/src/index.css` (or similar)
- [x] T016 [US1] Implement automatic login handling in frontend after successful registration in `frontend-app/src/services/authService.js`
- [x] T017 [US1] Implement unit tests for `UserService` in `backend-app/src/test/java/eu/pmant/app/service/UserServiceTest.java`
- [x] T018 [US1] Implement integration tests for `RegistrationController` in `backend-app/src/test/java/eu/pmant/app/controller/RegistrationControllerIntegrationTest.java`
- [x] T019 [US1] Implement E2E test for successful registration flow using Cypress/Playwright (placeholder) in `frontend-app/src/test/e2e/registration.spec.js`

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - Password Too Short with Centered Form (Priority: P2)

**Goal**: Prevent registration with passwords shorter than 8 characters and display an error message.

**Independent Test**: Attempt registration with a short password, verify error message and rejection.

### Implementation for User Story 2

- [x] T020 [US2] Add password length validation to `RegistrationRequest` DTO using annotations in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationRequest.java`
- [x] T021 [US2] Implement error handling for password length validation in `RegistrationController` in `backend-app/src/main/java/eu/pmant/app/controller/RegistrationController.java`
- [x] T022 [US2] Implement frontend validation and error message display for password length in `frontend-app/src/components/RegistrationForm.js`
- [x] T023 [US2] Implement unit tests for password validation in `UserServiceTest.java` (Validation handled at controller level, tested in integration tests)
- [x] T024 [US2] Implement integration tests for password validation via API in `RegistrationControllerIntegrationTest.java`
- [x] T025 [US2] Implement E2E test for password too short scenario in `frontend-app/src/test/e2e/registration.spec.js`

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - Responsive Centered Form (Priority: P2)

**Goal**: Ensure the registration form remains visually centered and responsive across various screen sizes.

**Independent Test**: Visually inspect the registration page on different devices/screen sizes, verify central alignment and responsiveness.

### Implementation for User Story 3

- [x] T026 [US3] Refine CSS for responsive centering of the form in `frontend-app/src/index.css` (or similar)
- [x] T027 [US3] Implement UI tests for responsiveness (e.g., visual regression tests) in `frontend-app/src/test/ui/responsive.spec.js`

**Checkpoint**: All user stories should now be independently functional

---

## Final Phase: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T028 [P] Implement unique login validation in `backend-app/src/main/java/eu/pmant/app/service/UserService.java` and `backend-app/src/main/java/eu.pmant/app/repository/UserRepository.java`
- [x] T029 [P] Implement error handling for empty login/password fields in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationRequest.java` and `backend-app/src/main/java/eu/pmant/app/controller/RegistrationController.java`
- [x] T030 [P] Ensure HTTPS with TLS 1.2 or higher is enforced for all registration data transmission (configuration in Nginx and Spring Boot) in `nginx/nginx.conf` and `backend-app/src/main/resources/application.properties`
- [x] T031 Add logging for registration events in `backend-app/src/main/java/eu/pmant/app/service/UserService.java`
- [x] T032 Update `quickstart.md` with instructions to run the application and test registration.

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable

### Within Each User Story

- Tests (if included) MUST be written and FAIL before implementation
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Models within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together (if tests requested):
Task: "Implement unit tests for `UserService` in `backend-app/src/test/java/eu/pmant/app/service/UserServiceTest.java`"
Task: "Implement integration tests for `RegistrationController` in `backend-app/src/test/java/eu/pmant/app/controller/RegistrationControllerIntegrationTest.java`"
Task: "Implement E2E test for successful registration flow using Cypress/Playwright (placeholder) in `frontend-app/src/test/e2e/registration.spec.js`"

# Launch all models for User Story 1 together:
Task: "Create RegistrationRequest DTO in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationRequest.java`"
Task: "Create RegistrationResponse DTO in `backend-app/src/main/java/eu/pmant/app/dto/RegistrationResponse.java`"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
