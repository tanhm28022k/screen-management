package com.example.screenmanagement.model.request.auth;

import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty
    protected String password;
    @NotEmpty
    protected String username;

    CreateDeviceRequest createDeviceData;
}
