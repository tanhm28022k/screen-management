package com.example.screenmanagement.config;


import com.example.screenmanagement.service.impl.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.screenmanagement.utility.JwtTokenUtil;

import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractBearTokenFromHeader(request);
        String username = null;
        if (token != null) {
            try {
                username = jwtTokenUtil.extractUsernameFromToken(token);
            } catch (ExpiredJwtException ex) {
                // JWT hết hạn
                authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("JWT has expired"));

            } catch (MalformedJwtException ex) {
                // JWT không hợp lệ
                authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid JWT"));

            } catch (Exception e) {
                // Các trường hợp khác
                authenticationFailureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Authentication failed"));
            }

        }


        // Validate token
        if (Strings.isNotBlank(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);
                boolean isValidToken = jwtTokenUtil.validateToken(token, userDetails);
                if (isValidToken) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (Exception ex) {
                logger.error("Validate token failed at: " + ex);
            }

        }
        filterChain.doFilter(request, response);
    }

    private String extractBearTokenFromHeader(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        if (requestTokenHeader == null) {
            return null;
        }
        return requestTokenHeader.split(" ")[1];
    }
}
