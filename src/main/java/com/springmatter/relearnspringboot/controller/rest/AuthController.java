package com.springmatter.relearnspringboot.controller.rest;


import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.common.BaseController;
import com.springmatter.relearnspringboot.dto.record.*;
import com.springmatter.relearnspringboot.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController extends BaseController {

    private final UsersService usersService;


    // Đọc từ @CookieValue với HttpOnly Cookie (Bảo mật cao nhất cho Web Client / React / Vue / Next.js)

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request,
                                            HttpServletResponse response) {
        AccessTokenResponse accessTokenResponse = usersService.login(request);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenResponse.refreshToken());
        return ApiResponse.success(new TokenResponse(accessTokenResponse.accessToken()));
    }

    @PostMapping("/register")
    public ApiResponse<Map<String,String>> registerAccount(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.success(usersService.register(request));

    }

    @PostMapping("/refresh-token")
    public ApiResponse<TokenResponse> getAccessTokenByRefreshToken(
            @CookieValue(name = "refreshToken", required = false)
            @NotBlank(message = "Refresh token cookie không tìm thấy hoặc rỗng")
            String refreshToken,
            HttpServletResponse response) {
        AccessTokenResponse accessTokenResponse = usersService.getAccessTokenByRefreshToken(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenResponse.refreshToken());
        return ApiResponse.success(new TokenResponse(accessTokenResponse.accessToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestHeader("Authorization") String authorization, HttpServletResponse response) {

        ResponseCookie cleanCookie = usersService.logout(authorization);
        response.addHeader(HttpHeaders.SET_COOKIE, cleanCookie.toString());
        return ApiResponse.success("Logout sucessfully");
    }


}
