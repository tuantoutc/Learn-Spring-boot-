package com.springmatter.relearnspringboot.dto.record.response;

import org.springframework.http.ResponseCookie;

public record AccessTokenResponse(
        String accessToken,
        ResponseCookie responseCookie,
        String refreshToken

) {
}
