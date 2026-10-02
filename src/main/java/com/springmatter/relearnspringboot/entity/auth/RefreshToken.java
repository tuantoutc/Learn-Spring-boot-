package com.springmatter.relearnspringboot.entity.auth;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_family_id", columnList = "familyId")
})@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;


    @Builder.Default
    private boolean revoked = false;

    @Builder.Default
    private boolean used = false; // MỚI: Đánh dấu đã dùng

    @Column(nullable = false)
    private String familyId;

    private Long userId;

}
