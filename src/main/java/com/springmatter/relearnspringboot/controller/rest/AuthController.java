package com.springmatter.relearnspringboot.controller.rest;


import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.common.BaseController;
import com.springmatter.relearnspringboot.dto.record.AccessTokenResponse;
import com.springmatter.relearnspringboot.dto.record.LoginRequest;
import com.springmatter.relearnspringboot.dto.record.RefreshTokenRequest;
import com.springmatter.relearnspringboot.dto.record.RegisterRequest;
import com.springmatter.relearnspringboot.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final UsersService usersService;


    // xu dung

    @PostMapping("/login")
    public ApiResponse<ResponseEntity> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success(ResponseEntity.ok(usersService.login(request)));

    }

    @PostMapping("/register")
    public ApiResponse<ResponseEntity> registerAccount(@RequestBody @Valid RegisterRequest request) {
        return ApiResponse.success(ResponseEntity.ok(usersService.register(request)));

    }


    @PostMapping("/refresh-token")
    public ApiResponse<AccessTokenResponse> getAccessTokenByRefreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.success(usersService.getAccessTokenByRefreshToken(request));
    }

    @PostMapping("/logout")
    public ApiResponse<ResponseEntity<?>> logout(@RequestHeader("Authorization") String authorization) {
        return ApiResponse.success(ResponseEntity.ok(usersService.logout(authorization)));
    }






}
