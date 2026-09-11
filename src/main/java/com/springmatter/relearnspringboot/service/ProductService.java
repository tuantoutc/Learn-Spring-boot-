package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {

    Page<ProductResponse> getAll(String name, BigDecimal minPrice, BigDecimal maxPrice, String category, Pageable pageable);
}
