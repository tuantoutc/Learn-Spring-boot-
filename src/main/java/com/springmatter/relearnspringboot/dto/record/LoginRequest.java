package com.springmatter.relearnspringboot.dto.record;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String userName,
        @NotBlank String password
) {
}
