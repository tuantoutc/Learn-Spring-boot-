package com.springmatter.relearnspringboot.dto.record.response;

import java.io.Serializable;

public record CacheBenchmarkResponse(
        ProductCacheResponse product,
        String source,
        long executionTimMs,
        String dataloadReduction,
        String explanation

) implements Serializable {
}
