package com.jobportal.repository;

import com.jobportal.model.Vacancy;
import com.jobportal.model.VacancyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyReportRepository extends JpaRepository<VacancyReport, Integer> {
    List<VacancyReport> findByVacancy(Vacancy vacancy);
    List<VacancyReport> findByStatus(String status);
}
