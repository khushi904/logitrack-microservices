package org.logitrack.authservice.controller;

import jakarta.validation.Valid;
import org.logitrack.authservice.dto.UserUpdateProfileRequest;
import org.logitrack.authservice.model.User;
import org.logitrack.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.logitrack.authservice.model.Role;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @RequestParam String email,
            @Valid @RequestBody UserUpdateProfileRequest request) {

        userService.updateProfile(
                email,
                request.getName(),
                request.getPhoneNumber()
        );

        return ResponseEntity.ok(
                Map.of("message", "Profile updated successfully")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{email}/role")
    public ResponseEntity<Map<String, String>> updateRole(
            @PathVariable String email,
            @RequestParam Role role) {

        userService.updateRole(email, role);

        return ResponseEntity.ok(
                Map.of("message", "User role updated successfully")
        );
    }
}