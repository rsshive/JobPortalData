package com.jobportal.repository;

import com.jobportal.model.Recruiter;
import com.jobportal.model.Vacancy;
import com.jobportal.model.enums.VacancyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Integer>, JpaSpecificationExecutor<Vacancy> {
    List<Vacancy> findByRecruiter(Recruiter recruiter);
    List<Vacancy> findByStatus(VacancyStatus status);
}
