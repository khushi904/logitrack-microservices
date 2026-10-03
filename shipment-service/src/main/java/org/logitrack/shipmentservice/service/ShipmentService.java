package org.logitrack.shipmentservice.service;

import org.logitrack.shipmentservice.dto.CreateShipmentRequest;
import org.logitrack.shipmentservice.dto.UpdateShipmentStatusRequest;
import org.logitrack.shipmentservice.client.AuthClient;
import org.logitrack.shipmentservice.dto.UserResponse;
import org.logitrack.shipmentservice.model.Shipment;
import org.logitrack.shipmentservice.model.ShipmentStatus;
import org.logitrack.shipmentservice.model.TrackingEvent;
import org.logitrack.shipmentservice.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final TrackingEventService trackingEventService;
    private final NotificationClient notificationClient;
    private final AuthClient authClient;

    public ShipmentService(
            ShipmentRepository shipmentRepository,
            TrackingEventService trackingEventService,
            NotificationClient notificationClient,
            AuthClient authClient) {
        this.shipmentRepository = shipmentRepository;
        this.trackingEventService = trackingEventService;
        this.notificationClient = notificationClient;
        this.authClient = authClient;
    }

    public Shipment createShipment(CreateShipmentRequest request, String senderId) {

        Shipment shipment = new Shipment();

        shipment.setTrackingNumber(generateTrackingNumber());
        shipment.setSenderId(senderId);
        shipment.setReceiverName(request.getReceiverName());
        shipment.setReceiverPhone(request.getReceiverPhone());
        shipment.setPickupAddress(request.getPickupAddress());
        shipment.setDeliveryAddress(request.getDeliveryAddress());
        shipment.setWeight(request.getWeight());
        shipment.setCategory(request.getCategory());
        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setCreatedAt(Instant.now());
        shipment.setUpdatedAt(Instant.now());

        Shipment savedShipment = shipmentRepository.save(shipment);

        UserResponse userResponse = authClient.getUserById(senderId);

        trackingEventService.addEvent(
                savedShipment.getTrackingNumber(),
                ShipmentStatus.CREATED,
                "Shipment created"
        );

        notificationClient.sendNotification(
                userResponse.email(),
                "SHIPMENT_CREATED",
                "Your shipment " + savedShipment.getTrackingNumber() + " has been created",
                "EMAIL"
        );

        return savedShipment;
    }

    private String generateTrackingNumber() {
        return "LT-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public Shipment getByTrackingNumber(String trackingNumber) {

        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));
    }

    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    public Shipment updateStatus(
            String trackingNumber,
            UpdateShipmentStatusRequest request,
            String userId,
            String role) {

        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        if ("ROLE_DELIVERY_AGENT".equals(role)
                && !userId.equals(shipment.getAssignedAgentId())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not authorized to update this shipment");
        }

        ShipmentStatus currentStatus = shipment.getStatus();
        ShipmentStatus newStatus = request.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition from " + currentStatus + " to " + newStatus);
        }

        shipment.setStatus(newStatus);
        shipment.setUpdatedAt(Instant.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        String description = request.getDescription();
        if (description == null || description.isBlank()) {
            description = "Shipment status updated to " + newStatus;
        }

        trackingEventService.addEvent(
                trackingNumber,
                newStatus,
                description
        );

        UserResponse userResponse = authClient.getUserById(shipment.getSenderId());

        notificationClient.sendNotification(
                userResponse.email(),
                "SHIPMENT_STATUS_UPDATED",
                "Your shipment " + trackingNumber + " status has been updated to " + newStatus,
                "EMAIL"
        );

        return updatedShipment;
    }

    private boolean isValidTransition(
            ShipmentStatus currentStatus,
            ShipmentStatus newStatus) {

        return switch (currentStatus) {
            case CREATED -> newStatus == ShipmentStatus.PICKED_UP;
            case PICKED_UP -> newStatus == ShipmentStatus.IN_TRANSIT;
            case IN_TRANSIT -> newStatus == ShipmentStatus.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> newStatus == ShipmentStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
    }

    public List<Shipment> getShipmentsBySenderId(String senderId) {
        return shipmentRepository.findBySenderId(senderId);
    }

    public Shipment assignShipment(
            String trackingNumber,
            String agentId) {

        Shipment shipment = shipmentRepository
                .findByTrackingNumber(trackingNumber)
                .orElseThrow(() ->
                        new RuntimeException("Shipment not found"));

        shipment.setAssignedAgentId(agentId);
        shipment.setUpdatedAt(Instant.now());

        Shipment updatedShipment = shipmentRepository.save(shipment);

        UserResponse agentResponse = authClient.getUserById(agentId);

        notificationClient.sendNotification(
                agentResponse.email(),
                "SHIPMENT_ASSIGNED",
                "Shipment " + trackingNumber + " has been assigned to you for delivery",
                "EMAIL"
        );

        return updatedShipment;
    }

    public List<Shipment> getShipmentsByAgentId(String agentId) {
        return shipmentRepository.findByAssignedAgentId(agentId);
    }


}
