package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String>, JpaSpecificationExecutor<Device> {
   List<Device> findByUserId(String userId);

   Device findByIdentityDevice(String id);
   @Query(value = "select * from device WHERE upd_datetime <= (NOW() - INTERVAL 10 SECOND)", nativeQuery = true)
   List<Device> getOfflineDevices();

   @Query(value = "select * from device WHERE upd_datetime > (NOW() - INTERVAL 10 SECOND) and available = false", nativeQuery = true)
   List<Device> getOnlineDevices();

}
