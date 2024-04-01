package com.example.screenmanagement.repository;

import com.example.screenmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findFirstByUsername(String username);

    Boolean existsByMail(String email);

    Boolean existsByPhoneNumb(String phoneNum);
}
