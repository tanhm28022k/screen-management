package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.user.CreateUserRequest;
import com.example.screenmanagement.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse> getAllUsers() {
        BaseResponse response = userService.getList();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse> createUser(@RequestPart("userData") @Valid CreateUserRequest req) {
        BaseResponse response = userService.createUser(req);
        return ResponseEntity.ok(response);
    }
}
