// package com.project.controller;

// import com.project.dto.CandidateDTO;
// import com.project.service.CandidateService;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api")
// public class CandidateController {

//     @Autowired
//     private CandidateService candidateService;

//     // ============================================
//     // CANDIDATE ENDPOINTS (For logged-in candidates)
//     // ============================================

//     @GetMapping("/candidates/profile")
//     @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
//     public ResponseEntity<CandidateDTO> getCurrentCandidateProfile(Authentication authentication) {
//         String email = authentication.getName();
//         CandidateDTO candidate = candidateService.getCandidateByEmail(email);
//         return ResponseEntity.ok(candidate);
//     }

//     @PutMapping("/candidates/profile")
//     @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
//     public ResponseEntity<CandidateDTO> updateCurrentCandidateProfile(
//             Authentication authentication,
//             @Valid @RequestBody CandidateDTO candidateDTO) {
//         String email = authentication.getName();
//         CandidateDTO candidate = candidateService.getCandidateByEmail(email);
//         CandidateDTO updated = candidateService.updateCandidate(candidate.getId(), candidateDTO);
//         return ResponseEntity.ok(updated);
//     }

//     // ============================================
//     // ADMIN ENDPOINTS
//     // ============================================

//     @PostMapping("/admin/candidates")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<CandidateDTO> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
//         CandidateDTO created = candidateService.createCandidate(candidateDTO);
//         return ResponseEntity.ok(created);
//     }

