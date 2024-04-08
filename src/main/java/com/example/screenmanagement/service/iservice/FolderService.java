package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.folder.CreateFolderReq;

public interface FolderService {
    BaseResponse createFolder(CreateFolderReq request);

    BaseResponse listFolder();
}
