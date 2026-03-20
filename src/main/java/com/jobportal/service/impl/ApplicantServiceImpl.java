package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.repository.ApplicantRepository;
import com.jobportal.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;

    @Override
    public List<Applicant> searchApplicants(String skills, String education, String experience) {
        Specification<Applicant> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (skills != null && !skills.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("skills")), "%" + skills.toLowerCase() + "%"));
            }

            if (education != null && !education.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("education")), "%" + education.toLowerCase() + "%"));
            }

            if (experience != null && !experience.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("workExperience")), "%" + experience.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return applicantRepository.findAll(spec);
    }

    @Override
    public Optional<Applicant> findById(Integer id) {
        return applicantRepository.findById(id);
    }

    @Override
    public List<Applicant> findAll() {
        return applicantRepository.findAll();
    }
}
