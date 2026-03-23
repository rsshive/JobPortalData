package com.jobportal.service;

import com.jobportal.model.Applicant;
import com.jobportal.model.CV;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

public interface CVService {
    CV uploadCV(CV cv);
    CV uploadCV(MultipartFile file, Applicant applicant);
    Optional<CV> findById(Integer cvId);
    List<CV> findByApplicant(Applicant applicant);
    void deleteCV(Integer cvId);
}
