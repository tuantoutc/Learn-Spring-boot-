package com.springmatter.relearnspringboot.dto.record;

import java.util.List;

public record CategoryRequest(
        Long id,
        String name,
        String description,
        List<ProductRequest> products
) {
}
