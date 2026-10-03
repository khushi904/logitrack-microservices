package org.logitrack.shipmentservice.controller;

import jakarta.validation.Valid;
import org.logitrack.shipmentservice.dto.AssignShipmentRequest;
import org.logitrack.shipmentservice.dto.CreateShipmentRequest;
import org.logitrack.shipmentservice.dto.UpdateShipmentStatusRequest;
import org.logitrack.shipmentservice.model.Shipment;
import org.logitrack.shipmentservice.model.TrackingEvent;
import org.logitrack.shipmentservice.service.ShipmentService;
import org.logitrack.shipmentservice.service.TrackingEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final TrackingEventService trackingEventService;

    public ShipmentController(ShipmentService shipmentService,TrackingEventService trackingEventService) {
        this.shipmentService = shipmentService;
        this.trackingEventService = trackingEventService;
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Shipment> createShipment(
            @Valid @RequestBody CreateShipmentRequest request,
            Authentication authentication) {

        String senderId = authentication.getName();

        Shipment shipment =
                shipmentService.createShipment(request, senderId);

        return ResponseEntity.ok(shipment);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'DELIVERY_AGENT')")
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Shipment> getShipment(
            @PathVariable String trackingNumber) {

        return ResponseEntity.ok(
                shipmentService.getByTrackingNumber(trackingNumber)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments(){
        return ResponseEntity.ok(
                shipmentService.getAllShipments());
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'DELIVERY_AGENT')")
    @GetMapping("/{trackingNumber}/tracking")
    public ResponseEntity<List<TrackingEvent>> getTrackingHistory(
            @PathVariable String trackingNumber) {

        return ResponseEntity.ok(
                trackingEventService.getTrackingHistory(trackingNumber)
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY_AGENT')")
    @PutMapping("/{trackingNumber}/status")
    public ResponseEntity<Shipment> updateStatus(
            @PathVariable String trackingNumber,
            @Valid @RequestBody UpdateShipmentStatusRequest request,
            Authentication authentication) {

        String userId = authentication.getName();
        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();

        Shipment shipment = shipmentService.updateStatus(
                trackingNumber,
                request,
                userId,
                role
        );

        return ResponseEntity.ok(shipment);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    @GetMapping("/my-shipment")
    public ResponseEntity<List<Shipment>> getMyShipments(
            Authentication authentication) {

        String senderId = authentication.getName();

        return ResponseEntity.ok(
                shipmentService.getShipmentsBySenderId(senderId)
        );
    }

    @GetMapping("/public/track/{trackingNumber}")
    public ResponseEntity<List<TrackingEvent>> publicTrackShipment(
            @PathVariable String trackingNumber) {

        return ResponseEntity.ok(
                trackingEventService.getTrackingHistory(trackingNumber)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{trackingNumber}/assign")
    public ResponseEntity<Shipment> assignShipment(
            @PathVariable String trackingNumber,
            @Valid @RequestBody AssignShipmentRequest request) {

        Shipment shipment = shipmentService.assignShipment(
                trackingNumber,
                request.getAgentId()
        );

        return ResponseEntity.ok(shipment);
    }

    @PreAuthorize("hasRole('DELIVERY_AGENT')")
    @GetMapping("/assigned")
    public ResponseEntity<List<Shipment>> getAssignedShipments(
            Authentication authentication) {

        String agentId = authentication.getName();

        return ResponseEntity.ok(
                shipmentService.getShipmentsByAgentId(agentId)
        );
    }

}
