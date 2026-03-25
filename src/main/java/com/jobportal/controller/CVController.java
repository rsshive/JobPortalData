package com.jobportal.controller;

import com.jobportal.model.Applicant;
import com.jobportal.model.CV;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cvs")
@RequiredArgsConstructor
@Tag(name = "CV Management", description = "Endpoints for managing applicant CVs")
public class CVController {

    private final CVService cvService;
    private final com.jobportal.service.FileStorageService fileStorageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload a CV file", description = "Allows an applicant to upload a PDF/DOCX CV file.")
    public ResponseEntity<CV> uploadCVFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Applicant) {
            return ResponseEntity.ok(cvService.uploadCV(file, (Applicant) userDetails.getUser()));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Download a CV file", description = "Allows downloading the physical CV file by CV ID.")
    public ResponseEntity<org.springframework.core.io.Resource> downloadCV(@PathVariable("id") Integer id) {
        Optional<CV> cvOpt = cvService.findById(id);
        if (cvOpt.isPresent()) {
            CV cv = cvOpt.get();
            org.springframework.core.io.Resource resource = fileStorageService.loadFileAsResource(cv.getFilePath());
            
            String contentType = "application/octet-stream";
            try {
                contentType = org.springframework.http.MediaType.APPLICATION_PDF_VALUE; // Simplification for PDF
            } catch (Exception e) {
                // Keep default
            }

            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Get my CVs", description = "Retrieves all CVs for the currently authenticated applicant.")
    public ResponseEntity<List<CV>> getMyCVs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getUser() instanceof Applicant) {
            Applicant applicant = (Applicant) userDetails.getUser();
            return ResponseEntity.ok(cvService.findByApplicant(applicant));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CV by ID", description = "Retrieves a specific CV by its ID.")
    public ResponseEntity<CV> getCV(@PathVariable("id") Integer id) {
        Optional<CV> cvOpt = cvService.findById(id);
        if (cvOpt.isPresent()) {
            CV cv = cvOpt.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Allow access if the user is the owner, or if we want recruiters to view it (simplification: owner only)
            if (userDetails.getUser() instanceof Applicant && cv.getApplicant().getUserId().equals(userDetails.getUser().getUserId())) {
                return ResponseEntity.ok(cv);
            }
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a CV", description = "Deletes a specific CV by its ID (owner only).")
    public ResponseEntity<Void> deleteCV(@PathVariable("id") Integer id) {
        Optional<CV> cvOpt = cvService.findById(id);
        if (cvOpt.isPresent()) {
            CV cv = cvOpt.get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getUser() instanceof Applicant && cv.getApplicant().getUserId().equals(userDetails.getUser().getUserId())) {
                cvService.deleteCV(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.notFound().build();
    }
}
