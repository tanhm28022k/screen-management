package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.model.request.device.InfoDeviceRequest;
import com.example.screenmanagement.model.request.device.MoveDeviceReq;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;

import java.util.List;

public interface IDeviceService {
    BaseResponse getListByUserId();

    BaseResponse saveDevice(CreateDeviceRequest createDeviceRequest, String username);

    BaseResponse resetUpdateTime(String id);

    BaseResponse sendInfoDevice(InfoDeviceRequest req);

    BaseResponse searchDevice(SearchDeviceReq req);

    BaseResponse getDetailDevice(String id);

    BaseResponse deleteDevices(List<String> ids);

    BaseResponse moveDevices(List<MoveDeviceReq> req);
}
