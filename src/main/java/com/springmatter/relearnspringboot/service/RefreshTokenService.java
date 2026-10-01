package com.springmatter.relearnspringboot.service;


import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {

    String createRefreshToken(Long userId);

    void verifyRefreshToken(RefreshToken refreshToken);

    void revokeAllUserToken(Long userId);
}
