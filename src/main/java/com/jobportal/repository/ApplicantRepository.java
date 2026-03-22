package com.jobportal.repository;

import com.jobportal.model.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer>, JpaSpecificationExecutor<Applicant> {
    
    Optional<Applicant> findByEmail(String email);
}
