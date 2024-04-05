package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.InfoDeviceRequest;
import com.example.screenmanagement.model.request.device.MoveDeviceReq;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;
import com.example.screenmanagement.model.request.device.UpdateDeviceDateRequest;
import com.example.screenmanagement.service.impl.DeviceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @PostMapping("/send-info-device")
    public ResponseEntity<BaseResponse> sendInfoDevice(@RequestBody InfoDeviceRequest request){
        BaseResponse baseResponse = deviceService.sendInfoDevice(request);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<BaseResponse> search(@RequestBody @Valid SearchDeviceReq req) {
        BaseResponse baseResponse = deviceService.searchDevice(req);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> detailDevice(@PathVariable String id) {
        BaseResponse baseResponse = deviceService.getDetailDevice(id);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/move")
    public ResponseEntity<BaseResponse> moveDevice(@RequestBody @Valid MoveDeviceReq req) {
        BaseResponse baseResponse = deviceService.moveDevices(req);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse> deleteByIds(@RequestParam("ids")  List<String> deviceIds){
        BaseResponse baseResponse = deviceService.deleteDevices(deviceIds);
        return ResponseEntity.ok(baseResponse);
    }

}
