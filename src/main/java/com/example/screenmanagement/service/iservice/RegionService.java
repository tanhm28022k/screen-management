package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.region.CreateRegionReq;

public interface RegionService {
    BaseResponse createRegion(CreateRegionReq req);

    BaseResponse listRegion();
}
