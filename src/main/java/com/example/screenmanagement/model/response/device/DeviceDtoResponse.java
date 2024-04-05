package com.example.screenmanagement.model.response.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDtoResponse {
    private String id;
    private Boolean available;
    private String description;
    private String identityDevice;
    private String location;
    private String name;
    private String status;
    private String timeOffAgo;
    private String model;
    private Boolean isPresenting;
    private String isPresentingSchedule;
    private String lastActiveTime;
    private String lastDisconnectedTime;
    private String serialNumber;
    private String manufacturer;
    private String appVersion;
    private String osVersion;
}
