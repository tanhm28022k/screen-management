package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Region;
import com.example.screenmanagement.model.request.region.CreateRegionReq;
import com.example.screenmanagement.model.response.region.RegionDto;
import com.example.screenmanagement.repository.RegionRepository;
import com.example.screenmanagement.service.iservice.RegionService;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;


    @Override
    public BaseResponse createRegion(CreateRegionReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Tạo region thành công"));
        try {
            if (!req.getParentId().isEmpty()) {
                Region regionParent = regionRepository.findFirstById(req.getParentId());
                if (regionParent == null) {
                    throw new RuntimeException("Region cha không tồn tại");
                }
            }
            log.info("start save region...");
            Region region = Region.builder()
                    .name(req.getName())
                    .description(req.getDescription())
                    .status(Constant.STATUS.ACTIVE.name())
                    .parentId(req.getParentId())
                    .build();
            Region save = regionRepository.save(region);
            baseResponse.setData(save);
        } catch (Exception ex) {
            log.info("create new region has errors... {}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse listRegion() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Lấy danh sách thành công"));
        try {
            List<Region> regions = regionRepository.findAll();

            List<RegionDto> parents = regions.stream().filter(region -> Objects.isNull(region.getParentId())).map(region -> RegionDto.builder()
                    .id(region.getId())
                    .name(region.getName())
                    .build()).toList();
            List<RegionDto> regionDtoList = new ArrayList<>(parents);
            var collect = regions.stream().filter(region -> Objects.nonNull(region.getParentId()))
                    .map(x -> RegionDto.builder()
                            .id(x.getId())
                            .name(x.getName())
                            .parentId(x.getParentId())
                            .build())
                    .collect(Collectors.groupingBy(RegionDto::getParentId));
            for (RegionDto regionDto : regionDtoList) {
                List<RegionDto> subs = collect.get(regionDto.getId());
                if (!CollectionUtils.isNullOrEmpty(subs)) {
                    regionDto.setSubs(subs);
                }
            }
            baseResponse.setData(regionDtoList);
        } catch (Exception ex) {
            log.info("list region has errors: {}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }
}
