package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.GroupOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupOrg, String>, JpaSpecificationExecutor<GroupOrg> {

    Optional<GroupOrg> findById(String id);

}
