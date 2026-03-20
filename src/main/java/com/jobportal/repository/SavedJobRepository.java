package com.jobportal.repository;

import com.jobportal.model.Applicant;
import com.jobportal.model.SavedJob;
import com.jobportal.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, Integer> {
    List<SavedJob> findByApplicant(Applicant applicant);
    boolean existsByApplicantAndVacancy(Applicant applicant, Vacancy vacancy);
}
