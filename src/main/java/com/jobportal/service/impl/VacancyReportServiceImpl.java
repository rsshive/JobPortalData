package com.jobportal.service.impl;

import com.jobportal.model.Admin;
import com.jobportal.model.User;
import com.jobportal.model.Vacancy;
import com.jobportal.model.VacancyReport;
import com.jobportal.repository.VacancyReportRepository;
import com.jobportal.repository.VacancyRepository;
import com.jobportal.service.VacancyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyReportServiceImpl implements VacancyReportService {

    private final VacancyReportRepository vacancyReportRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    @Transactional
    public VacancyReport reportVacancy(VacancyReport report, User reporter, Integer vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        report.setReporter(reporter);
        report.setVacancy(vacancy);
        report.setStatus("PENDING");
        return vacancyReportRepository.save(report);
    }

    @Override
    public List<VacancyReport> findAll() {
        return vacancyReportRepository.findAll();
    }

    @Override
    public List<VacancyReport> findByStatus(String status) {
        return vacancyReportRepository.findByStatus(status);
    }

    @Override
    public Optional<VacancyReport> findById(Integer reportId) {
        return vacancyReportRepository.findById(reportId);
    }

    @Override
    @Transactional
    public VacancyReport resolveReport(Integer reportId, Admin resolver) {
        VacancyReport report = vacancyReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setResolver(resolver);
        report.setStatus("RESOLVED");
        return vacancyReportRepository.save(report);
    }
}
