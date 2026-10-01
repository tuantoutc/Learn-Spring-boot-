package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
import com.springmatter.relearnspringboot.entity.auth.Users;
import com.springmatter.relearnspringboot.repository.auth.RefreshTokenRepository;
import com.springmatter.relearnspringboot.repository.auth.UserRepository;
import com.springmatter.relearnspringboot.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;


    @Transactional
    @Override
    public String createRefreshToken(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not exist"));

        refreshTokenRepository.deleteAllByUserId(userId);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(passwordEncoder.encode(UUID.randomUUID().toString()))
                .userId(user.getId())
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    @Transactional
    @Override
    public void verifyRefreshToken(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new IllegalArgumentException("Refresh token expired");
        }
    }

    @Transactional
    @Override
    public void revokeAllUserToken(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }


}
