package com.example.screenmanagement.model.request.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String password;
    private String username;
    private String email;
    private String gender;
    private String status;
    private String phoneNumber;
    private Date birthday;
    private String roles;
}
