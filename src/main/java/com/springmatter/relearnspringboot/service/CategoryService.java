package com.springmatter.relearnspringboot.service;

import com.springmatter.relearnspringboot.dto.record.CategoryRequest;
import com.springmatter.relearnspringboot.dto.record.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest rq);

    CategoryResponse updateCategory(Long id, CategoryRequest rq);

    void deleteCategory(Long id);

    CategoryResponse getCategory(Long id);

    Page<CategoryResponse> getCategories(Pageable pageable);
}
