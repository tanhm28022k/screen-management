package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.folder.CreateFolderReq;
import com.example.screenmanagement.model.request.region.CreateRegionReq;
import com.example.screenmanagement.service.iservice.FolderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folder")
@CrossOrigin
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> update(@RequestBody @Valid CreateFolderReq request) {
        BaseResponse baseResponse = folderService.createFolder(request);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse> list() {
        BaseResponse baseResponse = folderService.listFolder();
        return ResponseEntity.ok(baseResponse);
    }


}
