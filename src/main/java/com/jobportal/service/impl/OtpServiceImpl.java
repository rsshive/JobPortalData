package com.jobportal.service.impl;

import com.jobportal.model.Otp;
import com.jobportal.repository.OtpRepository;
import com.jobportal.service.EmailService;
import com.jobportal.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final Random random = new Random();

    @Override
    @Transactional
    public String generateOtp(String email) {
        otpRepository.deleteByEmail(email);

        String otpCode = String.format("%06d", random.nextInt(1000000));
        Otp otp = Otp.builder()
                .email(email)
                .otpCode(otpCode)
                .expiryTime(LocalDateTime.now().plusMinutes(5)) // 5 minutes validity
                .build();

        otpRepository.save(otp);
        return otpCode;
    }

    @Override
    @Transactional
    public boolean verifyOtp(String email, String otpCode) {
        Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpCode(email, otpCode);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (otp.getExpiryTime().isAfter(LocalDateTime.now())) {
                otpRepository.deleteByEmail(email);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void sendOtpEmail(String email) {
        String otpCode = generateOtp(email);
        String subject = "Your Job Portal Verification Code";
        String body = "Hello,\n\nYour verification code is: " + otpCode +
                "\nThis code will expire in 5 minutes.\n\nBest regards,\nJob Portal Team";

        emailService.sendEmail(email, subject, body);
    }
}
