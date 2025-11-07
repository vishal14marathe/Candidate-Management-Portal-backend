package com.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    // Base64 encoded file data for registration
    private String resumeBase64;
    private String resumeFilename;
    private String resumeContentType;

    private String idProofBase64;
    private String idProofFilename;
    private String idProofContentType;
}
