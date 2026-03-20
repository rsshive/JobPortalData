package com.jobportal.service;

import com.jobportal.model.Applicant;
import com.jobportal.model.SavedJob;

import java.util.List;

public interface SavedJobService {
    SavedJob saveJob(Integer vacancyId, Applicant applicant);
    void unsaveJob(Integer savedJobId);
    List<SavedJob> findByApplicant(Applicant applicant);
}
