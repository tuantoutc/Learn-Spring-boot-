package com.springmatter.relearnspringboot.dto.record.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String category,
        String status,
        String description
) {
}
