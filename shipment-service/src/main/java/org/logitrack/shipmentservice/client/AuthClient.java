package org.logitrack.shipmentservice.client;

import org.logitrack.shipmentservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${AUTH_SERVICE_URL:http://localhost:8081}")
public interface AuthClient {

    @GetMapping("/users/{id}")
    UserResponse getUserById(@PathVariable String id);
}