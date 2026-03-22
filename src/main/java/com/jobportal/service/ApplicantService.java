package com.jobportal.service;

import com.jobportal.model.Applicant;
import java.util.List;
import java.util.Optional;

public interface ApplicantService {
    List<Applicant> searchApplicants(String skills, String education, String experience);
    Optional<Applicant> findById(Integer id);
    Optional<Applicant> findByEmail(String email);
    List<Applicant> findAll();
    Applicant updateProfile(String email, Applicant updatedProfile);
}
