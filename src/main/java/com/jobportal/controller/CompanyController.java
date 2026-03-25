package com.jobportal.controller;

import com.jobportal.model.Company;
import com.jobportal.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Endpoints for managing company profiles")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @Operation(summary = "Registration a company", description = "Allows a recruiter to register their company information.")
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.createCompany(company));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company details", description = "Retrieves information about a specific company.")
    public ResponseEntity<Company> getCompany(@PathVariable("id") Integer id) {
        return companyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "List all companies", description = "Retrieves a list of all registered companies.")
    public List<Company> getAllCompanies() {
        return companyService.findAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update company profile", description = "Allows a recruiter to update their company information.")
    public ResponseEntity<Company> updateCompany(@PathVariable("id") Integer id, @RequestBody Company company) {
        company.setCompanyId(id);
        return ResponseEntity.ok(companyService.updateCompany(company));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a company", description = "Deletes a company record from the system.")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Integer id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
