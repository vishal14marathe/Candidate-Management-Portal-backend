package com.project.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class CandidateDTO {
    private Long id;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than 100")
    private Integer age;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    private String identityProofNumber;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Occupation status is required")
    private String occupationStatus;

    private String resumePath;
    private String resumeBase64;
    private String resumeFilename;
    private String resumeContentType;
    private boolean hasResume;
    private String idProofBase64;
    private String idProofFilename;
    private String idProofContentType;
    private boolean hasIdProof;
    private LocalDateTime createdAt;

    // Constructors
    public CandidateDTO() {}

    public CandidateDTO(Long id, String fullName, Integer age, String qualification, String identityProofNumber, 
                       String location, String email, String mobileNumber, String occupationStatus, 
                       String resumePath, String resumeBase64, String resumeFilename, String resumeContentType, 
                       boolean hasResume, String idProofBase64, String idProofFilename, String idProofContentType, 
                       boolean hasIdProof, LocalDateTime createdAt) {
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
        this.resumeBase64 = resumeBase64;
        this.resumeFilename = resumeFilename;
        this.resumeContentType = resumeContentType;
        this.hasResume = hasResume;
        this.idProofBase64 = idProofBase64;
        this.idProofFilename = idProofFilename;
        this.idProofContentType = idProofContentType;
        this.hasIdProof = hasIdProof;
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

    public String getResumeBase64() { return resumeBase64; }
    public void setResumeBase64(String resumeBase64) { this.resumeBase64 = resumeBase64; }

    public String getResumeFilename() { return resumeFilename; }
    public void setResumeFilename(String resumeFilename) { this.resumeFilename = resumeFilename; }

    public String getResumeContentType() { return resumeContentType; }
    public void setResumeContentType(String resumeContentType) { this.resumeContentType = resumeContentType; }

    public boolean isHasResume() { return hasResume; }
    public void setHasResume(boolean hasResume) { this.hasResume = hasResume; }

    public String getIdProofBase64() { return idProofBase64; }
    public void setIdProofBase64(String idProofBase64) { this.idProofBase64 = idProofBase64; }

    public String getIdProofFilename() { return idProofFilename; }
    public void setIdProofFilename(String idProofFilename) { this.idProofFilename = idProofFilename; }

    public String getIdProofContentType() { return idProofContentType; }
    public void setIdProofContentType(String idProofContentType) { this.idProofContentType = idProofContentType; }

    public boolean isHasIdProof() { return hasIdProof; }
    public void setHasIdProof(boolean hasIdProof) { this.hasIdProof = hasIdProof; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}