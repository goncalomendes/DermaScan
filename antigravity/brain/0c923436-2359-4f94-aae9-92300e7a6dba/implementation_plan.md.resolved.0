# Implementation Plan - Complete Backend Core Features

## Goal Description

Complete the DermaScan backend core functionality by:
1. Switching from PostgreSQL to H2 in-memory database for faster development
2. Implementing authenticated scan upload with proper user association
3. Adding endpoints to retrieve user's scan history
4. Configuring multipart file upload settings
5. Testing the complete backend authentication and scan flow

## Proposed Changes

### Database Configuration

#### [MODIFY] [application.properties](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/src/main/resources/application.properties)
- Replace PostgreSQL configuration with H2 in-memory database
- Add JWT secret configuration
- Configure file upload size limits (10MB max)
- Enable H2 console for easier debugging

### Scan Controller Enhancements

#### [MODIFY] [ScanController.java](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/src/main/java/com/dermascan/api/controller/ScanController.java)
- **Fix upload endpoint** to properly associate scans with authenticated users
- Extract user from security context using `@AuthenticationPrincipal`
- Actually save scan to database (currently commented out)
- Add proper error handling and validation
- **Add GET endpoints**:
  - `GET /api/scans` - Retrieve all scans for authenticated user
  - `GET /api/scans/{id}` - Retrieve specific scan by ID (only if owned by user)

### Scan Model Enhancement

#### [MODIFY] [Scan.java](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/src/main/java/com/dermascan/api/model/Scan.java)
- Add `bodyLocation` field to track where on the body the scan was taken
- This will be useful for the mobile app's body mapping feature

### Response DTOs (Data Transfer Objects)

#### [NEW] [ScanResponse.java](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/src/main/java/com/dermascan/api/payload/ScanResponse.java)
- Create DTO to return scan data without exposing internal User entity details
- Include: id, imagePath, analysisResult, timestamp, bodyLocation

### Dependencies

#### [MODIFY] [pom.xml](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/pom.xml)
- Add H2 database dependency
- Keep PostgreSQL dependency but make it optional for future production use

## Verification Plan

### Automated Tests

Currently, there are no existing tests in the project. We will verify manually and create tests in a future iteration.

### Manual Verification

**Step 1: Start the Backend Server**
```bash
cd backend-api
mvn spring-boot:run
```
The server should start on `http://localhost:8080` without database connection errors.

**Step 2: Test User Registration**
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\"}"
```
Expected: Success message "User registered successfully!"

**Step 3: Test User Login**
```bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"password\":\"password123\"}"
```
Expected: JSON response with JWT token like `{"token":"eyJhbGc...", "id":1, "username":"testuser", "email":"test@example.com"}`

**Step 4: Test Image Upload (with JWT)**
```bash
# Replace YOUR_JWT_TOKEN with actual token from Step 3
curl -X POST http://localhost:8080/api/scans/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@path/to/test/image.jpg" \
  -F "bodyLocation=left-arm"
```
Expected: Success message with scan ID

**Step 5: Test Get User Scans**
```bash
curl -X GET http://localhost:8080/api/scans \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```
Expected: JSON array with the uploaded scan(s)

**Step 6: Verify H2 Console (Optional)**
- Navigate to `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:dermascan`
- Username: `sa`
- Password: (empty)
- Check that `users` and `scans` tables exist and contain data
