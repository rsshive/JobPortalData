package com.jobportal.repository;

import com.jobportal.model.Applicant;
import com.jobportal.model.JobAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobAlertRepository extends JpaRepository<JobAlert, Integer> {
    List<JobAlert> findByApplicant(Applicant applicant);
}
