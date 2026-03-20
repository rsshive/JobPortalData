package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.model.SavedJob;
import com.jobportal.model.Vacancy;
import com.jobportal.repository.SavedJobRepository;
import com.jobportal.repository.VacancyRepository;
import com.jobportal.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    @Transactional
    public SavedJob saveJob(Integer vacancyId, Applicant applicant) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (savedJobRepository.existsByApplicantAndVacancy(applicant, vacancy)) {
            throw new RuntimeException("Job already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .applicant(applicant)
                .vacancy(vacancy)
                .build();
        return savedJobRepository.save(savedJob);
    }

    @Override
    @Transactional
    public void unsaveJob(Integer savedJobId) {
        savedJobRepository.deleteById(savedJobId);
    }

    @Override
    public List<SavedJob> findByApplicant(Applicant applicant) {
        return savedJobRepository.findByApplicant(applicant);
    }
}
