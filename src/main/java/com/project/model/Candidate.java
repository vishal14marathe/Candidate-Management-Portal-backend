package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    @Column(nullable = false, unique = true)
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
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
