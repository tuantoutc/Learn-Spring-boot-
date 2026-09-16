package com.springmatter.relearnspringboot.controller.rest;

import com.springmatter.relearnspringboot.common.ApiResponse;
import com.springmatter.relearnspringboot.common.BaseController;
import com.springmatter.relearnspringboot.dto.record.CategoryRequest;
import com.springmatter.relearnspringboot.dto.record.CategoryResponse;
import com.springmatter.relearnspringboot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
public class CategoryController extends BaseController {
    private final CategoryService categoryService;

    @PostMapping()
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.success(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id,
                                                        @RequestBody CategoryRequest categoryRequest) {
        return ApiResponse.success(categoryService.updateCategory(id, categoryRequest));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success("Data has been deleted");
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategory(@PathVariable Long id) {
        return ApiResponse.success(categoryService.getCategory(id));
    }

    @GetMapping("")
    public ApiResponse<Page<CategoryResponse>> getAllCategory(@ParameterObject Pageable pageable) {
        return ApiResponse.success(categoryService.getCategories(pageable));
    }


}
