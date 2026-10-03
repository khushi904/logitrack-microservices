package org.logitrack.authservice.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8083")
                .build();
    }

    public void sendNotification(
            String userId,
            String type,
            String message,
            String channel) {

        Map<String, String> request = Map.of(
                "userId", userId,
                "type", type,
                "message", message,
                "channel", channel
        );

        restClient.post()
                .uri("/notifications")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}