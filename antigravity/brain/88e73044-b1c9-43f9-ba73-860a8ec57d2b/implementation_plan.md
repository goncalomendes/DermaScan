# Fix Compilation Error in User.java

The build failed because `SimpleGrantedAuthority` cannot be found in `User.java`. This is due to a missing import statement.

## Proposed Changes

### Backend API

#### [MODIFY] [User.java](file:///c:/Users/tiago/.gemini/antigravity/scratch/DermaScan/backend-api/src/main/java/com/dermascan/api/model/User.java)
- Add `import org.springframework.security.core.authority.SimpleGrantedAuthority;`

## Verification Plan

### Automated Tests
- Run `mvn clean test` in `backend-api` directory.
- Verify that the compilation error is resolved and tests run (even if they fail, the compilation error should be gone).
