package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Job Applications", description = "Endpoints for managing job applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @Operation(summary = "Submit an application", description = "Allows an applicant to apply for a job vacancy.")
    public ResponseEntity<Application> apply(@RequestBody Application application) {
        return ResponseEntity.ok(applicationService.apply(application));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application details", description = "Retrieves a specific job application by its ID.")
    public ResponseEntity<Application> getApplication(@PathVariable("id") Integer id) {
        return applicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update application status", description = "Updates the status of an application (e.g., ACCEPTED, REJECTED).")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") Integer id, @RequestParam("status") String status) {
        applicationService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
