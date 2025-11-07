package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullName;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    private String qualification;
    
    @Column(name = "identity_proof_number")
    private String identityProofNumber;
    
    @Column(nullable = false)
    private String location;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String mobileNumber;
    
    @Column(nullable = false)
    private String occupationStatus;
    
    private String resumePath;
    
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "resume_data", columnDefinition = "BYTEA")
    private byte[] resumeData;
    
    @Column(name = "resume_filename")
    private String resumeFilename;
    
    @Column(name = "resume_content_type")
    private String resumeContentType;
    
    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id_proof_data", columnDefinition = "BYTEA")
    private byte[] idProofData;
    
    @Column(name = "id_proof_filename")
    private String idProofFilename;
    
    @Column(name = "id_proof_content_type")
    private String idProofContentType;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
