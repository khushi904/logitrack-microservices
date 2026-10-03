package org.logitrack.shipmentservice.service;

import org.logitrack.shipmentservice.dto.NotificationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationClient {

    private final RestClient restClient;

    public NotificationClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendNotification(
            String userId,
            String type,
            String message,
            String channel) {

        NotificationRequest request = new NotificationRequest(
                userId,
                type,
                message,
                channel
        );

        restClient.post()
                .uri("/notifications")
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}