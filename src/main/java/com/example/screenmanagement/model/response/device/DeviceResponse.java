package com.example.screenmanagement.model.response.device;


import com.example.screenmanagement.utility.Constant;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceResponse {
    private String id;

    private String name;

    private String description;

    private String status = Constant.STATUS.ACTIVE.name();

    private Boolean available;

    private String timeOffAgo;

    private String location;

    private String identityDevice;

    private String userId;

    private Date insDatetime;

    private Date updDatetime;
}
