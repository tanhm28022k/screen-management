package com.example.screenmanagement.service.iservice;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.model.request.user.CreateUserRequest;
import com.example.screenmanagement.model.request.user.SearchUserReq;
import com.example.screenmanagement.model.request.user.UpdateUserReq;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    BaseResponse getList();

    BaseResponse createUser(CreateUserRequest createUserRequest);

    BaseResponse updateUser(UpdateUserReq req);

    BaseResponse getDetailUser(String id);

    BaseResponse search(SearchUserReq req);

}
