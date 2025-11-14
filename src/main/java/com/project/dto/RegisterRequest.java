package com.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Occupation status is required")
    private String occupationStatus;

    private String resumePath;
    private String idProofPath;
    private String resumeBase64;
    private String resumeFilename;
    private String resumeContentType;
    private String idProofBase64;
    private String idProofFilename;
    private String idProofContentType;

    // Constructors
    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, Integer age, String email, String password,
            String mobileNumber, String qualification, String location,
            String occupationStatus, String resumePath, String idProofPath,
            String resumeBase64, String resumeFilename, String resumeContentType,
            String idProofBase64, String idProofFilename, String idProofContentType) {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.qualification = qualification;
        this.location = location;
        this.occupationStatus = occupationStatus;
        this.resumePath = resumePath;
        this.idProofPath = idProofPath;
        this.resumeBase64 = resumeBase64;
        this.resumeFilename = resumeFilename;
        this.resumeContentType = resumeContentType;
        this.idProofBase64 = idProofBase64;
        this.idProofFilename = idProofFilename;
        this.idProofContentType = idProofContentType;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOccupationStatus() {
        return occupationStatus;
    }

    public void setOccupationStatus(String occupationStatus) {
        this.occupationStatus = occupationStatus;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public String getIdProofPath() {
        return idProofPath;
    }

    public void setIdProofPath(String idProofPath) {
        this.idProofPath = idProofPath;
    }

    public String getResumeBase64() {
        return resumeBase64;
    }

    public void setResumeBase64(String resumeBase64) {
        this.resumeBase64 = resumeBase64;
    }

    public String getResumeFilename() {
        return resumeFilename;
    }

    public void setResumeFilename(String resumeFilename) {
        this.resumeFilename = resumeFilename;
    }

    public String getResumeContentType() {
        return resumeContentType;
    }

    public void setResumeContentType(String resumeContentType) {
        this.resumeContentType = resumeContentType;
    }

    public String getIdProofBase64() {
        return idProofBase64;
    }

    public void setIdProofBase64(String idProofBase64) {
        this.idProofBase64 = idProofBase64;
    }

    public String getIdProofFilename() {
        return idProofFilename;
    }

    public void setIdProofFilename(String idProofFilename) {
        this.idProofFilename = idProofFilename;
    }

    public String getIdProofContentType() {
        return idProofContentType;
    }

    public void setIdProofContentType(String idProofContentType) {
        this.idProofContentType = idProofContentType;
    }
}