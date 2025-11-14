// package com.project.service;

// import com.project.dto.CandidateDTO;
// import com.project.model.Candidate;
// import com.project.repository.CandidateRepository;
// import com.project.repository.UserRepository;
// import jakarta.persistence.criteria.Predicate;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.ArrayList;
// import java.util.Base64;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class CandidateService {

//     @Autowired
//     private CandidateRepository candidateRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     @Autowired
//     private UserRepository userRepository;

//     public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
//         if (candidateRepository.existsByEmail(candidateDTO.getEmail())) {
//             throw new RuntimeException("Email already exists");
//         }
//         if (candidateRepository.existsByIdentityProofNumber(candidateDTO.getIdentityProofNumber())) {
//             throw new RuntimeException("Identity proof number already exists");
//         }

//         Candidate candidate = modelMapper.map(candidateDTO, Candidate.class);
//         Candidate savedCandidate = candidateRepository.save(candidate);
//         return modelMapper.map(savedCandidate, CandidateDTO.class);
//     }

//     public List<CandidateDTO> getAllCandidates(String qualification, String occupationStatus, String location) {
//         Specification<Candidate> spec = (root, query, criteriaBuilder) -> {
//             List<Predicate> predicates = new ArrayList<>();

//             if (qualification != null && !qualification.isEmpty()) {
//                 predicates.add(criteriaBuilder.equal(root.get("qualification"), qualification));
//             }
//             if (occupationStatus != null && !occupationStatus.isEmpty()) {
//                 predicates.add(criteriaBuilder.equal(root.get("occupationStatus"), occupationStatus));
//             }
//             if (location != null && !location.isEmpty()) {
//                 predicates.add(criteriaBuilder.equal(root.get("location"), location));
//             }

//             return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//         };

//         List<Candidate> candidates = candidateRepository.findAll(spec);
//         return candidates.stream()
//                 .map(this::convertToDTO)
//                 .collect(Collectors.toList());
//     }

//     public CandidateDTO getCandidateById(Long id) {
//         Candidate candidate = candidateRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
//         return convertToDTO(candidate);
//     }

//     public CandidateDTO getCandidateByEmail(String email) {
//         Candidate candidate = candidateRepository.findByEmail(email)
//                 .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email));
//         return convertToDTO(candidate);
//     }

//     // Convert Candidate entity to DTO with file data
//     private CandidateDTO convertToDTO(Candidate candidate) {
//         CandidateDTO dto = modelMapper.map(candidate, CandidateDTO.class);

//         // Convert resume bytes to Base64 for frontend
//         if (candidate.getResumeData() != null && candidate.getResumeData().length > 0) {
//             dto.setResumeBase64(Base64.getEncoder().encodeToString(candidate.getResumeData()));
//             dto.setResumeFilename(candidate.getResumeFilename());
//             dto.setResumeContentType(candidate.getResumeContentType());
//             dto.setHasResume(true);
//         } else {
//             dto.setHasResume(false);
//         }

//         // Convert ID proof bytes to Base64 for frontend
//         if (candidate.getIdProofData() != null && candidate.getIdProofData().length > 0) {
//             dto.setIdProofBase64(Base64.getEncoder().encodeToString(candidate.getIdProofData()));
//             dto.setIdProofFilename(candidate.getIdProofFilename());
//             dto.setIdProofContentType(candidate.getIdProofContentType());
//             dto.setHasIdProof(true);
//         } else {
//             dto.setHasIdProof(false);
//         }

//         return dto;
//     }

//     public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
//         Candidate candidate = candidateRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

//         if (!candidate.getEmail().equals(candidateDTO.getEmail()) &&
//                 candidateRepository.existsByEmail(candidateDTO.getEmail())) {
//             throw new RuntimeException("Email already exists");
//         }

//         // Validate mobile number (additional check beyond @Pattern annotation)
//         if (candidateDTO.getMobileNumber() != null) {
//             String mobile = candidateDTO.getMobileNumber().trim();
//             if (!mobile.matches("^[0-9]{10}$")) {
//                 throw new RuntimeException("Mobile number must be exactly 10 digits");
//             }
//         }

