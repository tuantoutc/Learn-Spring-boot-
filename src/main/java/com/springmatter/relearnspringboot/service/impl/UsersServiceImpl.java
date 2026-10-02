package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.dto.record.AccessTokenResponse;
import com.springmatter.relearnspringboot.dto.record.LoginRequest;
import com.springmatter.relearnspringboot.dto.record.RegisterRequest;
import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
import com.springmatter.relearnspringboot.entity.auth.Users;
import com.springmatter.relearnspringboot.repository.auth.UserRepository;
import com.springmatter.relearnspringboot.security.jwt.JwtService;
import com.springmatter.relearnspringboot.service.RefreshTokenService;
import com.springmatter.relearnspringboot.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;


    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpireTime;


    @Override
    @Transactional
    public AccessTokenResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userName(),
                        request.password()
                )
        );
        Users user = (Users) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(Objects.requireNonNull(user));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        ResponseCookie responseCookie = buildRefreshTokenCookie(refreshToken.getToken(), refreshTokenExpireTime);

        return new AccessTokenResponse(accessToken, responseCookie, refreshToken.getToken());
    }


    @Override
    @Transactional
    public Map<String, String> register(RegisterRequest registerRequest) {
        Optional<Users> user = userRepository.findByUsername(registerRequest.userName());
        if (user.isPresent()) {
            return Map.of("error", "username already in use");
        }
        userRepository.save(Users.builder()
                .username(registerRequest.userName())
                .password(passwordEncoder.encode(registerRequest.password()))
                .enabled(false)
                .roles("ROLE_USER")
                .build());
        return Map.of("Register Account status", "ok");
    }

    @Transactional
    @Override
    public AccessTokenResponse getAccessTokenByRefreshToken(String oldRefreshToken) {
        // 1. Xoay vòng Refresh Token (hủy token cũ, sinh token mới trong cùng family)
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken);
        Users user = userRepository.findById(newRefreshToken.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found"));

        String newAccessToken = jwtService.generateToken(user);

        ResponseCookie newCookie = buildRefreshTokenCookie(newRefreshToken.getToken(), refreshTokenExpireTime);
        return new AccessTokenResponse(newAccessToken, newCookie, newRefreshToken.getToken());
    }

    @Override
    @Transactional
    public ResponseCookie logout(String authorization) {
        // xoa refresh token trong db
        String jwt = authorization.substring(7);
        String username = jwtService.extractUserName(jwt);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("user not found"));
        refreshTokenService.revokeAllUserToken(user.getId());
        // 3. Tạo cookie rác (maxAge = 0) để yêu cầu client xóa cookie
        return buildRefreshTokenCookie("", 0L);
    }

    public ResponseCookie buildRefreshTokenCookie(String refreshToken, Long maxAgeSeconds) {

        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)                      // Chống JavaScript đọc (chống XSS)
                .secure(false)                        // Chỉ gửi qua HTTPS ở local test chua có https
                .path("/api/v1/auth/refresh-token")  // Giới hạn cookie chỉ gửi lên endpoint refresh
                .maxAge(maxAgeSeconds)            // Thời gian sống 7 ngày
                .sameSite("Strict")                  // Chống CSRF
                .build();
    }


}
