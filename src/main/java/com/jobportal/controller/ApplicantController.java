package com.jobportal.controller;

import com.jobportal.model.Applicant;
import com.jobportal.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
@Tag(name = "Applicant Profile", description = "Endpoints for managing applicant personal profile and info")
public class ApplicantController {

    private final ApplicantService applicantService;

    @GetMapping("/profile")
    @Operation(summary = "Get current applicant profile", description = "Retrieves the profile of the currently logged-in applicant.")
    public ResponseEntity<Applicant> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return applicantService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    @Operation(summary = "Update applicant profile", description = "Updates the personal information of the currently logged-in applicant.")
    public ResponseEntity<Applicant> updateProfile(Authentication authentication, @RequestBody Applicant updatedProfile) {
        String email = authentication.getName();
        try {
            Applicant savedApplicant = applicantService.updateProfile(email, updatedProfile);
            return ResponseEntity.ok(savedApplicant);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
