package com.example.screenmanagement.model.response.auth;

import com.example.screenmanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private User user;
}
