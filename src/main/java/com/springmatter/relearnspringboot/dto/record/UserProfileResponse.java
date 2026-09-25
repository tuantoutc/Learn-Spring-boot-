package com.springmatter.relearnspringboot.dto.record;

import java.util.List;

public record UserProfileResponse (
        String username,
        List<String> roles
) {
}
