package org.logitrack.notificationservice.controller;

import jakarta.validation.Valid;
import org.logitrack.notificationservice.dto.CreateNotificationRequest;
import org.logitrack.notificationservice.model.Notification;
import org.logitrack.notificationservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationService.createNotification(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(
            @PathVariable String userId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUserId(userId)
        );
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable String notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId)
        );
    }
}
