# Implementation Plan - Backend Development

## Goal Description
Implement core backend features: User Authentication, Image Upload, and Database integration using Spring Boot and PostgreSQL.

## Proposed Changes

### Database Configuration
- **File**: `src/main/resources/application.properties`
- **Action**: Configure PostgreSQL connection details (already present, need to verify against user's local DB or use H2 for dev if preferred, but sticking to Postgres as per plan).
- **Entities**:
    - `User`: ID, username, password, email. (One-to-Many relationship with Scan)
    - `Scan`: ID, user_id, image_path, analysis_result, timestamp.

### User Authentication
- **Dependencies**: Add `spring-boot-starter-security`.
- **Components**:
    - `AuthController`: Login/Register endpoints.
    - `UserService`: Handle user logic.
    - `UserRepository`: JPA repository.
    - `SecurityConfig`: Configure security chain (allow public access to auth endpoints).

### Image Upload API
- **Components**:
    - `ScanController`: Upload endpoint.
    - `ScanService`: Handle file storage and DB record creation.
    - `ScanRepository`: JPA repository.
- **Storage**: Store files locally in `uploads/` directory for now.

## Verification Plan

### Automated Tests
- Create `AuthControllerTest` to verify login/register.
- Create `ScanControllerTest` to verify upload.

### Manual Verification
- Use `curl` or Postman (if available) to test endpoints.
- Verify files appear in `uploads/` folder.
- Verify records in Database.
