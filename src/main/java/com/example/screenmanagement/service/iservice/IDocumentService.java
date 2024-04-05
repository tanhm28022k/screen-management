package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentService {
    BaseResponse uploadDocument(List<MultipartFile> files);

    BaseResponse getList();

    BaseResponse getById(String id);


}
