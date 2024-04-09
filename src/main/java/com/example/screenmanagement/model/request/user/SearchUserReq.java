package com.example.screenmanagement.model.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchUserReq {
    private int page = 0;
    private int size = 20;
    private String fullName;
    private String phoneNumber;
    private String userName;
}
