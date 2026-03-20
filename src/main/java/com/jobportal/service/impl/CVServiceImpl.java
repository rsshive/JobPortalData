package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.model.CV;
import com.jobportal.repository.CVRepository;
import com.jobportal.service.CVService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CVServiceImpl implements CVService {

    private final CVRepository cvRepository;

    @Override
    @Transactional
    public CV uploadCV(CV cv) {
        return cvRepository.save(cv);
    }

    @Override
    public Optional<CV> findById(Integer cvId) {
        return cvRepository.findById(cvId);
    }

    @Override
    public List<CV> findByApplicant(Applicant applicant) {
        return cvRepository.findByApplicant(applicant);
    }

    @Override
    @Transactional
    public void deleteCV(Integer cvId) {
        cvRepository.deleteById(cvId);
    }
}
