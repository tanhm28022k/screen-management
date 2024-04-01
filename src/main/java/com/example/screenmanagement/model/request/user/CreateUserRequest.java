package com.example.screenmanagement.model.request.user;

import com.example.screenmanagement.utility.Constant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @NotNull(message = "Name is required")
    private String fullName;

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Mail is required")
    @Email(message = "Invalid email format")
    private String mail;

    @NotNull(message = "Phone number is required")
    private String phoneNumb;

    @NotNull(message = "role is required")
    private String role;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Gender is required")
    private Constant.GENDER gender;

    @NotNull(message = "Birthday is required")
    private Date birthday;


}
