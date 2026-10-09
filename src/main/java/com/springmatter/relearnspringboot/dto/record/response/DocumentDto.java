package com.springmatter.relearnspringboot.dto.record.response;

import org.springframework.web.multipart.MultipartFile;

public record DocumentDto (
        String title,
        String author,
        String department,
        String content,
        MultipartFile attachmentFile
) {
}
