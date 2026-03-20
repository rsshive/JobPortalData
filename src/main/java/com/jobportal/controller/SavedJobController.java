package com.jobportal.controller;

import com.jobportal.model.Applicant;
import com.jobportal.model.SavedJob;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.SavedJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
@Tag(name = "Saved Jobs Management", description = "Endpoints for applicants to save and manage job vacancies")
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{vacancyId}")
    @Operation(summary = "Save a job vacancy", description = "Allows an applicant to save a specific job vacancy.")
    public ResponseEntity<SavedJob> saveJob(@PathVariable Integer vacancyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Applicant) {
            Applicant applicant = (Applicant) userDetails.getUser();
            return ResponseEntity.ok(savedJobService.saveJob(vacancyId, applicant));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{savedJobId}")
    @Operation(summary = "Unsave a job vacancy", description = "Allows an applicant to remove a job from their saved list.")
    public ResponseEntity<Void> unsaveJob(@PathVariable Integer savedJobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Applicant) {
            // Further security check could be added to ensure the applicant owns the saved job
            savedJobService.unsaveJob(savedJobId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get my saved jobs", description = "Retrieves a list of all jobs saved by the authenticated applicant.")
    public ResponseEntity<List<SavedJob>> getMySavedJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Applicant) {
            Applicant applicant = (Applicant) userDetails.getUser();
            return ResponseEntity.ok(savedJobService.findByApplicant(applicant));
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
