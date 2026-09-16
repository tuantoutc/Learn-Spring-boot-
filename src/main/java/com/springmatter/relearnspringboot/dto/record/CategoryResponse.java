package com.springmatter.relearnspringboot.dto.record;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        List<ProductResponse> products
) { }
