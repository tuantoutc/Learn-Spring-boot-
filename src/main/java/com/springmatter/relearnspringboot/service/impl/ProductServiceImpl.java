package com.springmatter.relearnspringboot.service.impl;

import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.entity.Product;
import com.springmatter.relearnspringboot.enums.SearchOperation;
import com.springmatter.relearnspringboot.mapper.ProductMapper;
import com.springmatter.relearnspringboot.repository.ProductRepository;
import com.springmatter.relearnspringboot.repository.specification.core.GenericSpecificationBuilder;
import com.springmatter.relearnspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getAll(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String category, Pageable pageable) {
        GenericSpecificationBuilder<Product> builder = new GenericSpecificationBuilder<>();
        if (StringUtils.hasText(keyword)) {
            builder.with("name", SearchOperation.LIKE_IGNORE_CASE, keyword.trim())
                    .withOr("description", SearchOperation.LIKE_IGNORE_CASE, keyword.trim());
        }

        builder.with("status", SearchOperation.EQUAL, "TRUE");

        if (StringUtils.hasText(category)) {
            builder.with("category", SearchOperation.LIKE_IGNORE_CASE, category.trim());
        }
        if (minPrice != null) {
            builder.with("price", SearchOperation.GREATER_THAN_EQUAL, minPrice);
        }
        if (maxPrice != null) {
            builder.with("price", SearchOperation.LESS_THAN_EQUAL, maxPrice);
        }

        Specification<Product> specification = builder.build();
        Page<Product> products = productRepository.findAll(specification, pageable);

        return products.map(productMapper::mapToProductResponse);
    }
}
