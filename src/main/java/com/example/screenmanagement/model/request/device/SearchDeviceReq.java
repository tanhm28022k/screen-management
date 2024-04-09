package com.example.screenmanagement.model.request.device;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchDeviceReq {
    private int page = 0;
    private int size = 20;
    private String name;
    @NotBlank(message = "folderId can not blank")
    private String regionId;
}
