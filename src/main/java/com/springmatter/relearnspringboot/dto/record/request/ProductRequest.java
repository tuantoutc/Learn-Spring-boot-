package com.springmatter.relearnspringboot.dto.record.request;

import java.math.BigDecimal;

public record ProductRequest(
         Long id,
         String name,
         BigDecimal price,
         String description,
         String status
) {
}
