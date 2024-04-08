package com.example.screenmanagement.service.impl;

import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.example.screenmanagement.entity.User;
import com.example.screenmanagement.entity.UserGroup;
import com.example.screenmanagement.model.request.auth.RegisterRequest;
import com.example.screenmanagement.model.request.user.CreateUserRequest;
import com.example.screenmanagement.model.request.user.SearchUserReq;
import com.example.screenmanagement.model.request.user.UpdateUserReq;
import com.example.screenmanagement.repository.GroupRepository;
import com.example.screenmanagement.repository.UserGroupRepository;
import com.example.screenmanagement.repository.UserRepository;
import com.example.screenmanagement.repository.custom.UserRepositoryCustom;
import com.example.screenmanagement.repository.impl.BaseResultSelect;
import com.example.screenmanagement.service.iservice.IUserService;
import com.example.screenmanagement.utility.Constant;
import com.example.screenmanagement.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, IUserService {
    Logger logger = LogManager.getLogger(this.getClass());

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final UserGroupRepository userGroupRepository;

    private final UserRepositoryCustom userRepositoryCustom;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findFirstByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(), getAuthorities(getRoles(user)));
        } catch (UsernameNotFoundException exception) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }

    public List<String> getRoles(User user) {
        if (StringUtils.isNotBlank(user.getRoles())) {
            return Arrays.stream(user.getRoles().split(", ")).toList();
        }
        return Collections.emptyList();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public BaseResponse getList() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            List<User> all = userRepository.findAll();
            baseResponse.setData(all);
            return baseResponse;
        } catch (Exception ex) {
            logger.info("get all users error : {} ", ex.getMessage());
            baseResponse.setResult(new Result(Constant.ERROR_CODE_MAP.SUCESS.getMessage(), false, Constant.ERROR_CODE_MAP.SUCESS.getCode()));
            return baseResponse;
        }
    }

    @Override
    public BaseResponse createUser(CreateUserRequest req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Tạo user thành công"));
        try {
            User user = new User();
            validateUserCreation(req);
            BeanUtils.copyProperties(req, user);
            user.setName(req.getFullName());
            user.setRoles(req.getRole());
            user.setPassword(SecurityUtils.encodePassword(req.getPassword()));
            User savedUser = userRepository.save(user);

            logger.info("start save user_groups...");
            UserGroup userGroup = UserGroup.builder()
                    .groupId(req.getGroupId())
                    .userId(savedUser.getId())
                    .build();
            userGroupRepository.save(userGroup);

        } catch (Exception e) {
            logger.info("Error creating user: {} ", e.getMessage());
            baseResponse.setResult(new Result(e.getMessage(), false, "Lỗi hệ thống"));
        }

        return baseResponse;
    }

    @Override
    public BaseResponse updateUser(UpdateUserReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Update user successfully!"));
        try {
            logger.info("start update user...");
            User user = userRepository.findById(req.getId()).orElseThrow(() -> new RuntimeException("User not found"));
            BeanUtils.copyProperties(req, user);
            User save = userRepository.save(user);
            baseResponse.setData(save);
        } catch (Exception e) {
            logger.info("Error update user: {} ", e.getMessage());
            baseResponse.setResult(new Result(e.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }

    @Override
    public BaseResponse getDetailUser(String id) {
        BaseResponse baseResponse = new BaseResponse();
        userRepository.findById(id).ifPresentOrElse(device -> {
            baseResponse.setResult(Result.OK("Success"));
            baseResponse.setData(device);
        }, () -> baseResponse.setResult(new Result("Không tồn user thiết bị có id:" + id,
                false, Constant.ERROR_CODE_MAP.DETAIL_DEVICE_ERROR.getCode())));
        return baseResponse;
    }

    @Override
    public BaseResponse search(SearchUserReq req) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(Result.OK("Success"));
        try {
            BaseResultSelect result = userRepositoryCustom.searchUser(req);
            PageImpl<User> deviceDtoResponses = new PageImpl<>(result.getListData(),
                    PageRequest.of(req.getPage(), req.getSize()), result.getCount());
            baseResponse.setData(deviceDtoResponses);
        } catch (Exception ex) {
            logger.info("Search user has errors...{}", ex.getMessage());
            baseResponse.setResult(new Result(ex.getMessage(), false, "Lỗi hệ thống"));
        }
        return baseResponse;
    }

    public BaseResponse save(RegisterRequest registerRequest) {
        BaseResponse response = new BaseResponse();
        try {
            User user = new User();
            user.setMail(registerRequest.getEmail());
            user.setUsername(registerRequest.getUsername());
            user.setPassword(SecurityUtils.encodePassword(registerRequest.getPassword()));
            user.setName(registerRequest.getName());
            user.setPhoneNumb(registerRequest.getPhoneNumber());
            user.setBirthday(registerRequest.getBirthday());
            user.setRoles(registerRequest.getRoles());
            user.setGender(Constant.GENDER.valueOf("MALE"));
            user.setStatus("ACTIVE");
            Object userData = userRepository.save(user);

            response.setResult(Result.OK("Đăng kí tài khoản thành công"));
            response.setData(userData);
        } catch (Exception ex) {
            response.setResult(new Result("An error occurred when saving the user", false, HttpStatus.BAD_REQUEST.toString()));
        }
        return response;
    }

    private void validateUserCreation(CreateUserRequest req) {
        // Kiểm tra xem group có tồn tại không
        groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new RuntimeException("Tên group không tồn tại"));

        // Kiểm tra xem email đã tồn tại chưa
        if (Boolean.TRUE.equals(userRepository.existsByMail(req.getMail().toLowerCase()))) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Kiểm tra xem số điện thoại đã tồn tại chưa
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumb(req.getPhoneNumb()))) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }
    }
}
