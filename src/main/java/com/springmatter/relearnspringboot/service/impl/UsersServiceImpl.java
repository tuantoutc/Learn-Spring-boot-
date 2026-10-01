package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.dto.record.AccessTokenResponse;
import com.springmatter.relearnspringboot.dto.record.LoginRequest;
import com.springmatter.relearnspringboot.dto.record.RefreshTokenRequest;
import com.springmatter.relearnspringboot.dto.record.RegisterRequest;
import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
import com.springmatter.relearnspringboot.entity.auth.Users;
import com.springmatter.relearnspringboot.repository.auth.RefreshTokenRepository;
import com.springmatter.relearnspringboot.repository.auth.UserRepository;
import com.springmatter.relearnspringboot.security.jwt.JwtService;
import com.springmatter.relearnspringboot.service.RefreshTokenService;
import com.springmatter.relearnspringboot.service.UsersService;
import lombok.RequiredArgsConstructor;
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

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    @Transactional
    public Map<String, String> login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.userName(),
                        request.password()
                )
        );
        Users user = (Users) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(Objects.requireNonNull(user));
        String refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return Map.of("access_token", accessToken,
                "refresh_token", refreshToken);
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

    @Override
    public AccessTokenResponse getAccessTokenByRefreshToken(RefreshTokenRequest refreshTokenRequest) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenRequest.refreshToken()).orElseThrow(() -> new RuntimeException("refresh token not found"));
        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException("refresh token is revoked");
        }
        refreshTokenService.verifyRefreshToken(refreshToken);
        Users user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));
        String newAccessToken = jwtService.generateToken(user);
        return new AccessTokenResponse(newAccessToken);
    }

    @Override
    @Transactional
    public String logout(String authorization) {
        String jwt = authorization.substring(7);
        String username = jwtService.extractUserName(jwt);
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found"));
        refreshTokenService.revokeAllUserToken(user.getId());
        return "Logout successful";
    }


}
