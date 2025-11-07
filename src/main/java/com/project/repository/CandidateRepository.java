// package com.project.repository;

// import com.project.model.Candidate;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

// import java.time.LocalDateTime;
// import java.util.Optional;

// @Repository
// public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
//     boolean existsByEmail(String email);

//     boolean existsByIdentityProofNumber(String identityProofNumber);

//     Optional<Candidate> findByEmail(String email);

//     @Query("SELECT COUNT(c) FROM Candidate c WHERE c.createdAt BETWEEN :startDate AND :endDate")
//     long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
// }

package com.project.repository;

import com.project.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // Basic CRUD operations are inherited from JpaRepository

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if identity proof number exists
    boolean existsByIdentityProofNumber(String identityProofNumber);

    // Find candidate by email
    Optional<Candidate> findByEmail(String email);

    // Count candidates by date range
    @Query("SELECT COUNT(c) FROM Candidate c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Filter methods
    List<Candidate> findByQualification(String qualification);

    List<Candidate> findByOccupationStatus(String occupationStatus);

    List<Candidate> findByLocation(String location);

    List<Candidate> findByQualificationAndOccupationStatus(String qualification, String occupationStatus);

    List<Candidate> findByQualificationAndLocation(String qualification, String location);

    List<Candidate> findByOccupationStatusAndLocation(String occupationStatus, String location);

    List<Candidate> findByQualificationAndOccupationStatusAndLocation(
            String qualification, String occupationStatus, String location);
}