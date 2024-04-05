package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {
    Region findFirstById(String id);

}
