package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Device;
import com.example.screenmanagement.entity.Document;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.DocumentRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.service.iservice.IDeviceService;
import com.example.screenmanagement.service.iservice.IDocumentService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService implements IDocumentService {
    @Value("${upload.directory}")
    private String uploadDirectory;
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DocumentRepository documentRepository;

    @Override
    public BaseResponse uploadDocument(List<MultipartFile> files) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            List<Document> documents = new ArrayList<>();
            files.forEach(multipartFile -> {
                String uploadFilePath = getUploadFilePath(multipartFile);
                if(uploadFilePath != null) {
                    Document document = new Document();
                    document.setName(multipartFile.getOriginalFilename());
                    document.setPath(uploadFilePath);
                    documents.add(document);
                }
            });
            if(!CollectionUtils.isEmpty(documents)) {
                documentRepository.saveAllAndFlush(documents);
            }
        } catch (Exception ex) {
            logger.info("save files error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
        }

        return baseResponse;
    }

    @Override
    public BaseResponse getList() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            List<Document> documents = documentRepository.findAll();
            baseResponse.setData(documents);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("get document error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    public String getUploadFilePath(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return "/upload/" + fileName;
        } catch (IOException e) {
            logger.error("Upload file error: {} ", e.getMessage());
            return null;
        }
    }
}