//         candidate.setFullName(candidateDTO.getFullName());
//         candidate.setAge(candidateDTO.getAge());
//         candidate.setQualification(candidateDTO.getQualification());
//         candidate.setLocation(candidateDTO.getLocation());
//         candidate.setEmail(candidateDTO.getEmail());
//         candidate.setMobileNumber(candidateDTO.getMobileNumber());
//         candidate.setOccupationStatus(candidateDTO.getOccupationStatus());

//         // Update resume if provided
//         if (candidateDTO.getResumeBase64() != null && !candidateDTO.getResumeBase64().isEmpty()) {
//             try {
//                 byte[] resumeBytes = Base64.getDecoder().decode(candidateDTO.getResumeBase64());
//                 candidate.setResumeData(resumeBytes);
//                 candidate.setResumeFilename(candidateDTO.getResumeFilename());
//                 candidate.setResumeContentType(candidateDTO.getResumeContentType());
//             } catch (Exception e) {
//                 throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
//             }
//         }

//         // Update ID proof if provided
//         if (candidateDTO.getIdProofBase64() != null && !candidateDTO.getIdProofBase64().isEmpty()) {
//             try {
//                 byte[] idProofBytes = Base64.getDecoder().decode(candidateDTO.getIdProofBase64());
//                 candidate.setIdProofData(idProofBytes);
//                 candidate.setIdProofFilename(candidateDTO.getIdProofFilename());
//                 candidate.setIdProofContentType(candidateDTO.getIdProofContentType());
//             } catch (Exception e) {
//                 throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
//             }
//         }

//         Candidate updatedCandidate = candidateRepository.save(candidate);
//         return convertToDTO(updatedCandidate);
//     }

//     @Transactional
//     public void deleteCandidate(Long id) {
//         Candidate candidate = candidateRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

//         // Delete associated user account by email
//         String email = candidate.getEmail();
//         candidateRepository.deleteById(id);

//         // Delete the user account if it exists
//         if (email != null && !email.isEmpty()) {
//             userRepository.deleteByEmail(email);
//         }
//     }
// }

// package com.project.service;

// import com.project.dto.CandidateDTO;
// import com.project.model.Candidate;
// import com.project.repository.CandidateRepository;
// import com.project.repository.UserRepository;
// import jakarta.persistence.criteria.Predicate;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.ArrayList;
// import java.util.Base64;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// public class CandidateService {

//     @Autowired
//     private CandidateRepository candidateRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     @Autowired
//     private UserRepository userRepository;

//     public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
//         try {
//             System.out.println("=== CREATING CANDIDATE ===");
//             System.out.println("Full Name: " + candidateDTO.getFullName());
//             System.out.println("Email: " + candidateDTO.getEmail());

//             // Check if email already exists
//             if (candidateRepository.existsByEmail(candidateDTO.getEmail())) {
//                 throw new RuntimeException("Email already exists: " + candidateDTO.getEmail());
//             }

//             // Generate identity proof number if not provided
//             if (candidateDTO.getIdentityProofNumber() == null || candidateDTO.getIdentityProofNumber().isEmpty()) {
//                 String identityProofNumber = "ID" + System.currentTimeMillis();
//                 candidateDTO.setIdentityProofNumber(identityProofNumber);
//                 System.out.println("Generated Identity Proof: " + identityProofNumber);
//             }

//             // Map DTO to Entity
//             Candidate candidate = modelMapper.map(candidateDTO, Candidate.class);

//             // Handle optional file uploads - set to null if not provided
//             if (candidateDTO.getResumeBase64() == null || candidateDTO.getResumeBase64().isEmpty()) {
//                 candidate.setResumeData(null);
//                 candidate.setResumeFilename(null);
//                 candidate.setResumeContentType(null);
//             } else {
//                 // Decode Base64 resume data
//                 try {
//                     byte[] resumeBytes = Base64.getDecoder().decode(candidateDTO.getResumeBase64());
//                     candidate.setResumeData(resumeBytes);
//                     candidate.setResumeFilename(candidateDTO.getResumeFilename());
//                     candidate.setResumeContentType(candidateDTO.getResumeContentType());
//                 } catch (Exception e) {
//                     throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
//                 }
//             }

