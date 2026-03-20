package com.jobportal.service;

public interface OtpService {
    String generateOtp(String email);
    boolean verifyOtp(String email, String otpCode);
    void sendOtpEmail(String email);
}
