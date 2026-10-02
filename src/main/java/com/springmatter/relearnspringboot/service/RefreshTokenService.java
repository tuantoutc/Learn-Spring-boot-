package com.springmatter.relearnspringboot.service;


import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
import org.springframework.stereotype.Service;

@Service
public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

    RefreshToken rotateRefreshToken(String tokenString);

    void revokeAllUserToken(Long userId);
}
