package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Document;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;
import com.example.screenmanagement.model.response.device.DeviceDtoResponse;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.DocumentRepository;
import com.example.screenmanagement.repository.impl.BaseResultSelect;
import com.example.screenmanagement.service.iservice.IDocumentService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.JwtTokenUtil;
import com.example.screenmanagement.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.example.screenmanagement.utility.Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR;
import static com.example.screenmanagement.utility.Constant.ERROR_CODE_MAP.SEARCH_DEVICE_ERROR;

@Service
@RequiredArgsConstructor
public class DocumentService implements IDocumentService {
    @Value("${upload.directory}")
    private String uploadDirectory;
    Logger logger = LogManager.getLogger(this.getClass());

    private final DeviceRepository deviceRepository;
    private final DocumentRepository documentRepository;
    private final JwtTokenUtil tokenUtil;

    @Override
    public BaseResponse uploadDocument(List<MultipartFile> files) {
        long startTime = System.currentTimeMillis();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            List<Document> documents = new ArrayList<>();
            String userId = tokenUtil.getFromAuthentication().getId();
            files.forEach(multipartFile -> {
                validateFile(multipartFile);
                long endTime = System.currentTimeMillis();
                long timeUpload = endTime - startTime;
                String uploadFilePath = getUploadFilePath(multipartFile);
                if (uploadFilePath != null) {
                    Document document = new Document();
                    document.setName(StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename())));
                    document.setPath(uploadFilePath);
                    document.setSize(multipartFile.getSize());
                    document.setType(getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
                    document.setUploadedTime(timeUpload);
                    document.setUserId(userId);
                    documents.add(document);
                }
            });
            if (!CollectionUtils.isEmpty(documents)) {
                documentRepository.saveAllAndFlush(documents);
            }
        } catch (Exception ex) {
            logger.info("save files error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
        }
        return baseResponse;
    }

    private void validateFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        // Kiểm tra phần mở rộng của tệp
        String fileExtension = getFileExtension(fileName);
        if (!fileExtension.equalsIgnoreCase("mp4") &&
                !fileExtension.equalsIgnoreCase("jpg")) {
            throw new RuntimeException("Chỉ cho phép upload file MP4 hoặc JPG.");
        }
        // Kiểm tra tên tệp không chứa dấu
        if (!isValidFileName(fileName)) {
            throw new RuntimeException("Tên tệp không hợp lệ.");
        }
        // Kiểm tra xem tệp đã tồn tại trước đó chưa
        if (documentRepository.existsByName(fileName)) {
            throw new RuntimeException("Tệp đã tồn tại. Vui lòng đổi tên và thử lại.");
        }

    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private boolean isValidFileName(String fileName) {
        // Kiểm tra xem tên tệp không chứa dấu
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9-_\\.\\s]");
        return !pattern.matcher(fileName).find();
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
            logger.info("get document error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse getById(String id) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            Document document = documentRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy document với id: " + id));
            baseResponse.setData(document);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("get document error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse searchDocument(SearchDeviceReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
//        try {
//            String userId = tokenUtil.getFromAuthentication().getId();
//            BaseResultSelect baseResultSelect = deviceCustomRepository.searchData(req.getName(),
//                    req.getPage(), req.getSize(), fromAuthentication.getId(), req.getRegionId());
//            PageImpl<DeviceDtoResponse> deviceDtoResponses = new PageImpl<DeviceDtoResponse>(baseResultSelect.getListData(),
//                    PageRequest.of(req.getPage(), req.getSize()), baseResultSelect.getCount());
//            baseResponse.setData(deviceDtoResponses);
//        } catch (Exception ex) {
//            logger.info("searchDevice has errors...{}", ex.getMessage());
//            baseResponse.setResult(new Result(SEARCH_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
//        }
        return baseResponse;
    }

    public String getUploadFilePath(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String filePath = uploadDirectory + fileName;
            File dest = new File(filePath);
            file.transferTo(dest);
            // return "/upload/" + fileName;
            return filePath;
        } catch (IOException e) {
            logger.error("Upload file error: {} ", e.getMessage());
            return null;
        }
    }
}