//             if (candidateDTO.getIdProofBase64() == null || candidateDTO.getIdProofBase64().isEmpty()) {
//                 candidate.setIdProofData(null);
//                 candidate.setIdProofFilename(null);
//                 candidate.setIdProofContentType(null);
//             } else {
//                 // Decode Base64 ID proof data
//                 try {
//                     byte[] idProofBytes = Base64.getDecoder().decode(candidateDTO.getIdProofBase64());
//                     candidate.setIdProofData(idProofBytes);
//                     candidate.setIdProofFilename(candidateDTO.getIdProofFilename());
//                     candidate.setIdProofContentType(candidateDTO.getIdProofContentType());
//                 } catch (Exception e) {
//                     throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
//                 }
//             }

//             Candidate savedCandidate = candidateRepository.save(candidate);
//             System.out.println("Candidate created successfully with ID: " + savedCandidate.getId());

//             return convertToDTO(savedCandidate);

//         } catch (Exception e) {
//             System.err.println("Error creating candidate: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to create candidate: " + e.getMessage());
//         }
//     }

//     public List<CandidateDTO> getAllCandidates(String qualification, String occupationStatus, String location) {
//         try {
//             System.out.println("=== GETTING ALL CANDIDATES ===");
//             System.out.println("Qualification filter: " + qualification);
//             System.out.println("Occupation Status filter: " + occupationStatus);
//             System.out.println("Location filter: " + location);

//             List<Candidate> candidates;

//             // Use simple repository methods instead of Specification for now
//             if (qualification != null && !qualification.isEmpty() &&
//                     occupationStatus != null && !occupationStatus.isEmpty() &&
//                     location != null && !location.isEmpty()) {
//                 candidates = candidateRepository.findByQualificationAndOccupationStatusAndLocation(
//                         qualification, occupationStatus, location);
//             } else if (qualification != null && !qualification.isEmpty() &&
//                     occupationStatus != null && !occupationStatus.isEmpty()) {
//                 candidates = candidateRepository.findByQualificationAndOccupationStatus(qualification,
//                         occupationStatus);
//             } else if (qualification != null && !qualification.isEmpty() &&
//                     location != null && !location.isEmpty()) {
//                 candidates = candidateRepository.findByQualificationAndLocation(qualification, location);
//             } else if (occupationStatus != null && !occupationStatus.isEmpty() &&
//                     location != null && !location.isEmpty()) {
//                 candidates = candidateRepository.findByOccupationStatusAndLocation(occupationStatus, location);
//             } else if (qualification != null && !qualification.isEmpty()) {
//                 candidates = candidateRepository.findByQualification(qualification);
//             } else if (occupationStatus != null && !occupationStatus.isEmpty()) {
//                 candidates = candidateRepository.findByOccupationStatus(occupationStatus);
//             } else if (location != null && !location.isEmpty()) {
//                 candidates = candidateRepository.findByLocation(location);
//             } else {
//                 candidates = candidateRepository.findAll();
//             }

//             System.out.println("Found " + candidates.size() + " candidates");

//             return candidates.stream()
//                     .map(this::convertToDTO)
//                     .collect(Collectors.toList());

//         } catch (Exception e) {
//             System.err.println("Error in getAllCandidates: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to retrieve candidates: " + e.getMessage());
//         }
//     }

//     public CandidateDTO getCandidateById(Long id) {
//         try {
//             Candidate candidate = candidateRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
//             return convertToDTO(candidate);
//         } catch (Exception e) {
//             System.err.println("Error in getCandidateById: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to retrieve candidate: " + e.getMessage());
//         }
//     }

//     public CandidateDTO getCandidateByEmail(String email) {
//         try {
//             Candidate candidate = candidateRepository.findByEmail(email)
//                     .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email));
//             return convertToDTO(candidate);
//         } catch (Exception e) {
//             System.err.println("Error in getCandidateByEmail: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to retrieve candidate: " + e.getMessage());
//         }
//     }

