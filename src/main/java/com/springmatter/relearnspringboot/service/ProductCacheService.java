package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.response.CacheBenchmarkResponse;
import com.springmatter.relearnspringboot.dto.record.response.ProductCacheResponse;

public interface ProductCacheService {

    ProductCacheResponse getProductById(Long id);

    CacheBenchmarkResponse getProductWithBenchmark(Long id);

    ProductCacheResponse updateProduct(Long id, ProductCacheResponse request);

    void deleteProduct(Long id);

    void clearAllCache();
}
