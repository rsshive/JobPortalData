package com.jobportal.service.impl;

import com.jobportal.model.Recruiter;
import com.jobportal.model.Vacancy;
import com.jobportal.model.enums.VacancyStatus;
import com.jobportal.repository.RecruiterRepository;
import com.jobportal.repository.VacancyRepository;
import com.jobportal.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    @Transactional
    public Vacancy postVacancy(Vacancy vacancy) {
        if (vacancy.getRecruiter() != null && vacancy.getRecruiter().getUserId() != null) {
            Recruiter recruiter = recruiterRepository.findById(vacancy.getRecruiter().getUserId())
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));
            vacancy.setRecruiter(recruiter);
            if (recruiter.getCompany() != null) {
                vacancy.setCompany(recruiter.getCompany());
            }
        }
        vacancy.setStatus(VacancyStatus.PENDING);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public Optional<Vacancy> findById(Integer vacancyId) {
        return vacancyRepository.findById(vacancyId);
    }

    @Override
    public List<Vacancy> findAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> findByRecruiter(Recruiter recruiter) {
        return vacancyRepository.findByRecruiter(recruiter);
    }

    @Override
    public List<Vacancy> findByStatus(VacancyStatus status) {
        return vacancyRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void updateVacancy(Vacancy vacancy) {
        Vacancy existing = vacancyRepository.findById(vacancy.getVacancyId())
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        
        if (vacancy.getStatus() == null) {
            vacancy.setStatus(existing.getStatus());
        }
        
        if (vacancy.getRecruiter() != null && vacancy.getRecruiter().getUserId() != null) {
            Recruiter recruiter = recruiterRepository.findById(vacancy.getRecruiter().getUserId())
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));
            vacancy.setRecruiter(recruiter);
        }
        vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public void deleteVacancy(Integer vacancyId) {
        vacancyRepository.deleteById(vacancyId);
    }

    @Override
    @Transactional
    public Vacancy approveVacancy(Integer id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        vacancy.setStatus(VacancyStatus.OPEN);
        return vacancyRepository.save(vacancy);
    }

    @Override
    @Transactional
    public Vacancy rejectVacancy(Integer id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        vacancy.setStatus(VacancyStatus.REJECTED);
        return vacancyRepository.save(vacancy);
    }

    @Override
    public List<Vacancy> searchVacancies(String title, String location, Integer minSalary, Integer maxSalary, String type) {
        Specification<Vacancy> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("status"), VacancyStatus.OPEN));

            if (title != null && !title.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (location != null && !location.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }

            if (minSalary != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("salaryMin"), minSalary));
            }

            if (maxSalary != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("salaryMax"), maxSalary));
            }

            if (type != null && !type.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("employmentType")), type.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return vacancyRepository.findAll(spec);
    }
}
