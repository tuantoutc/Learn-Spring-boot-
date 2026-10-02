package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.*;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UsersService {

    AccessTokenResponse login(LoginRequest loginRequest);

    Map<String, String> register(RegisterRequest registerRequest);

    AccessTokenResponse getAccessTokenByRefreshToken(String refreshToken);

    ResponseCookie logout(String authorization);

}
