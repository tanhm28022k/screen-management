package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;
import com.example.screenmanagement.service.impl.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/document")
@CrossOrigin
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @PostMapping()
    public ResponseEntity<BaseResponse> uploadDocument(@RequestPart(value = "files") List<MultipartFile> files) {
        BaseResponse baseResponse = documentService.uploadDocument(files);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping()
    public ResponseEntity<BaseResponse> getAll() {
        BaseResponse baseResponse = documentService.getList();
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getById(@PathVariable String id) {
        BaseResponse baseResponse = documentService.getById(id);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/search")
    public ResponseEntity<BaseResponse> search(@RequestBody @Valid SearchDeviceReq req) {
        BaseResponse baseResponse = documentService.searchDocument(req);
        return ResponseEntity.ok(baseResponse);
    }

}
