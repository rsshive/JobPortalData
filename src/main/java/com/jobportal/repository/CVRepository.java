package com.jobportal.repository;

import com.jobportal.model.Applicant;
import com.jobportal.model.CV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CVRepository extends JpaRepository<CV, Integer> {
    List<CV> findByApplicant(Applicant applicant);
}