//     // Convert Candidate entity to DTO with file data
//     private CandidateDTO convertToDTO(Candidate candidate) {
//         try {
//             CandidateDTO dto = new CandidateDTO();

//             // Map basic fields
//             dto.setId(candidate.getId());
//             dto.setFullName(candidate.getFullName());
//             dto.setAge(candidate.getAge());
//             dto.setQualification(candidate.getQualification());
//             dto.setIdentityProofNumber(candidate.getIdentityProofNumber());
//             dto.setLocation(candidate.getLocation());
//             dto.setEmail(candidate.getEmail());
//             dto.setMobileNumber(candidate.getMobileNumber());
//             dto.setOccupationStatus(candidate.getOccupationStatus());
//             dto.setCreatedAt(candidate.getCreatedAt());

//             // Convert resume bytes to Base64 for frontend
//             if (candidate.getResumeData() != null && candidate.getResumeData().length > 0) {
//                 dto.setResumeBase64(Base64.getEncoder().encodeToString(candidate.getResumeData()));
//                 dto.setResumeFilename(candidate.getResumeFilename());
//                 dto.setResumeContentType(candidate.getResumeContentType());
//                 dto.setHasResume(true);
//             } else {
//                 dto.setHasResume(false);
//                 dto.setResumeBase64(null);
//                 dto.setResumeFilename(null);
//                 dto.setResumeContentType(null);
//             }

//             // Convert ID proof bytes to Base64 for frontend
//             if (candidate.getIdProofData() != null && candidate.getIdProofData().length > 0) {
//                 dto.setIdProofBase64(Base64.getEncoder().encodeToString(candidate.getIdProofData()));
//                 dto.setIdProofFilename(candidate.getIdProofFilename());
//                 dto.setIdProofContentType(candidate.getIdProofContentType());
//                 dto.setHasIdProof(true);
//             } else {
//                 dto.setHasIdProof(false);
//                 dto.setIdProofBase64(null);
//                 dto.setIdProofFilename(null);
//                 dto.setIdProofContentType(null);
//             }

//             return dto;
//         } catch (Exception e) {
//             System.err.println("Error converting candidate to DTO: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to convert candidate data");
//         }
//     }

//     public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
//         try {
//             Candidate candidate = candidateRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

//             if (!candidate.getEmail().equals(candidateDTO.getEmail()) &&
//                     candidateRepository.existsByEmail(candidateDTO.getEmail())) {
//                 throw new RuntimeException("Email already exists");
//             }

//             // Validate mobile number (additional check beyond @Pattern annotation)
//             if (candidateDTO.getMobileNumber() != null) {
//                 String mobile = candidateDTO.getMobileNumber().trim();
//                 if (!mobile.matches("^[0-9]{10}$")) {
//                     throw new RuntimeException("Mobile number must be exactly 10 digits");
//                 }
//             }

//             candidate.setFullName(candidateDTO.getFullName());
//             candidate.setAge(candidateDTO.getAge());
//             candidate.setQualification(candidateDTO.getQualification());
//             candidate.setLocation(candidateDTO.getLocation());
//             candidate.setEmail(candidateDTO.getEmail());
//             candidate.setMobileNumber(candidateDTO.getMobileNumber());
//             candidate.setOccupationStatus(candidateDTO.getOccupationStatus());

//             // Update resume if provided (optional)
//             if (candidateDTO.getResumeBase64() != null && !candidateDTO.getResumeBase64().isEmpty()) {
//                 try {
//                     byte[] resumeBytes = Base64.getDecoder().decode(candidateDTO.getResumeBase64());
//                     candidate.setResumeData(resumeBytes);
//                     candidate.setResumeFilename(candidateDTO.getResumeFilename());
//                     candidate.setResumeContentType(candidateDTO.getResumeContentType());
//                 } catch (Exception e) {
//                     throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
//                 }
//             }

