package org.logitrack.shipmentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipments")
public class Shipment {

    @Id
    private String id;

    private String trackingNumber;

    private String senderId;

    private String assignedAgentId;

    private String receiverName;

    private String receiverPhone;

    private String pickupAddress;

    private String deliveryAddress;

    private Double weight;

    private String category;

    private ShipmentStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}