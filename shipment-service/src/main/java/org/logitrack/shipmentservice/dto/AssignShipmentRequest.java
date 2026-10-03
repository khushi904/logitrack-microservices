package org.logitrack.shipmentservice.dto;

import jakarta.validation.constraints.NotBlank;

public class AssignShipmentRequest {

    @NotBlank
    private String agentId;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}