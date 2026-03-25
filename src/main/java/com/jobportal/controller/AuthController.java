package com.jobportal.controller;

import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.dto.UserResponse;
import com.jobportal.model.*;
import com.jobportal.model.enums.AccountStatus;
import com.jobportal.model.enums.Role;
import com.jobportal.security.CustomUserDetails;
import com.jobportal.security.JwtUtil;
import com.jobportal.service.EmailService;
import com.jobportal.service.OtpService;
import com.jobportal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password request", description = "Generates a reset OTP and sends an email to the user.")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String otpCode = otpService.generateOtp(email);

        String subject = "Password Reset Code";
        String body = "Hello,\n\n" +
                      "Your OTP to reset your password is: " + otpCode +
                      "\n\nThis code will expire in 5 minutes.";
        
        try {
            emailService.sendEmail(email, subject, body);
            return ResponseEntity.ok("OTP has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Validates the OTP and updates the user's password.")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email, @RequestParam("otpCode") String otpCode, @RequestParam("newPassword") String newPassword) {
        boolean isVerified = otpService.verifyOtp(email, otpCode);
        if (!isVerified) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        userService.changeUserPassword(user, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers an Applicant or Recruiter in PENDING status and sends an OTP.")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User user;
        if (request.getRole() == Role.APPLICANT) {
            user = Applicant.builder()
                    .email(request.getEmail())
                    .passwordHash(request.getPassword())
                    .role(Role.APPLICANT)
                    .status(AccountStatus.PENDING)
                    .fullName(request.getFullName())
                    .build();
        } else if (request.getRole() == Role.RECRUITER) {
            Company company = null;
            if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
                company = Company.builder().name(request.getCompanyName()).build();
            }
            user = Recruiter.builder()
                    .email(request.getEmail())
                    .passwordHash(request.getPassword())
                    .role(Role.RECRUITER)
                    .status(AccountStatus.PENDING)
                    .company(company)
                    .build();
        } else if (request.getRole() == Role.ADMIN) {
            user = Admin.builder()
                    .email(request.getEmail())
                    .passwordHash(request.getPassword())
                    .role(Role.ADMIN)
                    .status(AccountStatus.ACTIVE)
                    .build();
        } else {
            return ResponseEntity.badRequest().build();
        }

        User registeredUser = userService.registerUser(user);
        
        try {
            otpService.sendOtpEmail(registeredUser.getEmail());
        } catch (Exception e) {
            System.err.println("Failed to send OTP email: " + e.getMessage());
        }

        return ResponseEntity.ok(mapToResponse(registeredUser));
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP and activates the user account.")
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String email, @RequestParam("otpCode") String otpCode) {
        boolean isVerified = otpService.verifyOtp(email, otpCode);
        if (isVerified) {
            userService.updateUserStatusByEmail(email, AccountStatus.ACTIVE);
            return ResponseEntity.ok("Verification successful. Your account is now active.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    @PostMapping("/resend-otp")
    @Operation(summary = "Resend OTP", description = "Sends a new verification code to the user's email.")
    public ResponseEntity<String> resendOtp(@RequestParam("email") String email) {
        try {
            otpService.sendOtpEmail(email);
            return ResponseEntity.ok("A new verification code has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token for accessing protected routes.")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        var token = jwtUtil.generateToken(userDetails);
        
        User loggedInUser = ((CustomUserDetails) userDetails).getUser();
        UserResponse userResponse = mapToResponse(loggedInUser);
        
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build());
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }
}
