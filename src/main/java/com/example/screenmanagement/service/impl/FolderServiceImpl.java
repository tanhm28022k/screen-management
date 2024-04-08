package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Folder;
import com.example.screenmanagement.model.request.folder.CreateFolderReq;
import com.example.screenmanagement.model.response.folder.FolderDto;
import com.example.screenmanagement.repository.FolderRepository;
import com.example.screenmanagement.service.iservice.FolderService;
import com.example.screenmanagement.utility.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;

    @Override
    public BaseResponse createFolder(CreateFolderReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Tạo region thành công"));
        try {
            if (!req.getParentId().isEmpty()) {
                Folder folder = folderRepository.findFirstById(req.getParentId());
                if (folder == null) {
                    throw new RuntimeException("Folder cha không tồn tại");
                }
            }
            log.info("start save folder...");
            Folder folder = Folder.builder()
                    .name(req.getName())
                    .description(req.getDescription())
                    .status(Constant.STATUS.ACTIVE.name())
                    .parentId(req.getParentId())
                    .build();
            Folder save = folderRepository.save(folder);
            baseResponse.setData(save);
        } catch (Exception ex) {
            log.info("create new folder has errors... {}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse listFolder() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Lấy danh sách thành công"));
        try {
            List<Folder> folders = folderRepository.findAll();

            List<FolderDto> parents = folders.stream().filter(folder -> Objects.isNull(folder.getParentId())).map(region -> FolderDto.builder()
                    .id(region.getId())
                    .name(region.getName())
                    .build()).toList();
            List<FolderDto> folderDtos = new ArrayList<>(parents);
            var collect = folders.stream().filter(region -> Objects.nonNull(region.getParentId()))
                    .map(x -> FolderDto.builder()
                            .id(x.getId())
                            .name(x.getName())
                            .parentId(x.getParentId())
                            .build())
                    .collect(Collectors.groupingBy(FolderDto::getParentId));
            for (FolderDto folderDto : folderDtos) {
                List<FolderDto> subs = collect.get(folderDto.getId());
                if (!CollectionUtils.isNullOrEmpty(subs)) {
                    folderDto.setSubs(subs);
                }
            }
            baseResponse.setData(folderDtos);
        } catch (Exception ex) {
            log.info("list folder has errors: {}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }
}
