package org.logitrack.shipmentservice.dto;

public record NotificationRequest(
        String userId,
        String type,
        String message,
        String channel
) {
}