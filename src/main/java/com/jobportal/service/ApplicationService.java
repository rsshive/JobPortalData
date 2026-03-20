package com.jobportal.service;

import com.jobportal.model.Applicant;
import com.jobportal.model.Application;
import com.jobportal.model.Vacancy;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application apply(Application application);
    Optional<Application> findById(Integer applicationId);
    List<Application> findByApplicant(Applicant applicant);
    List<Application> findByVacancy(Vacancy vacancy);
    void updateStatus(Integer applicationId, String status);
}
