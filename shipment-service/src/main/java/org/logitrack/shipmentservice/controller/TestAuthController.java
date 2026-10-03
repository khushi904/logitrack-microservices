package org.logitrack.shipmentservice.controller;

import org.logitrack.shipmentservice.client.AuthClient;
import org.logitrack.shipmentservice.dto.UserResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-auth")
public class TestAuthController {

    private final AuthClient authClient;

    public TestAuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/{id}")
    public UserResponse getUserEmail(@PathVariable String id) {
        return authClient.getUserById(id);
    }
}