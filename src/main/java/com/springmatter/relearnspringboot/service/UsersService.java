package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.AccessTokenResponse;
import com.springmatter.relearnspringboot.dto.record.LoginRequest;
import com.springmatter.relearnspringboot.dto.record.RefreshTokenRequest;
import com.springmatter.relearnspringboot.dto.record.RegisterRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UsersService {

    Map<String, String> login(LoginRequest loginRequest);

    Map<String, String> register(RegisterRequest registerRequest);

    AccessTokenResponse getAccessTokenByRefreshToken(RefreshTokenRequest refreshToken);

    String logout(String authorization);

}
