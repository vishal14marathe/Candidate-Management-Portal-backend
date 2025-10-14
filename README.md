# Candidate Management System - Backend API

A Spring Boot backend system for managing candidates with JWT authentication, Excel operations, email notifications, and scheduled admin reports.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.2.0**
- **H2 Database** (file-based)
- **Maven** (build tool)
- **JWT** (authentication)
- **Apache POI** (Excel import/export)
- **Spring Mail** (email notifications)
- **Spring Scheduler** (daily admin emails)

## Features

✅ JWT-based authentication with ROLE_ADMIN and ROLE_USER access control  
✅ Candidate CRUD operations with filtering support  
✅ Resume file upload/download  
✅ Excel bulk import with duplicate validation  
✅ Excel export with dynamic filtering  
✅ Email notifications for candidate confirmations  
✅ Daily admin summary emails (scheduled at 9 PM)  
✅ Global exception handling  
✅ Input validation

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### Default Admin Credentials

- **Email:** admin@candidatesystem.com
- **Password:** admin123

## API Endpoints

### Authentication APIs

#### 1. Register Candidate
```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

#### 2. Login (Admin or Candidate)
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@candidatesystem.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@candidatesystem.com",
  "fullName": "System Admin",
  "role": "ROLE_ADMIN",
  "message": "Login successful"
}
```

### Candidate Management APIs (Admin Only)

**Note:** All admin endpoints require `Authorization: Bearer <token>` header

#### 3. Add Candidate Manually
```http
POST /api/admin/candidates
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "Ravi Sharma",
  "age": 24,
  "qualification": "Graduate",
  "identityProofNumber": "ID12345",
  "location": "New Delhi",
  "email": "ravi@gmail.com",
  "mobileNumber": "9876543210",
  "occupationStatus": "Available"
}
```

#### 4. Get All Candidates (with optional filters)
```http
GET /api/admin/candidates?qualification=Graduate&occupationStatus=Available&location=New%20Delhi
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "fullName": "Ravi Sharma",
    "age": 24,
    "qualification": "Graduate",
    "identityProofNumber": "ID12345",
    "location": "New Delhi",
    "email": "ravi@gmail.com",
    "mobileNumber": "9876543210",
    "occupationStatus": "Available",
    "resumePath": null,
    "createdAt": "2025-10-14T18:30:00"
  }
]
```

#### 5. Get Candidate by ID
```http
GET /api/admin/candidates/{id}
Authorization: Bearer <token>
```

#### 6. Delete Candidate
```http
DELETE /api/admin/candidates/{id}
Authorization: Bearer <token>
```

### Candidate Update API (User/Admin)

#### 7. Update Candidate
```http
PUT /api/candidates/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "Ravi Kumar Sharma",
  "age": 25,
  "qualification": "Post Graduate",
  "identityProofNumber": "ID12345",
  "location": "Mumbai",
  "email": "ravi@gmail.com",
  "mobileNumber": "9876543210",
  "occupationStatus": "Employed"
}
```

### File Upload/Download APIs

#### 8. Upload Resume
```http
POST /api/candidates/upload-resume/{id}
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <resume.pdf>
```

#### 9. Download Resume
```http
GET /api/candidates/download-resume/{fileName}
Authorization: Bearer <token>
```

### Excel Import/Export APIs (Admin Only)

#### 10. Import Candidates from Excel
```http
POST /api/admin/candidates/import-excel
Authorization: Bearer <token>
Content-Type: multipart/form-data

file: <candidates.xlsx>
```

**Excel Format:**
| Full Name | Age | Qualification | Identity Proof Number | Location | Email | Mobile Number | Occupation Status |
|-----------|-----|---------------|----------------------|----------|-------|---------------|-------------------|
| John Doe  | 25  | Graduate      | ID001                | Delhi    | john@example.com | 9999999999 | Available |

**Response:**
```json
{
  "message": "Import completed with errors",
  "errors": [
    "Row 3: Email already exists - john@example.com",
    "Row 5: Identity proof number already exists - ID001"
  ]
}
```

#### 11. Export Candidates to Excel
```http
GET /api/admin/candidates/export-excel?qualification=Graduate&occupationStatus=Available
Authorization: Bearer <token>
```

Downloads `candidates.xlsx` file with filtered data.

## Database

- **Type:** H2 (file-based)
- **Location:** `./data/candidatedb`
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/candidatedb`
  - Username: `sa`
  - Password: (leave empty)

## Email Configuration

The system supports email notifications for:
1. **Candidate Registration Confirmation** - Sent when a candidate registers
2. **Daily Admin Summary** - Sent at 9 PM daily with count of new candidates

To enable emails, configure in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Scheduled Tasks

- **Daily Admin Summary:** Runs every day at 9 PM (21:00)
  - Counts candidates added in last 24 hours
  - Sends email to admin

## Error Handling

All errors are handled globally with appropriate HTTP status codes:

- `400 Bad Request` - Validation errors, duplicate entries
- `401 Unauthorized` - Invalid credentials
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

## Project Structure

```
com.project
├── config          # Security, JWT, ModelMapper configurations
├── controller      # REST API controllers
├── dto             # Data Transfer Objects
├── model           # JPA entities
├── repository      # JPA repositories
├── service         # Business logic
└── util            # Exception handlers, utilities
```

## Security

- JWT token-based authentication
- Password encryption using BCrypt
- Role-based access control (ADMIN/USER)
- Session stateless (no server-side sessions)

## Testing

Use tools like Postman or cURL to test the APIs:

1. Login as admin to get JWT token
2. Use token in Authorization header for protected endpoints
3. Test CRUD operations, file uploads, Excel import/export

## Notes

- Resumes are stored in `./uploads/resumes/` directory
- H2 database file is stored in `./data/` directory
- Email service gracefully falls back to console logging if not configured
- Admin user is auto-created on first startup
