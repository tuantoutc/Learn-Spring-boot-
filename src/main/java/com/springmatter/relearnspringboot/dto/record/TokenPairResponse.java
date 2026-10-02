package com.springmatter.relearnspringboot.dto.record;

import org.springframework.http.ResponseCookie;

public record TokenPairResponse(
        String accessToken,
        ResponseCookie refreshCookie) {
}
