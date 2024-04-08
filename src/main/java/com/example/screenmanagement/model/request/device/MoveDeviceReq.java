package com.example.screenmanagement.model.request.device;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveDeviceReq {
    @NotBlank(message = "DeviceId can not blank")
    private String deviceId;
    @NotBlank(message = "oldFolderId can not blank")
    private String oldRegionId;
    @NotBlank(message = "newFolderId can not blank")
    private String newRegionId;
}
