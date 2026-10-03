package org.logitrack.authservice.service;

import org.logitrack.authservice.client.NotificationClient;
import org.logitrack.authservice.model.OtpVerification;
import org.logitrack.authservice.repository.OtpVerificationRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class OtpService {

    private final OtpVerificationRepository otpVerificationRepository;
    private final NotificationClient notificationClient;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpService(OtpVerificationRepository otpVerificationRepository, NotificationClient notificationClient) {
        this.otpVerificationRepository = otpVerificationRepository;
        this.notificationClient = notificationClient;
    }

    public String generateAndSaveOtp(String email) {
        // Remove previous OTP for this email
        otpVerificationRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));

        // OTP expires after 5 minutes
        Instant expiresAt = Instant.now().plusSeconds(5 * 60);

        OtpVerification otpVerification =
                new OtpVerification(email, otp, expiresAt);

        otpVerificationRepository.save(otpVerification);

        notificationClient.sendNotification(email, "LogiTrack - OTP Veification", "Your LogiTrack OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n" +
                "Please do not share this OTP with anyone.", "EMAIL");

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        OtpVerification otpVerification =
                otpVerificationRepository
                        .findTopByEmailOrderByCreatedAtDesc(email)
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpVerification.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!otpVerification.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);
        return true;
    }
}