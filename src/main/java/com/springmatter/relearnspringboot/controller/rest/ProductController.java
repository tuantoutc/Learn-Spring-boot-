package com.springmatter.relearnspringboot.controller.rest;

import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.common.BaseController;
import com.springmatter.relearnspringboot.dto.record.ProductResponse;
import com.springmatter.relearnspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/v1/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {
    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAll(
            @RequestParam(value = "page_no", defaultValue = "0") int pageNo,
            @RequestParam(value = "page_size", defaultValue ="10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "min_price", required = false) BigDecimal minPrice,
            @RequestParam(value = "max_price", required = false) BigDecimal maxPrice
    ) {
        Sort sort = sortType.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of((pageNo == 0 ? 0:pageNo -1 ) , pageSize, sort);

        return ApiResponse.success(productService.getAll(name, minPrice, maxPrice, category, pageable));
    }
}
