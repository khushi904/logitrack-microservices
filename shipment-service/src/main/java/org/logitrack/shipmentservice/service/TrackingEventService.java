package org.logitrack.shipmentservice.service;

import org.logitrack.shipmentservice.model.ShipmentStatus;
import org.logitrack.shipmentservice.model.TrackingEvent;
import org.logitrack.shipmentservice.repository.TrackingEventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TrackingEventService {

    private final TrackingEventRepository trackingEventRepository;

    public TrackingEventService(TrackingEventRepository trackingEventRepository) {
        this.trackingEventRepository = trackingEventRepository;
    }

    public TrackingEvent addEvent(
            String trackingNumber,
            ShipmentStatus status,
            String description) {

        TrackingEvent event = new TrackingEvent();

        event.setTrackingNumber(trackingNumber);
        event.setStatus(status);
        event.setDescription(description);
        event.setTimestamp(Instant.now());

        return trackingEventRepository.save(event);
    }

    public List<TrackingEvent> getTrackingHistory(String trackingNumber) {
        return trackingEventRepository
                .findByTrackingNumberOrderByTimestampAsc(trackingNumber);
    }
}