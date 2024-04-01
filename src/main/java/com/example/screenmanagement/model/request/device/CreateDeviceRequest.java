package com.example.screenmanagement.model.request.device;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateDeviceRequest {
    private String name;

    private String description;

    private int timeOffAgo;

    private String location;

    @NotNull
    private String identityDevice;
}
