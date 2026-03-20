package com.jobportal.repository;

import com.jobportal.model.Applicant;
import com.jobportal.model.Application;
import com.jobportal.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByApplicant(Applicant applicant);
    List<Application> findByVacancy(Vacancy vacancy);
}
