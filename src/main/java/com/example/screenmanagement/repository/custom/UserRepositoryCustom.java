package com.example.screenmanagement.repository.custom;

import com.example.screenmanagement.model.request.user.SearchUserReq;
import com.example.screenmanagement.repository.impl.BaseResultSelect;

public interface UserRepositoryCustom {
    BaseResultSelect searchUser(SearchUserReq req);
}
