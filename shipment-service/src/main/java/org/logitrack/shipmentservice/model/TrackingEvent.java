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
@Document(collection = "tracking_events")
public class TrackingEvent {

    @Id
    private String id;

    private String trackingNumber;

    private ShipmentStatus status;

    private String description;

    private Instant timestamp;
}