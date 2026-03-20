package com.jobportal.service;

import com.jobportal.model.Admin;
import com.jobportal.model.User;
import com.jobportal.model.VacancyReport;

import java.util.List;
import java.util.Optional;

public interface VacancyReportService {
    VacancyReport reportVacancy(VacancyReport report, User reporter, Integer vacancyId);
    List<VacancyReport> findAll();
    List<VacancyReport> findByStatus(String status);
    Optional<VacancyReport> findById(Integer reportId);
    VacancyReport resolveReport(Integer reportId, Admin resolver);
}
