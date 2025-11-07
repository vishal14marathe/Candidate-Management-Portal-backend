package com.project.service;

import com.project.config.JwtTokenProvider;
import com.project.dto.AuthResponse;
import com.project.dto.LoginRequest;
import com.project.dto.RegisterRequest;
import com.project.model.Candidate;
import com.project.model.User;
import com.project.repository.CandidateRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create user account
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_USER);
        
        userRepository.save(user);
        
        // Create candidate profile with all details
        Candidate candidate = new Candidate();
        candidate.setFullName(request.getFullName());
        candidate.setAge(request.getAge());
        candidate.setEmail(request.getEmail());
        candidate.setMobileNumber(request.getMobileNumber());
        candidate.setQualification(request.getQualification());
        candidate.setLocation(request.getLocation());
        candidate.setOccupationStatus(request.getOccupationStatus());
        candidate.setIdentityProofNumber("ID" + System.currentTimeMillis());
        
        // Set file paths if provided (for backward compatibility)
        if (request.getResumePath() != null && !request.getResumePath().isEmpty()) {
            candidate.setResumePath(request.getResumePath());
        }
        
        // Store resume as byte array if base64 data provided
        if (request.getResumeBase64() != null && !request.getResumeBase64().isEmpty()) {
            try {
                // Decode base64 to byte array
                byte[] resumeBytes = Base64.getDecoder().decode(request.getResumeBase64());
                candidate.setResumeData(resumeBytes);
                candidate.setResumeFilename(request.getResumeFilename());
                candidate.setResumeContentType(request.getResumeContentType());
            } catch (Exception e) {
                throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
            }
        }
        
        // Store ID proof as byte array if base64 data provided
        if (request.getIdProofBase64() != null && !request.getIdProofBase64().isEmpty()) {
            try {
                // Decode base64 to byte array
                byte[] idProofBytes = Base64.getDecoder().decode(request.getIdProofBase64());
                candidate.setIdProofData(idProofBytes);
                candidate.setIdProofFilename(request.getIdProofFilename());
                candidate.setIdProofContentType(request.getIdProofContentType());
            } catch (Exception e) {
                throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
            }
        }
        
        candidateRepository.save(candidate);
        
        return new AuthResponse(null, user.getEmail(), user.getFullName(), 
                user.getRole().name(), "Registration successful");
    }
    
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new AuthResponse(jwt, user.getEmail(), user.getFullName(), 
                user.getRole().name(), "Login successful");
    }
}
