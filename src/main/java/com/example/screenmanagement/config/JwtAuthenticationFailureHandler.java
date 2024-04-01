package com.example.screenmanagement.config;


import com.example.screenmanagement.common.BaseResponse;
import com.example.screenmanagement.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(new Result("Authentication failed: " + exception.getMessage(), false, String.valueOf(HttpStatus.UNAUTHORIZED.value())));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), baseResponse);
    }
}
