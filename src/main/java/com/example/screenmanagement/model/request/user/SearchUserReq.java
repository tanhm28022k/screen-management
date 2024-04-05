package com.example.screenmanagement.model.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SearchUserReq {
    @NotNull
    private int page = 0;
    @NotNull
    private int size = 20;
    private String fullName;
    private String phoneNumber;
    private String userName;
}
