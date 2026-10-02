package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.entity.auth.RefreshToken;
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
    public RefreshToken createRefreshToken(Long userId) {
        String newFamilyId = UUID.randomUUID().toString();
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not exist"));
        refreshTokenRepository.deleteAllByUserId(userId);
        return saveNewToken(userId, newFamilyId);
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
    public RefreshToken rotateRefreshToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new SecurityException("Refresh Token không tồn tại."));

        // 1. PHÁT HIỆN TÁI SỬ DỤNG TOKEN (AUTOMATIC REUSE DETECTION)
        // Nếu refreshToken gửi lên ĐÃ TỪNG DÙNG hoặc ĐÃ BỊ THU HỒI -> Kẻ gian đang tấn công bằng token cũ!
        if (refreshToken.isUsed() || refreshToken.isRevoked()) {
            refreshTokenRepository.revokeByFamilyId(refreshToken.getFamilyId());
            throw new SecurityException("Cảnh báo bảo mật: Refresh Token đã từng được sử dụng! Toàn bộ phiên làm việc đã bị hủy.");
        }

        // 2. KIỂM TRA HẠN SỬ DỤNG
        if (refreshToken.getExpiryDate().compareTo(Instant.now())< 0) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new SecurityException("Refresh Token đã hết hạn, vui lòng đăng nhập lại.");
        }

        // 3. ĐÁNH DẤU TOKEN HIỆN TẠI LÀ ĐÃ DÙNG
        refreshToken.setUsed(true);
        refreshTokenRepository.save(refreshToken);

        // 4. TẠO TOKEN MỚI TRONG CÙNG FAMILY
        return saveNewToken(refreshToken.getUserId(), refreshToken.getFamilyId());
    }

    private RefreshToken saveNewToken(Long userId, String familyId) {
        RefreshToken newToken = RefreshToken.builder()
                .userId(userId)
                .token(passwordEncoder.encode(UUID.randomUUID().toString()))
                .expiryDate(Instant.now().plusSeconds(refreshExpiration))
                .familyId(familyId)
                .used(false)
                .revoked(false)
                .build();
        return refreshTokenRepository.save(newToken);
    }


    @Transactional
    @Override
    public void revokeAllUserToken(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }


}
