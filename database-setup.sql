-- ===============================================
-- PostgreSQL Database Setup for Candidate Management System
-- ===============================================

-- Create database (run this as postgres superuser)
-- If database already exists, skip this step
CREATE DATABASE candidatedb;

-- Connect to the database
\c candidatedb;

-- The tables will be auto-created by Hibernate on first run
-- But you can manually create them if needed:

-- Users table (for authentication)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidates table
CREATE TABLE IF NOT EXISTS candidates (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    age INTEGER,
    qualification VARCHAR(100),
    location VARCHAR(100),
    occupation_status VARCHAR(50),
    identity_proof_number VARCHAR(100),
    resume_file_name VARCHAR(255),
    registration_date DATE DEFAULT CURRENT_DATE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_candidates_email ON candidates(email);
CREATE INDEX IF NOT EXISTS idx_candidates_user_id ON candidates(user_id);
CREATE INDEX IF NOT EXISTS idx_candidates_qualification ON candidates(qualification);
CREATE INDEX IF NOT EXISTS idx_candidates_location ON candidates(location);

-- Insert default admin user (password: admin123)
-- Note: The password is hashed using BCrypt
INSERT INTO users (full_name, email, password, role)
VALUES (
    'System Admin',
    'admin@candidatesystem.com',
    '$2a$10$xqwFq7QC5JZ3h7YxNNYG0OYkD6V1QWZGjH4Y8qZ2K8X9Y0Z1Z2Z3Z', 
    'ROLE_ADMIN'
) ON CONFLICT (email) DO NOTHING;

-- Verify the setup
SELECT 'Database setup completed successfully!' AS status;
SELECT COUNT(*) AS admin_count FROM users WHERE role = 'ROLE_ADMIN';
