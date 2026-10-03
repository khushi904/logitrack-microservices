package org.logitrack.notificationservice.service;

import org.logitrack.notificationservice.dto.CreateNotificationRequest;
import org.logitrack.notificationservice.model.Notification;
import org.logitrack.notificationservice.repository.NotificationRepository;
import org.logitrack.notificationservice.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public NotificationService(
            NotificationRepository notificationRepository,
            EmailService emailService) {
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    public Notification createNotification(CreateNotificationRequest request) {
        Notification notification = new Notification();
        if ("EMAIL".equalsIgnoreCase(request.getChannel())) {
            emailService.sendEmail(
                    request.getUserId(),
                    request.getType(),
                    request.getMessage()
            );
        }

        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setChannel(request.getChannel());
        notification.setRead(false);
        notification.setCreatedAt(Instant.now());

        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Notification markAsRead(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}
