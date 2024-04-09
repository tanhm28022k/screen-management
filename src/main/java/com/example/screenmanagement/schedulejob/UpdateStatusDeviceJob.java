package com.example.screenmanagement.schedulejob;

import com.example.screenmanagement.config.MyStompSessionHandler;
import com.example.screenmanagement.entity.Device;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.service.impl.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class UpdateStatusDeviceJob {
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserService userService;
    @Scheduled(cron = "*/5 * * * * *")
    public void updateStatusDevice() {
        try {
            //check cac tbi ngat ket noi
            checkOfflineDevice();
            //check cac thiet bị đã khôi phục kết nối
            checkOnlineDevice();
            //push danh sách tbi sau khi được cập nhat
            pushLatestDeviceStatus();
        } catch (Exception ex) {
            logger.info("Update status device : {} ", ex.getMessage());
        }
    }
    public void checkOfflineDevice() {
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
    }
    public void checkOnlineDevice() {
        List<Device> onlineDevices = deviceRepository.getOnlineDevices();
        if(!CollectionUtils.isEmpty(onlineDevices)) {
            List<Device> olDevices = onlineDevices.stream().map(device -> {
                device.setAvailable(true);
                device.setTimeOffAgo(0);
                return device;
            }).toList();
            deviceRepository.saveAllAndFlush(olDevices);
        }
    }

    public void pushLatestDeviceStatus() throws ExecutionException, InterruptedException, JsonProcessingException {
        List<Device> latestStatusDevices = deviceRepository.findAll();
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new StringMessageConverter());

        StompSessionHandler sessionHandler = new MyStompSessionHandler();
//        StompSession session = stompClient.connect("ws://167.71.198.237:8686/management", sessionHandler).get();
        StompSession session = stompClient.connect("ws://localhost:8686/management", sessionHandler).get();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(latestStatusDevices);
        try {
            session.send("/app/sendData", jsonString);
        } catch (Exception ex) {
            logger.error("Error send data : {} ", ex.getMessage());
        }
        session.disconnect();
    }
}
