package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentService {
    BaseResponse uploadDocument(List<MultipartFile> files);

    BaseResponse getList();

    BaseResponse getById(String id);


    BaseResponse searchDocument(SearchDeviceReq req);
}