//     @GetMapping("/admin/candidates")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> getAllCandidates(
//             @RequestParam(required = false) String qualification,
//             @RequestParam(required = false) String occupationStatus,
//             @RequestParam(required = false) String location,
//             @RequestParam(required = false) String name,
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size) {

//         List<CandidateDTO> allCandidates = candidateService.getAllCandidates(qualification, occupationStatus, location);

//         // Filter by name if provided
//         if (name != null && !name.isEmpty()) {
//             allCandidates = allCandidates.stream()
//                     .filter(c -> c.getFullName() != null &&
//                             c.getFullName().toLowerCase().contains(name.toLowerCase()))
//                     .collect(java.util.stream.Collectors.toList());
//         }

//         // Apply pagination
//         int totalElements = allCandidates.size();
//         int totalPages = (int) Math.ceil((double) totalElements / size);
//         int startIndex = page * size;
//         int endIndex = Math.min(startIndex + size, totalElements);

//         List<CandidateDTO> paginatedCandidates = allCandidates.subList(
//                 Math.min(startIndex, totalElements),
//                 Math.min(endIndex, totalElements));

//         // Create response with pagination info
//         java.util.Map<String, Object> response = new java.util.HashMap<>();
//         response.put("content", paginatedCandidates);
//         response.put("totalElements", totalElements);
//         response.put("totalPages", totalPages);
//         response.put("currentPage", page);
//         response.put("pageSize", size);

//         return ResponseEntity.ok(response);
//     }

//     @GetMapping("/admin/candidates/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
//         CandidateDTO candidate = candidateService.getCandidateById(id);
//         return ResponseEntity.ok(candidate);
//     }

//     @PutMapping("/candidates/{id}")
//     public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id,
//             @Valid @RequestBody CandidateDTO candidateDTO) {
//         CandidateDTO updated = candidateService.updateCandidate(id, candidateDTO);
//         return ResponseEntity.ok(updated);
//     }

//     @DeleteMapping("/admin/candidates/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
//         candidateService.deleteCandidate(id);
//         return ResponseEntity.ok("Candidate deleted successfully");
//     }
// }

// // package com.project.controller;

// // import com.project.dto.CandidateDTO;
// // import com.project.service.CandidateService;
// // import jakarta.validation.Valid;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.http.ResponseEntity;
// // import org.springframework.security.access.prepost.PreAuthorize;
// // import org.springframework.security.core.Authentication;
// // import org.springframework.web.bind.annotation.*;

// // import java.util.List;

// // @RestController
// // @RequestMapping("/api")
// // public class CandidateController {

// // @Autowired
// // private CandidateService candidateService;

// // // ============================================
// // // CANDIDATE ENDPOINTS (For logged-in candidates)
// // // ============================================

// // @GetMapping("/candidates/profile")
// // @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
// // public ResponseEntity<CandidateDTO> getCurrentCandidateProfile(Authentication
// // authentication) {
// // String email = authentication.getName();
// // CandidateDTO candidate = candidateService.getCandidateByEmail(email);
// // return ResponseEntity.ok(candidate);
// // }

// // @PutMapping("/candidates/profile")
// // @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
// // public ResponseEntity<CandidateDTO> updateCurrentCandidateProfile(
// // Authentication authentication,
// // @Valid @RequestBody CandidateDTO candidateDTO) {
// // String email = authentication.getName();
// // CandidateDTO candidate = candidateService.getCandidateByEmail(email);
// // CandidateDTO updated = candidateService.updateCandidate(candidate.getId(),
// // candidateDTO);
// // return ResponseEntity.ok(updated);
// // }

// // // ============================================
// // // ADMIN ENDPOINTS
// // // ============================================

// // @PostMapping("/admin/candidates")
// // @PreAuthorize("hasRole('ADMIN')")
// // public ResponseEntity<CandidateDTO> createCandidate(@Valid @RequestBody
// // CandidateDTO candidateDTO) {
// // try {
// // System.out.println("Creating candidate: " + candidateDTO);
// // CandidateDTO created = candidateService.createCandidate(candidateDTO);
// // return ResponseEntity.ok(created);
// // } catch (Exception e) {
// // System.err.println("Error creating candidate: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).build();
// // }
// // }

// // @GetMapping("/admin/candidates")
// // @PreAuthorize("hasRole('ADMIN')")
// // public ResponseEntity<?> getAllCandidates(
// // @RequestParam(required = false) String qualification,
// // @RequestParam(required = false) String occupationStatus,
// // @RequestParam(required = false) String location,
// // @RequestParam(required = false) String name,
// // @RequestParam(defaultValue = "0") int page,
// // @RequestParam(defaultValue = "10") int size) {

// // try {
// // List<CandidateDTO> allCandidates =
// // candidateService.getAllCandidates(qualification, occupationStatus,
// // location);

// // // Filter by name if provided
// // if (name != null && !name.isEmpty()) {
// // allCandidates = allCandidates.stream()
// // .filter(c -> c.getFullName() != null &&
// // c.getFullName().toLowerCase().contains(name.toLowerCase()))
// // .collect(java.util.stream.Collectors.toList());
// // }

// // // Apply pagination
// // int totalElements = allCandidates.size();
// // int totalPages = (int) Math.ceil((double) totalElements / size);
// // int startIndex = page * size;
// // int endIndex = Math.min(startIndex + size, totalElements);

// // List<CandidateDTO> paginatedCandidates = allCandidates.subList(
// // Math.min(startIndex, totalElements),
// // Math.min(endIndex, totalElements));

// // // Create response with pagination info
// // java.util.Map<String, Object> response = new java.util.HashMap<>();
// // response.put("content", paginatedCandidates);
// // response.put("totalElements", totalElements);
// // response.put("totalPages", totalPages);
// // response.put("currentPage", page);
// // response.put("pageSize", size);

// // return ResponseEntity.ok(response);
// // } catch (Exception e) {
// // System.err.println("Error getting candidates: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).build();
// // }
// // }

// // @GetMapping("/admin/candidates/{id}")
// // @PreAuthorize("hasRole('ADMIN')")
// // public ResponseEntity<CandidateDTO> getCandidateById(@PathVariable Long id) {
// // try {
// // CandidateDTO candidate = candidateService.getCandidateById(id);
// // return ResponseEntity.ok(candidate);
// // } catch (Exception e) {
// // System.err.println("Error getting candidate by ID: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).build();
// // }
// // }

// // @PutMapping("/candidates/{id}")
// // @PreAuthorize("hasRole('ADMIN')") // Added admin authorization
// // public ResponseEntity<CandidateDTO> updateCandidate(@PathVariable Long id,
// // @Valid @RequestBody CandidateDTO candidateDTO) {
// // try {
// // System.out.println("Updating candidate ID: " + id);
// // System.out.println("Received data: " + candidateDTO);

// // CandidateDTO updated = candidateService.updateCandidate(id, candidateDTO);
// // return ResponseEntity.ok(updated);
// // } catch (Exception e) {
// // System.err.println("Error updating candidate: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).build();
// // }
// // }

// // @DeleteMapping("/admin/candidates/{id}")
// // @PreAuthorize("hasRole('ADMIN')")
// // public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
// // try {
// // candidateService.deleteCandidate(id);
// // return ResponseEntity.ok("Candidate deleted successfully");
// // } catch (Exception e) {
// // System.err.println("Error deleting candidate: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).body("Error deleting candidate: " +
// // e.getMessage());
// // }
// // }

// // // ============================================
// // // ADDITIONAL UTILITY ENDPOINTS
// // // ============================================

// // @GetMapping("/admin/candidates/check-email")
// // @PreAuthorize("hasRole('ADMIN')")
// // public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
// // try {
// // boolean exists = candidateService.checkEmailExists(email);
// // java.util.Map<String, Object> response = new java.util.HashMap<>();
// // response.put("exists", exists);
// // return ResponseEntity.ok(response);
// // } catch (Exception e) {
// // System.err.println("Error checking email: " + e.getMessage());
// // e.printStackTrace();
// // return ResponseEntity.status(500).build();
// // }
// // }
// // }

// package com.project.controller;

// import com.project.dto.CandidateDTO;
// import com.project.service.CandidateService;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/api")
// public class CandidateController {

//     @Autowired
//     private CandidateService candidateService;

//     // ============================================
//     // CANDIDATE ENDPOINTS (For logged-in candidates)
//     // ============================================

//     @GetMapping("/candidates/profile")
//     @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
//     public ResponseEntity<?> getCurrentCandidateProfile(Authentication authentication) {
//         try {
//             String email = authentication.getName();
//             CandidateDTO candidate = candidateService.getCandidateByEmail(email);
//             return ResponseEntity.ok(candidate);
//         } catch (Exception e) {
//             System.err.println("Error getting candidate profile: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to get profile"));
//         }
//     }

//     @PutMapping("/candidates/profile")
//     @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
//     public ResponseEntity<?> updateCurrentCandidateProfile(
//             Authentication authentication,
//             @Valid @RequestBody CandidateDTO candidateDTO) {
//         try {
//             String email = authentication.getName();
//             CandidateDTO candidate = candidateService.getCandidateByEmail(email);
//             CandidateDTO updated = candidateService.updateCandidate(candidate.getId(), candidateDTO);
//             return ResponseEntity.ok(updated);
//         } catch (Exception e) {
//             System.err.println("Error updating candidate profile: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to update profile"));
//         }
//     }

//     // ============================================
//     // ADMIN ENDPOINTS
//     // ============================================

//     @PostMapping("/admin/candidates")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
//         try {
//             System.out.println("=== ADMIN CREATING CANDIDATE ===");
//             CandidateDTO created = candidateService.createCandidate(candidateDTO);
//             System.out.println("=== CANDIDATE CREATED SUCCESSFULLY ===");
//             return ResponseEntity.ok(created);
//         } catch (Exception e) {
//             System.err.println("Error creating candidate: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", e.getMessage()));
//         }
//     }

//     @GetMapping("/admin/candidates")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> getAllCandidates(
//             @RequestParam(required = false) String qualification,
//             @RequestParam(required = false) String occupationStatus,
//             @RequestParam(required = false) String location,
//             @RequestParam(required = false) String name,
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "10") int size) {

//         try {
//             System.out.println("=== GETTING ALL CANDIDATES FOR ADMIN ===");
//             List<CandidateDTO> allCandidates = candidateService.getAllCandidates(qualification, occupationStatus,
//                     location);

//             // Filter by name if provided
//             if (name != null && !name.isEmpty()) {
//                 allCandidates = allCandidates.stream()
//                         .filter(c -> c.getFullName() != null &&
//                                 c.getFullName().toLowerCase().contains(name.toLowerCase()))
//                         .collect(java.util.stream.Collectors.toList());
//             }

//             // Apply pagination
//             int totalElements = allCandidates.size();
//             int totalPages = (int) Math.ceil((double) totalElements / size);
//             int startIndex = page * size;
//             int endIndex = Math.min(startIndex + size, totalElements);

//             List<CandidateDTO> paginatedCandidates = allCandidates.subList(
//                     Math.min(startIndex, totalElements),
//                     Math.min(endIndex, totalElements));

//             // Create response with pagination info
//             java.util.Map<String, Object> response = new java.util.HashMap<>();
//             response.put("content", paginatedCandidates);
//             response.put("totalElements", totalElements);
//             response.put("totalPages", totalPages);
//             response.put("currentPage", page);
//             response.put("pageSize", size);

//             System.out.println("Returning " + paginatedCandidates.size() + " candidates");
//             return ResponseEntity.ok(response);
//         } catch (Exception e) {
//             System.err.println("Error getting candidates: " + e.getMessage());
//             e.printStackTrace();
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to retrieve candidates: " + e.getMessage()));
//         }
//     }

//     @GetMapping("/admin/candidates/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> getCandidateById(@PathVariable Long id) {
//         try {
//             CandidateDTO candidate = candidateService.getCandidateById(id);
//             return ResponseEntity.ok(candidate);
//         } catch (Exception e) {
//             System.err.println("Error getting candidate by ID: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Candidate not found"));
//         }
//     }

//     @PutMapping("/candidates/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> updateCandidate(@PathVariable Long id,
//             @Valid @RequestBody CandidateDTO candidateDTO) {
//         try {
//             System.out.println("Updating candidate ID: " + id);
//             CandidateDTO updated = candidateService.updateCandidate(id, candidateDTO);
//             return ResponseEntity.ok(updated);
//         } catch (Exception e) {
//             System.err.println("Error updating candidate: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to update candidate"));
//         }
//     }

//     @DeleteMapping("/admin/candidates/{id}")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> deleteCandidate(@PathVariable Long id) {
//         try {
//             candidateService.deleteCandidate(id);
//             return ResponseEntity.ok("Candidate deleted successfully");
//         } catch (Exception e) {
//             System.err.println("Error deleting candidate: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to delete candidate"));
//         }
//     }

//     // ============================================
//     // ADDITIONAL UTILITY ENDPOINTS
//     // ============================================

//     @GetMapping("/admin/candidates/check-email")
//     @PreAuthorize("hasRole('ADMIN')")
//     public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
//         try {
//             boolean exists = candidateService.checkEmailExists(email);
//             return ResponseEntity.ok(java.util.Map.of("exists", exists));
//         } catch (Exception e) {
//             System.err.println("Error checking email: " + e.getMessage());
//             return ResponseEntity.badRequest()
//                     .body(java.util.Map.of("error", "Failed to check email"));
//         }
//     }
// }
package com.project.controller;

import com.project.dto.CandidateDTO;
import com.project.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    // ============================================
    // CANDIDATE ENDPOINTS (For logged-in candidates)
    // ============================================

    @GetMapping("/candidates/profile")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
    public ResponseEntity<?> getCurrentCandidateProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            CandidateDTO candidate = candidateService.getCandidateByEmail(email);
            return ResponseEntity.ok(candidate);
        } catch (Exception e) {
            System.err.println("Error getting candidate profile: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to get profile: " + e.getMessage()));
        }
    }

    @PutMapping("/candidates/profile")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'USER')")
    public ResponseEntity<?> updateCurrentCandidateProfile(
            Authentication authentication,
            @Valid @RequestBody CandidateDTO candidateDTO) {
        try {
            String email = authentication.getName();
            CandidateDTO candidate = candidateService.getCandidateByEmail(email);
            CandidateDTO updated = candidateService.updateCandidate(candidate.getId(), candidateDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating candidate profile: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to update profile: " + e.getMessage()));
        }
    }

    // ============================================
    // ADMIN ENDPOINTS
    // ============================================

    @PostMapping("/admin/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCandidate(@Valid @RequestBody CandidateDTO candidateDTO) {
        try {
            System.out.println("=== ADMIN CREATING CANDIDATE ===");
            CandidateDTO created = candidateService.createCandidate(candidateDTO);
            System.out.println("=== CANDIDATE CREATED SUCCESSFULLY ===");
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            System.err.println("Error creating candidate: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/admin/candidates")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllCandidates(
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String occupationStatus,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            System.out.println("=== GETTING ALL CANDIDATES FOR ADMIN ===");
            List<CandidateDTO> allCandidates = candidateService.getAllCandidates(qualification, occupationStatus,
                    location);

            // Filter by name if provided
            if (name != null && !name.isEmpty()) {
                allCandidates = allCandidates.stream()
                        .filter(c -> c.getFullName() != null &&
                                c.getFullName().toLowerCase().contains(name.toLowerCase()))
                        .collect(java.util.stream.Collectors.toList());
            }

            // Apply pagination
            int totalElements = allCandidates.size();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalElements);

            List<CandidateDTO> paginatedCandidates = allCandidates.subList(
                    Math.min(startIndex, totalElements),
                    Math.min(endIndex, totalElements));

            // Create response with pagination info
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("content", paginatedCandidates);
            response.put("totalElements", totalElements);
            response.put("totalPages", totalPages);
            response.put("currentPage", page);
            response.put("pageSize", size);

            System.out.println("Returning " + paginatedCandidates.size() + " candidates");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error getting candidates: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to retrieve candidates: " + e.getMessage()));
        }
    }

    @GetMapping("/admin/candidates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCandidateById(@PathVariable Long id) {
        try {
            CandidateDTO candidate = candidateService.getCandidateById(id);
            return ResponseEntity.ok(candidate);
        } catch (Exception e) {
            System.err.println("Error getting candidate by ID: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Candidate not found: " + e.getMessage()));
        }
    }

    @PutMapping("/candidates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCandidate(@PathVariable Long id,
            @Valid @RequestBody CandidateDTO candidateDTO) {
        try {
            System.out.println("Updating candidate ID: " + id);
            CandidateDTO updated = candidateService.updateCandidate(id, candidateDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            System.err.println("Error updating candidate: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to update candidate: " + e.getMessage()));
        }
    }

    @DeleteMapping("/admin/candidates/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCandidate(@PathVariable Long id) {
        try {
            candidateService.deleteCandidate(id);
            return ResponseEntity.ok("Candidate deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting candidate: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to delete candidate: " + e.getMessage()));
        }
    }

    // ============================================
    // ADDITIONAL UTILITY ENDPOINTS
    // ============================================

    @GetMapping("/admin/candidates/check-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = candidateService.checkEmailExists(email);
            return ResponseEntity.ok(java.util.Map.of("exists", exists));
        } catch (Exception e) {
            System.err.println("Error checking email: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to check email: " + e.getMessage()));
        }
    }

    // Debug endpoint
    @GetMapping("/admin/candidates/debug")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> debugCandidates() {
        try {
            System.out.println("=== DEBUG CANDIDATES ===");

            // Get all candidates
            List<CandidateDTO> allCandidates = candidateService.getAllCandidates(null, null, null);

            return ResponseEntity.ok(java.util.Map.of(
                    "totalCandidates", allCandidates.size(),
                    "candidates", allCandidates));

        } catch (Exception e) {
            System.err.println("Debug error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Debug failed: " + e.getMessage()));
        }
    }
}