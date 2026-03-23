package com.jobportal.service.impl;

import com.jobportal.model.Applicant;
import com.jobportal.model.CV;
import com.jobportal.repository.CVRepository;
import com.jobportal.service.CVService;
import com.jobportal.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CVServiceImpl implements CVService {

    private final CVRepository cvRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public CV uploadCV(CV cv) {
        return cvRepository.save(cv);
    }

    @Override
    @Transactional
    public CV uploadCV(MultipartFile file, Applicant applicant) {
        String fileName = fileStorageService.storeFile(file);
        
        CV cv = CV.builder()
                .applicant(applicant)
                .filePath(fileName)
                .build();
        
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
