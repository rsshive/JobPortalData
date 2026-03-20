package com.jobportal.controller;

import com.jobportal.model.Admin;
import com.jobportal.model.User;
import com.jobportal.model.VacancyReport;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.VacancyReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Vacancy Reports", description = "Endpoints for reporting job vacancies and resolving reports")
public class VacancyReportController {

    private final VacancyReportService vacancyReportService;

    @PostMapping("/vacancy/{vacancyId}")
    @Operation(summary = "Report a vacancy", description = "Allows a user to report a problematic job vacancy.")
    public ResponseEntity<VacancyReport> reportVacancy(
            @PathVariable Integer vacancyId,
            @RequestBody VacancyReport report) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User reporter = userDetails.getUser();

        return ResponseEntity.ok(vacancyReportService.reportVacancy(report, reporter, vacancyId));
    }

    @GetMapping
    @Operation(summary = "Get all reports", description = "Retrieves a list of all vacancy reports (Admin only).")
    public ResponseEntity<List<VacancyReport>> getAllReports() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Admin) {
            return ResponseEntity.ok(vacancyReportService.findAll());
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status", description = "Filters reports by their status, e.g., PENDING or RESOLVED (Admin only).")
    public ResponseEntity<List<VacancyReport>> getReportsByStatus(@PathVariable String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Admin) {
            return ResponseEntity.ok(vacancyReportService.findByStatus(status));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PatchMapping("/{id}/resolve")
    @Operation(summary = "Resolve a report", description = "Marks a vacancy report as resolved by an Admin.")
    public ResponseEntity<VacancyReport> resolveReport(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Admin) {
            Admin admin = (Admin) userDetails.getUser();
            return ResponseEntity.ok(vacancyReportService.resolveReport(id, admin));
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
