package com.springmatter.relearnspringboot.dto.record.response;

import org.springframework.core.io.ByteArrayResource;

public record DocumentFileDown(
        ByteArrayResource resource,
        String encodedFileName
) {
}
