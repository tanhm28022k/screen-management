package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.RegionDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionDeviceRepository extends JpaRepository<RegionDevice,String> {

    Optional<RegionDevice> findFirstByDeviceIdAndRegionId(String deviceId, String regionId);

}