//             // Update ID proof if provided (optional)
//             if (candidateDTO.getIdProofBase64() != null && !candidateDTO.getIdProofBase64().isEmpty()) {
//                 try {
//                     byte[] idProofBytes = Base64.getDecoder().decode(candidateDTO.getIdProofBase64());
//                     candidate.setIdProofData(idProofBytes);
//                     candidate.setIdProofFilename(candidateDTO.getIdProofFilename());
//                     candidate.setIdProofContentType(candidateDTO.getIdProofContentType());
//                 } catch (Exception e) {
//                     throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
//                 }
//             }

//             Candidate updatedCandidate = candidateRepository.save(candidate);
//             return convertToDTO(updatedCandidate);
//         } catch (Exception e) {
//             System.err.println("Error in updateCandidate: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to update candidate: " + e.getMessage());
//         }
//     }

//     @Transactional
//     public void deleteCandidate(Long id) {
//         try {
//             Candidate candidate = candidateRepository.findById(id)
//                     .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

//             // Delete associated user account by email
//             String email = candidate.getEmail();
//             candidateRepository.deleteById(id);

//             // Delete the user account if it exists
//             if (email != null && !email.isEmpty()) {
//                 userRepository.deleteByEmail(email);
//             }
//         } catch (Exception e) {
//             System.err.println("Error in deleteCandidate: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to delete candidate: " + e.getMessage());
//         }
//     }

//     public boolean checkEmailExists(String email) {
//         try {
//             return candidateRepository.existsByEmail(email);
//         } catch (Exception e) {
//             System.err.println("Error in checkEmailExists: " + e.getMessage());
//             e.printStackTrace();
//             throw new RuntimeException("Failed to check email existence: " + e.getMessage());
//         }
//     }
// }

package com.project.service;

