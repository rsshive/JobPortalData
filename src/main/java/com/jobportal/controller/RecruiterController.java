package com.jobportal.controller;

import com.jobportal.model.Applicant;
import com.jobportal.service.ApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
@Tag(name = "Recruiter Tools", description = "Endpoints for recruiters to source talent and manage listings")
public class RecruiterController {

    private final ApplicantService applicantService;

    @GetMapping("/search-applicants")
    @Operation(summary = "Search for talent", description = "Allows recruiters to find applicants based on skills, education, and work experience.")
    public List<Applicant> searchApplicants(
            @RequestParam(name = "skills", required = false) String skills,
            @RequestParam(name = "education", required = false) String education,
            @RequestParam(name = "experience", required = false) String experience) {
        return applicantService.searchApplicants(skills, education, experience);
    }
}
