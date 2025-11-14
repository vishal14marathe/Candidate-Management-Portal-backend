package com.project.controller;

import com.project.dto.CandidateDTO;
import com.project.model.Candidate;
import com.project.repository.CandidateRepository;
import com.project.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private CandidateService candidateService;
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    /**
     * Download candidate resume
     */
    @GetMapping("/candidates/{id}/resume")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        
        if (candidate.getResumeData() == null || candidate.getResumeData().length == 0) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(candidate.getResumeContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + candidate.getResumeFilename() + "\"")
                .body(candidate.getResumeData());
    }
    
    /**
     * Download candidate ID proof
     */
    @GetMapping("/candidates/{id}/id-proof")
    public ResponseEntity<byte[]> downloadIdProof(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        
        if (candidate.getIdProofData() == null || candidate.getIdProofData().length == 0) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(candidate.getIdProofContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + candidate.getIdProofFilename() + "\"")
                .body(candidate.getIdProofData());
    }
    
    /**
     * View candidate details with files
     */
    @GetMapping("/candidates/{id}/details")
    public ResponseEntity<CandidateDTO> getCandidateDetails(@PathVariable Long id) {
        CandidateDTO candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }
}
