package com.example.screenmanagement.model.request.device;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeviceDateRequest {
    @NotNull
    private String id;
}
