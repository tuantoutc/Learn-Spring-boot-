package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.AccessTokenResponse;
import com.springmatter.relearnspringboot.dto.record.LoginRequest;
import com.springmatter.relearnspringboot.dto.record.RegisterRequest;
import com.springmatter.relearnspringboot.dto.record.TokenResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UsersService {

    AccessTokenResponse login(LoginRequest loginRequest);

    Map<String, String> register(RegisterRequest registerRequest);

    TokenResponse getAccessTokenByRefreshToken(String refreshToken);

    ResponseCookie logout(String authorization);

}
