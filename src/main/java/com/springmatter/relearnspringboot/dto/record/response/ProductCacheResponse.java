package com.springmatter.relearnspringboot.dto.record.response;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductCacheResponse(
        Long id,
        String name,
        BigDecimal price,
        String description,
        Integer stock,
        String cacheAt
) implements Serializable {
}
