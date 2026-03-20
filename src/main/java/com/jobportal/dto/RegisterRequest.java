package com.jobportal.dto;

import com.jobportal.model.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;
    private String fullName; // For Applicant
    private String companyName; // For Recruiter
}
