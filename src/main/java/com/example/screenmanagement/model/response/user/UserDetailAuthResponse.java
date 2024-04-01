package com.example.screenmanagement.model.response.user;

import com.example.screenmanagement.utility.Constant;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDetailAuthResponse {
    private String id;

    private String name;

    private String username;

    private Constant.GENDER gender;

    private String email;

    private String phoneNumb;

    private Date birthday;

    private  List<String> roles;
}
