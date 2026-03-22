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

    @Override
    public Optional<Applicant> findByEmail(String email) {
        return applicantRepository.findByEmail(email);
    }

    @Override
    public Applicant updateProfile(String email, Applicant updatedProfile) {
        Applicant applicant = applicantRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        // Update fields if provided
        if (updatedProfile.getFullName() != null) applicant.setFullName(updatedProfile.getFullName());
        if (updatedProfile.getPhone() != null) applicant.setPhone(updatedProfile.getPhone());
        if (updatedProfile.getSkills() != null) applicant.setSkills(updatedProfile.getSkills());
        if (updatedProfile.getEducation() != null) applicant.setEducation(updatedProfile.getEducation());
        if (updatedProfile.getWorkExperience() != null) applicant.setWorkExperience(updatedProfile.getWorkExperience());
        if (updatedProfile.getJobPreferences() != null) applicant.setJobPreferences(updatedProfile.getJobPreferences());

        return applicantRepository.save(applicant);
    }
}
