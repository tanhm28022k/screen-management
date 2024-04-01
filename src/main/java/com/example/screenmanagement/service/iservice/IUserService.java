package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.user.CreateUserRequest;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    BaseResponse getList();

    BaseResponse createUser(CreateUserRequest createUserRequest);

}
