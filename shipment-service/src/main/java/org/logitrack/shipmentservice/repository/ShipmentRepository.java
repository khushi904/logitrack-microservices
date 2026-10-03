package org.logitrack.shipmentservice.repository;

import org.logitrack.shipmentservice.model.Shipment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends MongoRepository<Shipment, String> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    List<Shipment> findBySenderId(String senderId);
    List<Shipment> findByAssignedAgentId(String assignedAgentId);
}