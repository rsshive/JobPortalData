package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.model.JobAlert;
import com.jobportal.model.Vacancy;
import com.jobportal.repository.ApplicantRepository;
import com.jobportal.repository.JobAlertRepository;
import com.jobportal.service.EmailService;
import com.jobportal.service.JobAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobAlertServiceImpl implements JobAlertService {

    private final JobAlertRepository jobAlertRepository;
    private final ApplicantRepository applicantRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public JobAlert createAlert(String applicantEmail, JobAlert jobAlert) {
        Applicant applicant = applicantRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        jobAlert.setApplicant(applicant);
        return jobAlertRepository.save(jobAlert);
    }

    @Override
    public List<JobAlert> getAlertsByApplicantEmail(String applicantEmail) {
        Applicant applicant = applicantRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
        return jobAlertRepository.findByApplicant(applicant);
    }

    @Override
    @Transactional
    public void deleteAlert(Integer alertId, String applicantEmail) {
        JobAlert alert = jobAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Job Alert not found"));
        
        if (!alert.getApplicant().getEmail().equals(applicantEmail)) {
            throw new RuntimeException("Unauthorized to delete this alert");
        }
        
        jobAlertRepository.delete(alert);
    }

    @Override
    public void matchAndNotify(Vacancy vacancy) {
        List<JobAlert> allAlerts = jobAlertRepository.findAll();
        
        for (JobAlert alert : allAlerts) {
            boolean matches = true;

            if (alert.getKeywords() != null && !alert.getKeywords().isEmpty()) {
                String keywords = alert.getKeywords().toLowerCase();
                String title = vacancy.getTitle() != null ? vacancy.getTitle().toLowerCase() : "";
                String skills = vacancy.getRequiredSkills() != null ? vacancy.getRequiredSkills().toLowerCase() : "";
                if (!title.contains(keywords) && !skills.contains(keywords)) {
                    matches = false;
                }
            }

            if (matches && alert.getLocation() != null && !alert.getLocation().isEmpty()) {
                String location = vacancy.getLocation() != null ? vacancy.getLocation().toLowerCase() : "";
                if (!location.contains(alert.getLocation().toLowerCase())) {
                    matches = false;
                }
            }

            if (matches && alert.getSalaryMin() != null) {
                Integer maxSalary = vacancy.getSalaryMax();
                if (maxSalary == null || maxSalary < alert.getSalaryMin()) {
                    matches = false;
                }
            }

            if (matches) {
                // Send email
                String email = alert.getApplicant().getEmail();
                String subject = "New Job Alert: " + vacancy.getTitle();
                String body = "A new job matching your alert criteria has been posted.\n\n" +
                              "Title: " + vacancy.getTitle() + "\n" +
                              "Location: " + vacancy.getLocation() + "\n" +
                              "Company: " + (vacancy.getCompany() != null ? vacancy.getCompany().getName() : "Unknown") + "\n\n" +
                              "Check out the portal to apply!";
                emailService.sendEmail(email, subject, body);
            }
        }
    }
}
