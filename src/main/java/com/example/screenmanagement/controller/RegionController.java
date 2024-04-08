package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.region.CreateRegionReq;
import com.example.screenmanagement.service.iservice.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/region")
@CrossOrigin
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> update(@RequestBody @Valid CreateRegionReq request) {
        BaseResponse baseResponse = regionService.createRegion(request);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list() {
        BaseResponse baseResponse = regionService.listRegion();
        return ResponseEntity.ok(baseResponse);
    }

}
