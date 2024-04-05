package com.example.screenmanagement.repository.custom;

import com.example.screenmanagement.repository.impl.BaseResultSelect;

public interface DeviceCustomRepository {
    BaseResultSelect searchData(String name, Integer page, Integer size, String userId, String regionId);
}
