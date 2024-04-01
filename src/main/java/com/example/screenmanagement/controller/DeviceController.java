package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.UpdateDeviceDateRequest;
import com.example.screenmanagement.service.impl.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@CrossOrigin
public class DeviceController {
    @Autowired
    DeviceService deviceService;

    @PostMapping("/update-date-time")
    public ResponseEntity<BaseResponse> update(@RequestBody UpdateDeviceDateRequest request) {
        BaseResponse baseResponse = deviceService.resetUpdateTime(request.getId());
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/get-list")
    public ResponseEntity<BaseResponse> getList() {
        BaseResponse baseResponse = deviceService.getListByUserId();
        return ResponseEntity.ok(baseResponse);
    }
}
