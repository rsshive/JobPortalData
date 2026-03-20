package com.jobportal.service;

import com.jobportal.model.Recruiter;
import com.jobportal.model.Vacancy;
import com.jobportal.model.enums.VacancyStatus;

import java.util.List;
import java.util.Optional;

public interface VacancyService {
    Vacancy postVacancy(Vacancy vacancy);
    Optional<Vacancy> findById(Integer vacancyId);
    List<Vacancy> findAll();
    List<Vacancy> findByRecruiter(Recruiter recruiter);
    List<Vacancy> findByStatus(VacancyStatus status);
    void updateVacancy(Vacancy vacancy);
    void deleteVacancy(Integer vacancyId);
    
    // New methods for moderation and search
    Vacancy approveVacancy(Integer id);
    Vacancy rejectVacancy(Integer id);
    List<Vacancy> searchVacancies(String title, String location, Integer minSalary, Integer maxSalary, String type);
}
