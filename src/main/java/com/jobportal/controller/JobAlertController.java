package com.jobportal.controller;

import com.jobportal.model.JobAlert;
import com.jobportal.service.JobAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Tag(name = "Job Alerts", description = "Endpoints for managing automated job alerts for applicants")
public class JobAlertController {

    private final JobAlertService jobAlertService;

    @PostMapping
    @Operation(summary = "Create a job alert", description = "Creates a new job alert for the authenticated applicant.")
    public ResponseEntity<JobAlert> createAlert(Authentication authentication, @RequestBody JobAlert jobAlert) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobAlertService.createAlert(email, jobAlert));
    }

    @GetMapping
    @Operation(summary = "Get all alerts", description = "Retrieves all job alerts for the authenticated applicant.")
    public ResponseEntity<List<JobAlert>> getAlerts(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(jobAlertService.getAlertsByApplicantEmail(email));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an alert", description = "Deletes a specific job alert by ID.")
    public ResponseEntity<Void> deleteAlert(Authentication authentication, @PathVariable Integer id) {
        String email = authentication.getName();
        try {
            jobAlertService.deleteAlert(id, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
