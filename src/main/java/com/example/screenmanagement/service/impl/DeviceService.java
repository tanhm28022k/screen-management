package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.Device;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.service.iservice.IDeviceService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.SecurityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class DeviceService implements IDeviceService {
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    UserRepository userRepository;
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
            deviceRepository.save(device);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("save device error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse resetUpdateTime(String id) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            Device device = deviceRepository.findByIdentityDevice(id);
            if(device != null) {
                device.setUpdDatetime(Calendar.getInstance().getTime());
                deviceRepository.saveAndFlush(device);
            }
            return baseResponse;
        } catch (Exception ex) {
            logger.info("update updDatetime error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getMessage(), false, Constant.ERROR_CODE_MAP.SAVE_DEVICE_ERROR.getCode()));
            return baseResponse;
        }
    }

    public User getFromAuthentication(String username) {
        User user = userRepository.findFirstByUsername(username);
        return user;
    }
}
