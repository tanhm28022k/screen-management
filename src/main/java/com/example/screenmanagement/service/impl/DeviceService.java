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
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.RegionDeviceRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.repository.custom.DeviceCustomRepository;
import com.example.screenmanagement.repository.impl.BaseResultSelect;
import com.example.screenmanagement.service.iservice.IDeviceService;
import com.example.screenmanagement.utility.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

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

    @Override
    public BaseResponse getListByUserId() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            User fromAuthentication = getFromAuthentication(SecurityUtils.getAuthenticatedUserDetails().getUsername());
            List<Device> devices = deviceRepository.findByUserId(fromAuthentication.getId());
            baseResponse.setData(devices);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("save device error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(SAVE_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
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
            deviceRepository.save(device);
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
            baseResponse.setData(save);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("send info device has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result(SAVE_DEVICE_ERROR.getMessage(), false, SAVE_DEVICE_ERROR.getCode()));
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
                    req.getPage(), req.getSize(), fromAuthentication.getId(),req.getRegionId());
            PageImpl<DeviceDtoResponse> deviceDtoResponses = new PageImpl<DeviceDtoResponse>(baseResultSelect.getListData(),
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
        }, () -> {
            baseResponse.setResult(new Result("Không tồn tại thiết bị có id:" + id,
                    false, DETAIL_DEVICE_ERROR.getCode()));
        });
        return baseResponse;
    }

    @Override
    public BaseResponse deleteDevices(List<String> ids) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            deviceRepository.deleteAllById(ids);
        } catch (Exception ex) {
            logger.info("delete devices has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result("Xóa thiết bị lỗi ids: {}" + ids,
                    false, DELETE_DEVICE_ERROR.getCode()));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse moveDevices(MoveDeviceReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            Optional<Device> device = deviceRepository.findById(req.getDeviceId());
            if (device.isEmpty()) {
                throw new RuntimeException("Device cannot exist");
            }
            RegionDevice regionDevice = regionDeviceRepository.findFirstByDeviceIdAndRegionId(req.getDeviceId(), req.getNewRegionId())
                    .orElseThrow(() -> new RuntimeException("Can not move this device"));
            regionDevice.setRegionId(req.getNewRegionId());
            RegionDevice save = regionDeviceRepository.save(regionDevice);
            baseResponse.setData(save);
        } catch (Exception ex) {
            logger.info("move device has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result("move device has errors",
                    false, MOVE_DEVICE_ERROR.getCode()));
        }
        return baseResponse;
    }

    public User getFromAuthentication(String username) {
        User user = userRepository.findFirstByUsername(username);
        return user;
    }
}
