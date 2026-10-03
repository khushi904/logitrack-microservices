package org.logitrack.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateProfileRequest {

//    @NotBlank(message = "Name is required")
    private String name;

    private String phoneNumber;
    public UserUpdateProfileRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}