import com.project.dto.CandidateDTO;
import com.project.model.Candidate;
import com.project.repository.CandidateRepository;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        try {
            System.out.println("=== CREATING CANDIDATE ===");
            System.out.println("Full Name: " + candidateDTO.getFullName());
            System.out.println("Email: " + candidateDTO.getEmail());

            // Check if email already exists
            if (candidateRepository.existsByEmail(candidateDTO.getEmail())) {
                throw new RuntimeException("Email already exists: " + candidateDTO.getEmail());
            }

            // Generate identity proof number if not provided
            if (candidateDTO.getIdentityProofNumber() == null || candidateDTO.getIdentityProofNumber().isEmpty()) {
                String identityProofNumber = "ID" + System.currentTimeMillis();
                candidateDTO.setIdentityProofNumber(identityProofNumber);
                System.out.println("Generated Identity Proof: " + identityProofNumber);
            }

            // Create candidate entity
            Candidate candidate = new Candidate();
            candidate.setFullName(candidateDTO.getFullName());
            candidate.setAge(candidateDTO.getAge());
            candidate.setQualification(candidateDTO.getQualification());
            candidate.setIdentityProofNumber(candidateDTO.getIdentityProofNumber());
            candidate.setLocation(candidateDTO.getLocation());
            candidate.setEmail(candidateDTO.getEmail());
            candidate.setMobileNumber(candidateDTO.getMobileNumber());
            candidate.setOccupationStatus(candidateDTO.getOccupationStatus());

            // Handle optional file uploads
            if (candidateDTO.getResumeBase64() != null && !candidateDTO.getResumeBase64().isEmpty()) {
                try {
                    byte[] resumeBytes = Base64.getDecoder().decode(candidateDTO.getResumeBase64());
                    candidate.setResumeData(resumeBytes);
                    candidate.setResumeFilename(candidateDTO.getResumeFilename());
                    candidate.setResumeContentType(candidateDTO.getResumeContentType());
                    System.out.println("Resume file added: " + candidateDTO.getResumeFilename());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
                }
            }

            if (candidateDTO.getIdProofBase64() != null && !candidateDTO.getIdProofBase64().isEmpty()) {
                try {
                    byte[] idProofBytes = Base64.getDecoder().decode(candidateDTO.getIdProofBase64());
                    candidate.setIdProofData(idProofBytes);
                    candidate.setIdProofFilename(candidateDTO.getIdProofFilename());
                    candidate.setIdProofContentType(candidateDTO.getIdProofContentType());
                    System.out.println("ID Proof file added: " + candidateDTO.getIdProofFilename());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
                }
            }

            Candidate savedCandidate = candidateRepository.save(candidate);
            System.out.println("Candidate created successfully with ID: " + savedCandidate.getId());

            return convertToDTO(savedCandidate);

        } catch (Exception e) {
            System.err.println("Error creating candidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create candidate: " + e.getMessage());
        }
    }

    public List<CandidateDTO> getAllCandidates(String qualification, String occupationStatus, String location) {
        try {
            System.out.println("=== GETTING ALL CANDIDATES ===");
            System.out.println("Qualification filter: " + qualification);
            System.out.println("Occupation Status filter: " + occupationStatus);
            System.out.println("Location filter: " + location);

            List<Candidate> candidates;

            // Apply filters using repository methods
            if (qualification != null && !qualification.isEmpty() &&
                    occupationStatus != null && !occupationStatus.isEmpty() &&
                    location != null && !location.isEmpty()) {
                candidates = candidateRepository.findByQualificationAndOccupationStatusAndLocation(
                        qualification, occupationStatus, location);
            } else if (qualification != null && !qualification.isEmpty() &&
                    occupationStatus != null && !occupationStatus.isEmpty()) {
                candidates = candidateRepository.findByQualificationAndOccupationStatus(qualification,
                        occupationStatus);
            } else if (qualification != null && !qualification.isEmpty() &&
                    location != null && !location.isEmpty()) {
                candidates = candidateRepository.findByQualificationAndLocation(qualification, location);
            } else if (occupationStatus != null && !occupationStatus.isEmpty() &&
                    location != null && !location.isEmpty()) {
                candidates = candidateRepository.findByOccupationStatusAndLocation(occupationStatus, location);
            } else if (qualification != null && !qualification.isEmpty()) {
                candidates = candidateRepository.findByQualification(qualification);
            } else if (occupationStatus != null && !occupationStatus.isEmpty()) {
                candidates = candidateRepository.findByOccupationStatus(occupationStatus);
            } else if (location != null && !location.isEmpty()) {
                candidates = candidateRepository.findByLocation(location);
            } else {
                candidates = candidateRepository.findAll();
            }

            System.out.println("Found " + candidates.size() + " candidates");

            return candidates.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error in getAllCandidates: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve candidates: " + e.getMessage());
        }
    }

    public CandidateDTO getCandidateById(Long id) {
        try {
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));
            return convertToDTO(candidate);
        } catch (Exception e) {
            System.err.println("Error in getCandidateById: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve candidate: " + e.getMessage());
        }
    }

    public CandidateDTO getCandidateByEmail(String email) {
        try {
            Candidate candidate = candidateRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email));
            return convertToDTO(candidate);
        } catch (Exception e) {
            System.err.println("Error in getCandidateByEmail: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve candidate: " + e.getMessage());
        }
    }

    private CandidateDTO convertToDTO(Candidate candidate) {
        try {
            CandidateDTO dto = new CandidateDTO();
            dto.setId(candidate.getId());
            dto.setFullName(candidate.getFullName());
            dto.setAge(candidate.getAge());
            dto.setQualification(candidate.getQualification());
            dto.setIdentityProofNumber(candidate.getIdentityProofNumber());
            dto.setLocation(candidate.getLocation());
            dto.setEmail(candidate.getEmail());
            dto.setMobileNumber(candidate.getMobileNumber());
            dto.setOccupationStatus(candidate.getOccupationStatus());
            dto.setCreatedAt(candidate.getCreatedAt());

            // Handle resume file data
            if (candidate.getResumeData() != null && candidate.getResumeData().length > 0) {
                dto.setResumeBase64(Base64.getEncoder().encodeToString(candidate.getResumeData()));
                dto.setResumeFilename(candidate.getResumeFilename());
                dto.setResumeContentType(candidate.getResumeContentType());
                dto.setHasResume(true);
            } else {
                dto.setHasResume(false);
            }

            // Handle ID proof file data
            if (candidate.getIdProofData() != null && candidate.getIdProofData().length > 0) {
                dto.setIdProofBase64(Base64.getEncoder().encodeToString(candidate.getIdProofData()));
                dto.setIdProofFilename(candidate.getIdProofFilename());
                dto.setIdProofContentType(candidate.getIdProofContentType());
                dto.setHasIdProof(true);
            } else {
                dto.setHasIdProof(false);
            }

            return dto;
        } catch (Exception e) {
            System.err.println("Error converting candidate to DTO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to convert candidate data");
        }
    }

    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        try {
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

            // Check email uniqueness if email is being changed
            if (!candidate.getEmail().equals(candidateDTO.getEmail()) &&
                    candidateRepository.existsByEmail(candidateDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            // Validate mobile number
            if (candidateDTO.getMobileNumber() != null) {
                String mobile = candidateDTO.getMobileNumber().trim();
                if (!mobile.matches("^[0-9]{10}$")) {
                    throw new RuntimeException("Mobile number must be exactly 10 digits");
                }
            }

            // Update basic fields
            candidate.setFullName(candidateDTO.getFullName());
            candidate.setAge(candidateDTO.getAge());
            candidate.setQualification(candidateDTO.getQualification());
            candidate.setLocation(candidateDTO.getLocation());
            candidate.setEmail(candidateDTO.getEmail());
            candidate.setMobileNumber(candidateDTO.getMobileNumber());
            candidate.setOccupationStatus(candidateDTO.getOccupationStatus());

            // Update resume if provided
            if (candidateDTO.getResumeBase64() != null && !candidateDTO.getResumeBase64().isEmpty()) {
                try {
                    byte[] resumeBytes = Base64.getDecoder().decode(candidateDTO.getResumeBase64());
                    candidate.setResumeData(resumeBytes);
                    candidate.setResumeFilename(candidateDTO.getResumeFilename());
                    candidate.setResumeContentType(candidateDTO.getResumeContentType());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode resume file: " + e.getMessage());
                }
            }

            // Update ID proof if provided
            if (candidateDTO.getIdProofBase64() != null && !candidateDTO.getIdProofBase64().isEmpty()) {
                try {
                    byte[] idProofBytes = Base64.getDecoder().decode(candidateDTO.getIdProofBase64());
                    candidate.setIdProofData(idProofBytes);
                    candidate.setIdProofFilename(candidateDTO.getIdProofFilename());
                    candidate.setIdProofContentType(candidateDTO.getIdProofContentType());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode ID proof file: " + e.getMessage());
                }
            }

            Candidate updatedCandidate = candidateRepository.save(candidate);
            return convertToDTO(updatedCandidate);
        } catch (Exception e) {
            System.err.println("Error in updateCandidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update candidate: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCandidate(Long id) {
        try {
            System.out.println("Attempting to delete candidate with id: " + id);
            
            Candidate candidate = candidateRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + id));

            // Store email before deletion
            String email = candidate.getEmail();
            System.out.println("Deleting candidate: " + email);

            // First delete the candidate record
            candidateRepository.deleteById(id);
            candidateRepository.flush(); // Force the delete to execute immediately

            // Then delete the associated user account if it exists
            if (email != null && !email.isEmpty()) {
                System.out.println("Deleting associated user account: " + email);
                userRepository.deleteByEmail(email);
                userRepository.flush(); // Force the delete to execute immediately
            }
            
            System.out.println("Successfully deleted candidate and user account for: " + email);
        } catch (Exception e) {
            System.err.println("Error in deleteCandidate: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete candidate: " + e.getMessage());
        }
    }

    public boolean checkEmailExists(String email) {
        try {
            return candidateRepository.existsByEmail(email);
        } catch (Exception e) {
            System.err.println("Error in checkEmailExists: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to check email existence: " + e.getMessage());
        }
    }
}