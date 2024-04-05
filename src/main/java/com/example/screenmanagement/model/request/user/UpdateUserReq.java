package com.example.screenmanagement.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserReq {
    @NotBlank(message = "Id can not blank")
    private String id;
    private String fullName;
    @Email(message = "Invalid email format")
    private String mail;
    private String phoneNumb;
    private String role;
    private String password;
    private String gender;
    private Date birthday;
    private String groupId;
}
