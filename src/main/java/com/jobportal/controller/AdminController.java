package com.jobportal.controller;

import com.jobportal.model.User;
import com.jobportal.model.Vacancy;
import com.jobportal.service.VacancyService;
import com.jobportal.model.enums.AccountStatus;
import com.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Operations", description = "Endpoints for administrator tasks like user management and job moderation")
public class AdminController {

    private final UserService userService;
    private final VacancyService vacancyService;

    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system (Admin only).")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Update user status", description = "Activates or deactivates a user account (Admin only).")
    public ResponseEntity<User> updateUserStatus(@PathVariable("userId") Integer userId, @RequestParam("status") AccountStatus status) {
        try {
            User updatedUser = userService.updateUserStatus(userId, status);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/vacancies/{id}/approve")
    @Operation(summary = "Approve a vacancy", description = "Approves a pending job vacancy, making it visible to applicants.")
    public ResponseEntity<Vacancy> approveVacancy(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vacancyService.approveVacancy(id));
    }

    @PatchMapping("/vacancies/{id}/reject")
    @Operation(summary = "Reject a vacancy", description = "Rejects a pending job vacancy.")
    public ResponseEntity<Vacancy> rejectVacancy(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(vacancyService.rejectVacancy(id));
    }
}
