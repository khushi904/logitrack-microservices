package org.logitrack.shipmentservice.repository;

import org.logitrack.shipmentservice.model.TrackingEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrackingEventRepository extends MongoRepository<TrackingEvent, String> {

    List<TrackingEvent> findByTrackingNumberOrderByTimestampAsc(String trackingNumber);
}