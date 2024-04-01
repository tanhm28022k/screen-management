package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.model.request.user.CreateUserRequest;

public interface IDeviceService {
    BaseResponse getListByUserId();

    BaseResponse saveDevice(CreateDeviceRequest createDeviceRequest, String username);

    BaseResponse resetUpdateTime(String id);
}
