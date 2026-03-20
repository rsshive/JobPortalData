package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.model.Application;
import com.jobportal.model.Vacancy;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Application apply(Application application) {
        application.setStatus("APPLIED");
        return applicationRepository.save(application);
    }

    @Override
    public Optional<Application> findById(Integer applicationId) {
        return applicationRepository.findById(applicationId);
    }

    @Override
    public List<Application> findByApplicant(Applicant applicant) {
        return applicationRepository.findByApplicant(applicant);
    }

    @Override
    public List<Application> findByVacancy(Vacancy vacancy) {
        return applicationRepository.findByVacancy(vacancy);
    }

    @Override
    @Transactional
    public void updateStatus(Integer applicationId, String status) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(status);
        applicationRepository.save(application);
    }
}
