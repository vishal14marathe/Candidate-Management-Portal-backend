package com.project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
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

    // Constructors
    public Candidate() {}

    public Candidate(Long id, String fullName, Integer age, String qualification, String identityProofNumber, 
                    String location, String email, String mobileNumber, String occupationStatus, 
                    String resumePath, byte[] resumeData, String resumeFilename, String resumeContentType, 
                    byte[] idProofData, String idProofFilename, String idProofContentType, LocalDateTime createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.qualification = qualification;
        this.identityProofNumber = identityProofNumber;
        this.location = location;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.occupationStatus = occupationStatus;
        this.resumePath = resumePath;
        this.resumeData = resumeData;
        this.resumeFilename = resumeFilename;
        this.resumeContentType = resumeContentType;
        this.idProofData = idProofData;
        this.idProofFilename = idProofFilename;
        this.idProofContentType = idProofContentType;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getIdentityProofNumber() { return identityProofNumber; }
    public void setIdentityProofNumber(String identityProofNumber) { this.identityProofNumber = identityProofNumber; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getOccupationStatus() { return occupationStatus; }
    public void setOccupationStatus(String occupationStatus) { this.occupationStatus = occupationStatus; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public byte[] getResumeData() { return resumeData; }
    public void setResumeData(byte[] resumeData) { this.resumeData = resumeData; }

    public String getResumeFilename() { return resumeFilename; }
    public void setResumeFilename(String resumeFilename) { this.resumeFilename = resumeFilename; }

    public String getResumeContentType() { return resumeContentType; }
    public void setResumeContentType(String resumeContentType) { this.resumeContentType = resumeContentType; }

    public byte[] getIdProofData() { return idProofData; }
    public void setIdProofData(byte[] idProofData) { this.idProofData = idProofData; }

    public String getIdProofFilename() { return idProofFilename; }
    public void setIdProofFilename(String idProofFilename) { this.idProofFilename = idProofFilename; }

    public String getIdProofContentType() { return idProofContentType; }
    public void setIdProofContentType(String idProofContentType) { this.idProofContentType = idProofContentType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}