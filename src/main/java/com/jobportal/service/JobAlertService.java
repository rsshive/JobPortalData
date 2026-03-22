package com.jobportal.service;

import com.jobportal.model.JobAlert;
import com.jobportal.model.Vacancy;

import java.util.List;

public interface JobAlertService {
    JobAlert createAlert(String applicantEmail, JobAlert jobAlert);
    List<JobAlert> getAlertsByApplicantEmail(String applicantEmail);
    void deleteAlert(Integer alertId, String applicantEmail);
    void matchAndNotify(Vacancy vacancy);
}
