package com.example.screenmanagement.schedulejob;

import com.example.screenmanagement.entity.Device;
import com.example.screenmanagement.repository.DeviceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;

@Component
public class UpdateStatusDeviceJob {
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceRepository deviceRepository;
    @Scheduled(cron = "*/5 * * * * *")
    public void updateStatusDevice() {
        try {
            //check cac tbi ngat ket noi
            List<Device> offlineDevices = deviceRepository.getOfflineDevices();
            if(!CollectionUtils.isEmpty(offlineDevices)) {
                List<Device> ofDevices = offlineDevices.stream().map(device -> {
                    device.setAvailable(false);
                    long currentTime = Calendar.getInstance().getTime().getTime();
                    long updateTime = device.getUpdDatetime().getTime();
                    device.setTimeOffAgo(currentTime - updateTime);
                    return device;
                }).toList();
                deviceRepository.saveAllAndFlush(ofDevices);
            }
            //check cac thiet bị đã khôi phục kết nối
            List<Device> onlineDevices = deviceRepository.getOnlineDevices();
            if(!CollectionUtils.isEmpty(onlineDevices)) {
                List<Device> olDevices = onlineDevices.stream().map(device -> {
                    device.setAvailable(true);
                    device.setTimeOffAgo(0);
                    return device;
                }).toList();
                deviceRepository.saveAllAndFlush(olDevices);
            }
        } catch (Exception ex) {
            logger.info("Update status device : {} ", ex.getMessage());
        }
    }
}
