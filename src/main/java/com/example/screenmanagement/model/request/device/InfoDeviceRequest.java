package com.example.screenmanagement.model.request.device;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoDeviceRequest {
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
