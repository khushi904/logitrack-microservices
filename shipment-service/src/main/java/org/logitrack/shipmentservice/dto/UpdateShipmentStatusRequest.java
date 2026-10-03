package org.logitrack.shipmentservice.dto;

import jakarta.validation.constraints.NotNull;
import org.logitrack.shipmentservice.model.ShipmentStatus;

public class UpdateShipmentStatusRequest {

    @NotNull
    private ShipmentStatus status;

    private String description;

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}