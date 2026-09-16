package com.springmatter.relearnspringboot.dto.record;

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
