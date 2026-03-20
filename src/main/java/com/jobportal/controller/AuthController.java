package com.jobportal.controller;

import com.jobportal.dto.AuthResponse;
import com.jobportal.dto.LoginRequest;
import com.jobportal.dto.RegisterRequest;
import com.jobportal.dto.UserResponse;
import com.jobportal.model.*;
import com.jobportal.model.enums.AccountStatus;
import com.jobportal.model.enums.Role;
import com.jobportal.repository.PasswordResetTokenRepository;
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

import java.util.UUID;

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
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password request", description = "Generates a reset token and sends an email to the user.")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);

        String resetLink = "http://localhost:8080/reset-password?token=" + token; // Placeholder link
        String subject = "Password Reset Request";
        String body = "To reset your password, click the link below:\n" + resetLink +
                      "\n\nThis link will expire in 24 hours.";
        
        try {
            emailService.sendEmail(email, subject, body);
            return ResponseEntity.ok("Reset link has been sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Validates the reset token and updates the user's password.")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String result = userService.validatePasswordResetToken(token);
        if (result != null) {
            return ResponseEntity.badRequest().body("Token is " + result);
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        
        userService.changeUserPassword(resetToken.getUser(), newPassword);
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
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otpCode) {
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
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
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
