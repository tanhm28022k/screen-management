package com.example.screenmanagement.controller;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.model.request.auth.LoginRequest;
import com.example.screenmanagement.model.request.auth.RegisterRequest;
import com.example.screenmanagement.model.request.device.CreateDeviceRequest;
import com.example.screenmanagement.model.response.auth.AuthResponse;
import com.example.screenmanagement.model.response.user.UserDetailAuthResponse;
import com.example.screenmanagement.repository.DeviceRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.service.impl.DeviceService;
import com.example.screenmanagement.service.impl.UserService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.Objects;

@RestController
@RequestMapping("/auth")
@Data
@CrossOrigin
public class AuthController {
    Logger logger = LogManager.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceService deviceService;
    @PostMapping(value = "/login")
    public ResponseEntity<BaseResponse> login(@RequestBody @Valid LoginRequest request) {

        BaseResponse response = authenticate(request.getUsername().toLowerCase(), request.getPassword(), request.getCreateDeviceData());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    ResponseEntity<?> saveUser(@RequestBody RegisterRequest registerRequest) throws Exception {
        return ResponseEntity.ok(userService.save(registerRequest));
    }


    private BaseResponse authenticate(String username, String password, CreateDeviceRequest createDeviceData) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            User userLogin = userRepository.findFirstByUsername(username.toLowerCase());
            if (userLogin != null && Objects.equals(userLogin.getStatus(), Constant.STATUS.DEACTIVE.name())) {
                baseResponse.setResult(new Result("Tài khoản không tồn tại", false, HttpStatus.UNAUTHORIZED.toString()));
                return baseResponse;
            }

            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password);
            Authentication authenticationResponse =
                    this.authenticationManager.authenticate(authenticationRequest);
            UserDetails userDetails = (UserDetails) authenticationResponse.getPrincipal();

            String token = jwtTokenUtil.generateToken(userDetails);
            //lưu thong tin device user dang nhap
            if(createDeviceData != null) {
                deviceService.saveDevice(createDeviceData, username);
            }else {
                logger.info("khong co thong tin device: {} ", createDeviceData);
            }
            AuthResponse authResponse = new AuthResponse(token, userLogin);
            baseResponse.setResult(Result.OK("Đăng nhập thành công"));
            baseResponse.setData(authResponse);
        } catch (Exception ex) {
            baseResponse.setResult(new Result("Tên đăng nhập hoặc mật khẩu không chính xác", false, HttpStatus.UNAUTHORIZED.toString()));
        }
        return baseResponse;
    }

}
