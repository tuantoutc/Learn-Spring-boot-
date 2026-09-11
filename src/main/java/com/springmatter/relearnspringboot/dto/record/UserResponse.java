package com.springmatter.relearnspringboot.dto.record;

public record UserResponse(
        Long id,
        String name,
        String username,
        String email,
        String phone,
        String address
) {
}
