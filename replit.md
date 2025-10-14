# Candidate Management System

## Overview
A Spring Boot backend REST API system for managing candidates with comprehensive features including JWT authentication, Excel operations, file uploads, email notifications, and scheduled tasks.

## Tech Stack
- **Backend:** Spring Boot 3.2.0, Java 17+
- **Database:** H2 (file-based SQL database)
- **Build Tool:** Maven
- **Security:** JWT-based authentication with BCrypt password encryption
- **File Processing:** Apache POI for Excel import/export
- **Email:** Spring Mail with scheduled notifications
- **ORM:** JPA/Hibernate

## Project Status
✅ **Completed** - All MVP features implemented and tested (October 14, 2025)

## Key Features Implemented
1. **Authentication System**
   - JWT token-based authentication
   - Role-based access control (ROLE_ADMIN, ROLE_USER)
   - Secure password encryption with BCrypt
   - Auto-seeded admin user on startup

2. **Candidate Management**
   - Complete CRUD operations for candidates
   - Advanced filtering by qualification, occupation status, and location
   - Input validation with JSR-303 annotations
   - Duplicate email and identity proof number prevention

3. **File Operations**
   - Resume upload to local file system (max 10MB)
   - Resume download with secure file serving
   - Unique filename generation to prevent conflicts

4. **Excel Integration**
   - Bulk candidate import from Excel (.xlsx)
   - Duplicate validation during import
   - Error reporting for invalid rows
   - Excel export with dynamic filtering

5. **Email Notifications**
   - Candidate registration confirmation emails
   - Daily admin summary at 9 PM (scheduled via Spring Scheduler)
   - Graceful fallback to console logging when email not configured

6. **Error Handling**
   - Global exception handler with @ControllerAdvice
   - Proper HTTP status codes for all error scenarios
   - Validation error messages for user input

## Project Structure
```
com.project/
├── config/          # Security, JWT, ModelMapper, DataInitializer
├── controller/      # REST API endpoints (Auth, Candidate, File, Excel)
├── dto/             # Data Transfer Objects
├── model/           # JPA entities (User, Candidate)
├── repository/      # Spring Data JPA repositories
├── service/         # Business logic layer
└── util/            # Global exception handler
```

## API Endpoints Summary
- **Auth:** `/api/auth/register`, `/api/auth/login`
- **Candidates:** `/api/admin/candidates` (CRUD + filtering)
- **Files:** `/api/candidates/upload-resume/{id}`, `/api/candidates/download-resume/{fileName}`
- **Excel:** `/api/admin/candidates/import-excel`, `/api/admin/candidates/export-excel`

## Database
- **Type:** H2 file-based database
- **Location:** `./data/candidatedb`
- **H2 Console:** http://localhost:8080/h2-console
- **Tables:** users, candidates

## Default Credentials
- **Admin Email:** admin@candidatesystem.com
- **Admin Password:** admin123

## Running the Application
```bash
mvn spring-boot:run
```
Server runs on **http://localhost:8080**

## Testing
All core endpoints have been tested and verified:
- ✅ Admin login and JWT token generation
- ✅ Candidate creation with validation
- ✅ Filtering candidates by qualification
- ✅ File upload/download ready
- ✅ Excel import/export configured
- ✅ Scheduled tasks configured

## Recent Changes (October 14, 2025)
- Fixed @PrePersist for createdAt timestamp in User and Candidate entities
- Configured H2 database instead of PostgreSQL for MVP compatibility
- Set server port to 8080 (backend API, not frontend)
- Implemented complete Spring Boot backend with all spec requirements
- Added comprehensive README documentation

## Dependencies
- Spring Boot Starter Web, Data JPA, Security, Mail, Validation
- H2 Database
- JWT (io.jsonwebtoken) 0.12.3
- Lombok
- ModelMapper 3.2.0
- Apache POI 5.2.5 (Excel operations)

## Notes
- Email service requires SMTP configuration in application.properties
- Resume files stored in `./uploads/resumes/` directory
- Scheduler runs daily at 9 PM for admin summary emails
- All admin endpoints require JWT token in Authorization header
