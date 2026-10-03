
package org.logitrack.authservice.controller;

import jakarta.validation.Valid;
import org.logitrack.authservice.dto.SendOtpRequest;
import org.logitrack.authservice.dto.VerifyOtpRequest;
import org.logitrack.authservice.model.User;
import org.logitrack.authservice.service.JwtService;
import org.logitrack.authservice.service.OtpService;
import org.logitrack.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final OtpService otpService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(OtpService otpService, UserService userService, JwtService jwtService) {
        this.otpService = otpService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        userService.createOrUpdateUser(
                request.getEmail(),
                request.getName(),
                request.getPhoneNumber()
        );

        otpService.generateAndSaveOtp(request.getEmail());

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP generated successfully"
                )
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        User user = userService.findOrCreateUser(
                request.getEmail()
        );

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP verified successfully",
                        "token", token
                )
        );
    }
}
