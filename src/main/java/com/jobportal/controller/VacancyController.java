package com.jobportal.controller;

import com.jobportal.model.Vacancy;
import com.jobportal.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.model.Recruiter;

import com.jobportal.model.enums.VacancyStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
@Tag(name = "Job Vacancies", description = "Endpoints for managing job vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @PostMapping
    @Operation(summary = "Post a job vacancy", description = "Allows a Recruiter to post a new job vacancy.")
    public ResponseEntity<Vacancy> postVacancy(@RequestBody Vacancy vacancy) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        if (userDetails.getUser() instanceof Recruiter) {
            vacancy.setRecruiter((Recruiter) userDetails.getUser());
            return ResponseEntity.ok(vacancyService.postVacancy(vacancy));
        } else {
            return ResponseEntity.status(403).build(); // Only recruiters can post vacancies
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vacancy details", description = "Retrieves a specific job vacancy by its ID.")
    public ResponseEntity<Vacancy> getVacancy(@PathVariable Integer id) {
        return vacancyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all vacancies", description = "Retrieves a list of all job vacancies.")
    public List<Vacancy> getAllVacancies() {
        return vacancyService.findAll();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get vacancies by status", description = "Retrieves job vacancies filtered by status (e.g., OPEN, CLOSED).")
    public List<Vacancy> getVacanciesByStatus(@PathVariable VacancyStatus status) {
        return vacancyService.findByStatus(status);
    }

    @GetMapping("/search")
    @Operation(summary = "Search job vacancies", description = "Searches for job vacancies based on various filters.")
    public List<Vacancy> searchVacancies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            @RequestParam(required = false) String type) {
        return vacancyService.searchVacancies(title, location, minSalary, maxSalary, type);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a vacancy", description = "Updates an existing job vacancy.")
    public ResponseEntity<Void> updateVacancy(@PathVariable Integer id, @RequestBody Vacancy vacancy) {
        vacancy.setVacancyId(id);
        vacancyService.updateVacancy(vacancy);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vacancy", description = "Deletes a job vacancy by its ID.")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Integer id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok().build();
    }
}
