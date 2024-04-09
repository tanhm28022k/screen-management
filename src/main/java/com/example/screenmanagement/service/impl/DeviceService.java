package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Device;
import com.example.screenmanagement.entity.RegionDevice;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.model.request.device.InfoDeviceRequest;
import com.example.screenmanagement.model.request.device.MoveDeviceReq;
import com.example.screenmanagement.model.request.device.SearchDeviceReq;
import com.example.screenmanagement.model.response.device.DeviceDtoResponse;
import com.example.screenmanagement.model.response.device.DeviceResponse;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.RegionDeviceRepository;
import com.example.screenmanagement.repository.RegionRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.repository.custom.DeviceCustomRepository;
import com.example.screenmanagement.repository.impl.BaseResultSelect;
import com.example.screenmanagement.service.iservice.IDeviceService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.screenmanagement.utility.Constant.DefaultValue.REGION_DEFAULT_INIT_SAVE_DEVICE;
import static com.example.screenmanagement.utility.Constant.ERROR_CODE_MAP.*;

@Service
public class DeviceService implements IDeviceService {
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private DeviceCustomRepository deviceCustomRepository;

    @Autowired
    private RegionDeviceRepository regionDeviceRepository;

    @Autowired
    private RegionRepository repository;

    @Autowired
    UserService userService;
    @Override
    public BaseResponse getListByUserId() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            User fromAuthentication = userService.getFromAuthentication(SecurityUtils.getAuthenticatedUserDetails().getUsername());
            List<Device> devices = deviceRepository.findByUserId(fromAuthentication.getId());
            List<DeviceResponse> list = devices.stream().map(device -> {
                DeviceResponse deviceResponse = new DeviceResponse();
                BeanUtils.copyProperties(device, deviceResponse);
                deviceResponse.setTimeOffAgo(formatTime(device.getTimeOffAgo()));
                return deviceResponse;
            }).toList();
            baseResponse.setData(list);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("save device error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse saveDevice(CreateDeviceRequest createDeviceRequest, String username) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            User fromAuthentication = getFromAuthentication(username);
            Device device = new Device();
            BeanUtils.copyProperties(createDeviceRequest, device);
            device.setUserId(fromAuthentication.getId());
            device.setUpdDatetime(Calendar.getInstance().getTime());
            Device savedDevice = deviceRepository.save(device);
            logger.info("start save region, region_device...");

            RegionDevice regionCommon = regionDeviceRepository.findById(REGION_DEFAULT_INIT_SAVE_DEVICE)
                    .orElseThrow(() -> new RuntimeException("Không tồn tại khu vực kho lưu trữ chung các thiết bị"));
            RegionDevice regionDevice = RegionDevice.builder()
                    .deviceId(savedDevice.getId())
                    .regionId(regionCommon.getId())
                    .build();
            regionDeviceRepository.save(regionDevice);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("save device error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(SAVE_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse resetUpdateTime(String id) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            Device device = deviceRepository.findByIdentityDevice(id);
            if (device != null) {
                device.setUpdDatetime(Calendar.getInstance().getTime());
                deviceRepository.saveAndFlush(device);
            }
            return baseResponse;
        } catch (Exception ex) {
            logger.info("update updDatetime error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(SAVE_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse sendInfoDevice(InfoDeviceRequest req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            User fromAuthentication = getFromAuthentication(SecurityUtils.getAuthenticatedUserDetails().getUsername());
            Device device = new Device();
            device.setUserId(fromAuthentication.getId());
            BeanUtils.copyProperties(req, device);
            device.setUpdDatetime(Calendar.getInstance().getTime());
            Device save = deviceRepository.save(device);
            RegionDevice regionCommon = regionDeviceRepository.findById(REGION_DEFAULT_INIT_SAVE_DEVICE)
                    .orElseThrow(() -> new RuntimeException("Không tồn tại khu vực kho lưu trữ chung các thiết bị"));
            RegionDevice regionDevice = RegionDevice.builder()
                    .deviceId(save.getId())
                    .regionId(regionCommon.getId())
                    .build();
            regionDeviceRepository.save(regionDevice);
            baseResponse.setData(save);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("send info device has errors...{}", ex.getMessage());
            logger.info("get document error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return null;
    }

    @Override
    public BaseResponse searchDevice(SearchDeviceReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            User fromAuthentication = getFromAuthentication(SecurityUtils.getAuthenticatedUserDetails().getUsername());
            BaseResultSelect baseResultSelect = deviceCustomRepository.searchData(req.getName(),
                    req.getPage(), req.getSize(), fromAuthentication.getId(), req.getRegionId());
            PageImpl<DeviceDtoResponse> deviceDtoResponses = new PageImpl<>(baseResultSelect.getListData(),
                    PageRequest.of(req.getPage(), req.getSize()), baseResultSelect.getCount());
            baseResponse.setData(deviceDtoResponses);
        } catch (Exception ex) {
            logger.info("searchDevice has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result(SEARCH_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse getDetailDevice(String id) {
        BaseResponse baseResponse = new BaseResponse();
        deviceRepository.findById(id).ifPresentOrElse(device -> {
            baseResponse.setResult(Result.OK("Success"));
            baseResponse.setData(device);
        }, () -> baseResponse.setResult(new Result("Không tồn tại thiết bị có id:" + id, false, DETAIL_DEVICE_ERROR.getCode())));
        return baseResponse;
    }

    @Override
    public BaseResponse deleteDevices(List<String> ids) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            logger.info("start delete devices...");
            List<Device> devices = deviceRepository.findAllById(ids);
            if (!CollectionUtils.isEmpty(devices) && (devices.size() != ids.size())) {
                throw new RuntimeException(MessageFormat.format("Một số thiết bị không tồn tại: {0}", ids));
            }
            devices.forEach(device -> device.setIsDeleted(Boolean.TRUE));
            deviceRepository.saveAll(devices);
        } catch (Exception ex) {
            logger.info("delete devices has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result("Xóa thiết bị lỗi ids: {}" + ids,
                    false, DELETE_DEVICE_ERROR.getCode()));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse moveDevices(List<MoveDeviceReq> reqs) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            logger.info("start log moveDevices...");
            List<String> deviceIds = reqs.stream().map(MoveDeviceReq::getDeviceId).toList();
            List<Device> devices = deviceRepository.findAllById(deviceIds);
            if (!CollectionUtils.isEmpty(devices) || devices.size() != deviceIds.size()) {
                throw new RuntimeException("Devices cannot exist");
            }
            List<RegionDevice> updates = new ArrayList<>();
            reqs.forEach(req -> regionDeviceRepository.findFirstByDeviceIdAndRegionId(req.getDeviceId(), req.getNewRegionId())
                    .ifPresentOrElse(regionDevice -> {
                        regionDevice.setRegionId(req.getNewRegionId());
                        updates.add(regionDevice);
                    }, () -> {
                        throw new RuntimeException("Cannot move this device");
                    }));
            if (!CollectionUtils.isEmpty(updates)) {
                regionDeviceRepository.saveAll(updates);
            }
        } catch (Exception ex) {
            logger.info("move device has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }

    public User getFromAuthentication(String username) {
        return userRepository.findFirstByUsername(username);
    }

    public String formatTime(long milisecond) {
        long seconds = milisecond/1000;
        if (seconds < 60) {
            return seconds + " seconds ago";
        }

        long minutes = seconds / 60;
        if (minutes < 60) {
            return minutes + " minutes ago";
        }

        long hours = minutes /60;
        if (hours < 24) {
            return hours + " hours ago";
        }

        long days = hours / 24;
        return days + " days ago";
    }
}
